package ril.implementation;

import ril.facotry.API_Factory;
import ril.Object;
import ril.SubjectProperty;
import ril.facotry.Sort_Factory;

import java.util.ArrayList;

import static ril.facotry.Sort_Factory.*;

// This class implements the interface "SubjectProperty".
// We don't comment the @Overload methods.
public class SubjectPropertyImplementation implements SubjectProperty {

    String subject_id;
    String subject_label;
    String property_id;
    String property_label;

    ArrayList<Object> objects;
    public Sort_Factory.Parameter sort_order = null;

    double IDCG = -1.0;

    public SubjectPropertyImplementation(String subject_id, String property_id) {
        this.subject_id = subject_id;
        this.property_id = property_id;
        this.subject_label = API_Factory.scrapeLabelByID_Wikidata(subject_id);
        this.objects = new ArrayList<>();
        this.property_label = API_Factory.scrapeLabelByID_Wikidata(property_id);
    }

    public SubjectPropertyImplementation(String subject_id, String property_id, String subject_label, String property_label) {
        this.subject_id = subject_id;
        this.property_id = property_id;
        this.subject_label = subject_label;
        this.property_label = property_label;
    }

    @Override
    public String getSubject_id() {
        return this.subject_id;
    }

    @Override
    public String getSubject_label() {
        return this.subject_label;
    }

    @Override
    public String getProperty_id() {
        return this.property_id;
    }

    @Override
    public String getProperty_label() {
        return property_label;
    }

    public double getIDCG() {
        return IDCG;
    }

    public void setObjects(ArrayList<Object> objects) {
        this.objects = objects;
    }

    @Override
    public void grabObjects(){
        this.objects = API_Factory.scrapeObjects_IDs_Wiki(this.subject_id,this.property_id);

        API_Factory.scrapePositive_Objects_Wikidata(this.subject_id, this.property_id,this.objects);

        for(int i=0; i<this.objects.size();i++) {
            String[] name_pices = objects.get(i).getObject_label().split("\\s+");
            String name = "";
            if (name_pices.length > 1) {
                for (int j = 0; j < name_pices.length - 1; j++) {
                    name = name + name_pices[j] + "_";
                }
                name = name + name_pices[name_pices.length - 1];
            } else {
                name = objects.get(i).getObject_label();
            }
            int inherent_importance = API_Factory.grabViewers(name);
            this.objects.get(i).setImportance(inherent_importance);
        }

        sortObject(Sort_Factory.Parameter.IMPORTANCE);
        int sum = 0;
        for(int i=0; i<this.objects.size();i++){
            sum = sum + this.objects.get(i).getImportance();
        }
        double average = 1.0 * sum / this.objects.size();

        System.out.println("Size of all awards : " + this.objects.size());
        System.out.println("Average : " + average);

        // select elements whose inherent importance is top 100.
        int size = this.objects.size()> 100? 100:this.objects.size();
        ArrayList<Object> objects_top100 = new ArrayList<>();
        for(int i=0; i<size;i++){
            objects_top100.add(this.objects.get(i));
        }
        this.objects = objects_top100;



        // select elements whose inherent importance is beyond average.
        /*
        ArrayList<Object> objects_beyond_average = new ArrayList<>();
        for(int i=0; i<this.objects.size();i++){
            if(this.objects.get(i).getImportance() >= average){
                objects_beyond_average.add(this.objects.get(i));
            }
        }
        this.objects = objects_beyond_average;
        */
    }

    @Override
    public void grabOccurrence(API_Occurrence api){
        if(this.objects == null)
            return;
        if(api == null)
            return;
        for(int i = 0; i< objects.size(); i++){
            int count = API_Factory.scrapeOccurrence(objects.get(i).getObject_label(), api);
            objects.get(i).setOccurrence(count,api);
        }
    }

    @Override
    public void grabCo_Occurrence(API_Occurrence api) {
        if (this.objects == null)
            return;
        if (api == null)
            return;
        for (int i = 0; i < objects.size(); i++) {
            int count = API_Factory.scrapeCo_occurrence(this.subject_label, objects.get(i).getObject_label(),api);
            objects.get(i).setCo_Occurrence(count,api);
        }
    }

