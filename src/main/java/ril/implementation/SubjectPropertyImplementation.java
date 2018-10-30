package ril.implementation;

import ril.Object;
import ril.SubjectProperty;
import ril.facotry.API_Factory;
import ril.facotry.Sort_Factory;

import java.util.ArrayList;
import java.util.List;

import static ril.facotry.Sort_Factory.*;

public class SubjectPropertyImplementation implements SubjectProperty {

    String subject_id;
    String subject_label;
    String property_id;
    String property_label;
    ArrayList<Object> objects;
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

    @Override
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

        this.objects = sortObject(this.objects,Sort_Factory.Parameter.IMPORTANCE);
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
    public List<Object> getNegativeObjects() {
        ArrayList<Object> objects_removePositiv = new ArrayList<>();
        for(int i=0; i<this.objects.size();i++){
            if(this.objects.get(i).isPositive_obj() == false){
                objects_removePositiv.add(this.objects.get(i));
            }
        }
        return objects_removePositiv;
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
            this.objects = sortObject(this.objects, Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Bing);
        }else{
            this.objects = sortObject(this.objects,Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Wikipedia);
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
        this.sortObject(this.objects, parameter);
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
    public ArrayList<Object> sortObject(List<Object> objects, Sort_Factory.Parameter parameter) {
        if(parameter == null || objects == null){
            return null;
        }
        switch(parameter){
            case CO_OCCURRENCE_COEFFICIENT_Bing: return sortObject_Rank_Coeff(objects,API_Occurrence.Bing);
            case CO_OCCURRENCE_COEFFICIENT_Wikipedia: return sortObject_Rank_Coeff(objects,API_Occurrence.Wikipedia);
            case IMPORTANCE: return sortObject_Importance(objects);
            case NUMSUPERCLASS: return sortObject_NumSuperclass(objects);
            case NUMSUBCLASS: return sortObject_NumSubclass(objects);
            case COUNTFACTS: return sortObject_countTriple(objects);
            case DISTANCE: return sortObject_Distance(objects);
            case GROUND_TRUTH: return sortObject_GoundTruth(objects);
            case NUMSUBJECT: return sortObject_NumSubject(objects);
            case NUMSUBJECTPROPERTY: return sortObject_NumSubjectProperty(objects);
            default: return  null;
        }
    }

    @Override
    public void sortObject(Sort_Factory.Parameter parameter) {
        if(parameter == null){
            return;
        }
        switch (parameter){
            case CO_OCCURRENCE_COEFFICIENT_Bing: this.objects = sortObject_Rank_Coeff(this.objects,API_Occurrence.Bing); break;
            case CO_OCCURRENCE_COEFFICIENT_Wikipedia: this.objects = sortObject_Rank_Coeff(this.objects,API_Occurrence.Wikipedia); break;
            case IMPORTANCE: this.objects = sortObject_Importance(this.objects); break;
            case COUNTFACTS: this.objects = sortObject_countTriple(this.objects); break;
            case NUMSUPERCLASS: this.objects = sortObject_NumSuperclass(this.objects);break;
            case NUMSUBCLASS: this.objects = sortObject_NumSubclass(this.objects); break;
            case DISTANCE: this.objects = sortObject_Distance(this.objects);break;
            case GROUND_TRUTH: this.objects = sortObject_GoundTruth(this.objects);break;
            case NUMSUBJECT: this.objects = sortObject_NumSubject(this.objects); break;
            case NUMSUBJECTPROPERTY: this.objects = sortObject_NumSubjectProperty(this.objects);break;
            default:
        }
    }

    private void computeIDCG(List<Object> objects_removePositive){
        double sum = 0.0;
        for(int i=0; i<objects_removePositive.size();i++){
            Object obj = objects_removePositive.get(i);
            sum = sum + ( (Math.pow(2.0,obj.getGroundTruth() )- 1) * Math.log(2.0) / Math.log(obj.getRank_GroundTruth() + 1));
        }
        this.IDCG = sum;
    }

    /**
     * This method computes the value DCG
     * @return the DCG value for @objects_removePositiv.
     */
    private double computeDCG(List<Object> objects_removePositive){
        double sum = 0.0;
        for(int i=0; i<objects_removePositive.size();i++){
            Object obj = objects_removePositive.get(i);
            sum = sum + ( (Math.pow(2.0,obj.getGroundTruth() )- 1) * Math.log(2.0) / Math.log(i + 2));
        }
        return sum;
    }

    /**
     * compute NDCG for the list sorted by @parameter.
     * If @parameter == null, then we don't sort this list.
     *      else, we sort @this.objects at first, then compute the value for DCG.
     * Finally we compute and output NDCG.
     * @param parameter the sort-parameter
     * @return NDCG
     */
    @Override
    public double computeNDCG(Sort_Factory.Parameter parameter){
        List<Object> objects_removePositive = this.getNegativeObjects();

        objects_removePositive = this.sortObject(objects_removePositive, parameter);
        double DCG = this.computeDCG(objects_removePositive);
        if(this.IDCG < 0.0){
            objects_removePositive = this.sortObject(objects_removePositive, Sort_Factory.Parameter.GROUND_TRUTH);
            this.computeIDCG(objects_removePositive);
        }
        return DCG / this.IDCG;
    }

    @Override
    public void grabNumSuperclass() {
        if (this.objects == null)
            return;
        for (int i = 0; i < objects.size(); i++) {
            int count = API_Factory.grabNumberForSuperclass(objects.get(i).getObject_ID());
            objects.get(i).setNumSuperclass(count);
        }
    }

    @Override
    public void grabNumSubclass() {
        if (this.objects == null)
            return;
        for (int i = 0; i < objects.size(); i++) {
            int count = API_Factory.grabNumberForSubclass(objects.get(i).getObject_ID());
            objects.get(i).setNumSubclass(count);
        }
    }

    @Override
    public void grabDistance() {
        if (this.objects == null)
            return;
        for (int i = 0; i < objects.size(); i++) {
            int count = API_Factory.grabDistance(subject_id, objects.get(i).getObject_ID());
            objects.get(i).setDistance(count);
        }
    }

    @Override
    public void grabNumSubject() {
        if (this.objects == null)
            return;
        for (int i = 0; i < objects.size(); i++) {
            int count = API_Factory.grabNumSubject_given_PropertyAndObject(objects.get(i).getProperty_ID(),objects.get(i).getObject_ID());
            objects.get(i).setNumSubject(count);
        }
    }

    @Override
    public void grabNumSubjectProperty() {
        if (this.objects == null)
            return;
        for (int i = 0; i < objects.size(); i++) {
            int count = API_Factory.grabNumSubjectProperty_given_Object(objects.get(i).getObject_ID());
            objects.get(i).setNumSubjectProperty(count);
        }
    }
}