    @Override
    public void computeCo_Occurrence_Coefficient(API_Occurrence api) {
        for(int i=0; i<objects.size();i++){
            Object obj = objects.get(i);
            int occurrence = obj.getOccurrence(api);
            int co_occurrence = obj.getCo_Occurrence(api);
            if( occurrence== -1 ||  co_occurrence== -1){
                System.out.println(" Num_object or Num_subj_object is not grabbed. We remove it. Object is : " + obj.getObject_label());
                this.objects.remove(obj);
                continue;
            }
            else{
                if(occurrence == 0){
                    this.objects.get(i).setCo_occur_coeff(0.0, api);
                }else {
                    double coeff = 1.0 * co_occurrence / occurrence;
                    if (coeff > 1.0) {
                        this.objects.get(i).setCo_occur_coeff(0.0, api);
                    } else {
                        this.objects.get(i).setCo_occur_coeff(coeff, api);
                    }
                }
            }
        }
        if(api == API_Occurrence.Bing) {
            sortObject(Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Bing);
        }else{
            sortObject(Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Wikipedia);
        }
    }

    @Override
    public void calculateAll(String filePath, Sort_Factory.Parameter parameter, API_Occurrence api){
      //  if (filePath == null) {
            this.grabObjects();
     //   } else {
      //      this.grabObjects(filePath);
     //   }
        this.grabOccurrence(api);
        this.grabCo_Occurrence(api);
        this.computeCo_Occurrence_Coefficient(api);
        this.sortObject(parameter);
    }

    @Override
    public ArrayList<Object> getObjects() {
        return this.objects;
    }

    /**
     *
     * @param parameter is "num_sub_obj"  for co-occurrence of subject and object,
     *                   or "num_obj" for occurrence of object,
     *                   or "importance" for inherent importance,
     *                   or "rank_coeff" for the rank coefficient (Namely, num_sub_obj / sum_obj).
     * @return the decreasing sorted list according the parameter.
     */
    @Override
    public void sortObject(Sort_Factory.Parameter parameter) {
        if(parameter != Sort_Factory.Parameter.IMPORTANCE &&  parameter != Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Bing && parameter != Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Wikipedia && parameter != Sort_Factory.Parameter.GROUND_TRUTH && parameter != Sort_Factory.Parameter.COUNTTRIPLE){
            return;
        }else if(parameter == Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Bing){
            this.objects = sortObject_Rank_Coeff(this.objects,API_Occurrence.Bing);
        }else if(parameter == Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Wikipedia){
            this.objects = sortObject_Rank_Coeff(this.objects,API_Occurrence.Wikipedia);
        }else if(parameter == Sort_Factory.Parameter.IMPORTANCE){
            this.objects = sortObject_Importance(this.objects);
        }else if(parameter == Sort_Factory.Parameter.COUNTTRIPLE){
            this.objects = sortObject_countTriple(this.objects);
        }else{
            this.objects = sortObject_GoundTruth(this.objects);
        }
        this.sort_order = parameter;
    }

    private void computeIDCG(){
        this.sortObject(Sort_Factory.Parameter.GROUND_TRUTH);
        double sum = 0.0;
        for(int i=0; i<this.objects.size();i++){
            Object obj = this.objects.get(i);
            sum = sum + ( obj.getGroundTruth() * Math.log(2.0) / Math.log(obj.getRank_GroundTruth() + 1));
        }
        this.IDCG = sum;
    }

    /**
     * This method computes the value DCG
     * @return the DCG value for @this.objects.
     */
    private double computeDCG(){
        double sum = 0.0;
        for(int i=0; i<this.objects.size();i++){
            Object obj = this.objects.get(i);
            sum = sum + ( obj.getGroundTruth() * Math.log(2.0) / Math.log(i + 2));
        }
        return sum;
    }

    @Override
    /**
     * compute NDCG for the list sorted by @parameter.
     * If @parameter == null, then we don't sort this list.
     *      else, we sort @this.objects at first, then compute the value for DCG.
     * Finally we compute and output NDCG.
     * @param parameter the sort-parameter
     * @return NDCG
     */
    public double computeNDCG(Sort_Factory.Parameter parameter){
        this.sortObject(parameter);
        double DCG = this.computeDCG();
        if(this.IDCG < 0.0){
            this.computeIDCG();
        }
        return DCG / this.IDCG;
    }

}