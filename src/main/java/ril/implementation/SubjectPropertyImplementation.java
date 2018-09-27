package ril.implementation;

import ril.API_Factory;
import ril.Object;
import ril.SubjectProperty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

// This class implements the interface "SubjectProperty".
// We don't comment the @Overload methods.
public class SubjectPropertyImplementation implements SubjectProperty {

    String subject_id;
    String subject_label;
    String property_id;

    ArrayList<Object> objects;
    public Object.Parameter sort_order = null;

    public SubjectPropertyImplementation(String subject_id, String property_id) {
        this.subject_id = subject_id;
        this.property_id = property_id;
        this.subject_label = API_Factory.scrapeLabelByID_WIKI(subject_id);
    }

    @Override
    public String getSubject() {
        return this.subject_id;
    }

    @Override
    public String getProperty() {
        return this.property_id;
    }

    @Override
    public void grabObjects(){
        ArrayList<String> ids = API_Factory.scrapeObjects_IDs_Wiki(this.property_id);
        ArrayList<Object> objs = new ArrayList<>();

        ArrayList<String> positive_objects = API_Factory.scrapeWiki(this.subject_id, this.property_id);

        for(int i=0; i<ids.size();i++){
            String obj = API_Factory.scrapeLabelByID_WIKI(ids.get(i));

            if(obj == null){
                continue;
            }
            if(positive_objects.contains(obj)){
                continue;
            }

            String[] name_pices = obj.split("\\s+");
            String name = "";
            if(name_pices.length > 1){
                for(int j=0; j<name_pices.length-1;j++){
                    name = name + name_pices[j] + "_";
                }
                name = name + name_pices[name_pices.length-1];
            }
            else{
                name = obj;
            }

            int inherent_importance = API_Factory.grabViewers(name);

            Object object = new Object(this.subject_id,this.property_id,obj,inherent_importance);
            objs.add(object);
        }

        int sum = 0;
        for(int i=0; i<objs.size();i++){
            sum = sum + objs.get(i).getImportance();
        }
        double average = 1.0 * sum / objs.size();

        System.out.println("Size of all awards : " + objs.size());
        System.out.println("Average : " + average);

        ArrayList<Object> objects_beyond_average = new ArrayList<>();
        for(int i=0; i<objs.size();i++){
            if(objs.get(i).getImportance() >= average){
                objects_beyond_average.add(objs.get(i));
            }
        }
        this.objects = objects_beyond_average;

    }

    @Override
    public void grabNum_objs(){
        if(this.objects == null)
            return;
        for(int i = 0; i< objects.size(); i++){
            //int count = API_Factory.scrapeBing(objects.get(i).getObject());
            int count = API_Factory.scrapeOccurrence_Wikipedia(objects.get(i).getObject());
            objects.get(i).setNum_object(count);
        }
        return;
    }

    @Override
    public void grabObjects(String filePath) {
        try {
            String encoding = "Unicode";
            ArrayList<Object> objects = new ArrayList<Object>();
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //judge whether the file exists.
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// the files should be in Unicode form.
                BufferedReader bufferedReader = new BufferedReader(read);

                ArrayList<String> positive_objects = API_Factory.scrapeWiki(this.subject_id, this.property_id);
                String lineTxt = null;
                int sum = 0;
                int count = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] line = lineTxt.split("\\s+");
                    String number = line[line.length - 1];
                    try {
                        Integer num = Integer.parseInt(number);
                        if( num instanceof  Integer == false ){
                            System.out.println("The inherent importance is not Integer.");
                            return;
                        }
                        String object = "";
                        for(int i = 0; i<line.length-2; i++){
                            object = object + line[i]+ " ";
                        }
                        object = object + line[line.length-2];
                        if(positive_objects.contains(object)) {
                            continue;
                        }

                        Object obj = new Object(this.subject_id, this.property_id,object,num);
                        objects.add(obj);
                        sum = sum + num;
                        count = count + 1;
                    }
                    catch(NumberFormatException e) {
                        System.out.println("Error while reading inherent importance." + "   " + number );
                        return;
                    }
                }
                double average = 1.0 * sum / count;
                System.out.println("Size of all awards :" + objects.size());
                System.out.println("Average : " + average);

                ArrayList<Object> objects_beyond_average = new ArrayList<>();
                for(int i = 0; i< objects.size();i++){
                    if(objects.get(i).getImportance() >=average){
                        objects_beyond_average.add(objects.get(i));
                    }
                    else{
                        break;
                    }
                }
                read.close();
                this.objects = objects_beyond_average;
            } else {
                System.out.println("The specified document does not exist.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error while reading document.");
            return;
        }
    }

    @Override
    public void grabNum_subj_objs() {
        if(this.objects == null) {
            System.out.println("Objects does not exist.");
            return;
        }

        for(int i = 0; i< objects.size(); i++){
            //int count = API_Factory.scrapeBing(subject_label + " " + objects.get(i).getObject());
            int count = API_Factory.scrapeCo_occurrence_Wikipedia(this.subject_label,objects.get(i).getObject());
            objects.get(i).setNum_subj_object(count);
        }
    }

    @Override
    public void calcRank_coeff() {
        for(int i=0; i<objects.size();i++){
            Object obj = objects.get(i);
            int num_obj = obj.getNum_object();
            int num_sub_obj = obj.getNum_subj_object();
            if( num_obj== -1 ||  num_sub_obj== -1){
                System.out.println(" Num_object or Num_subj_object is not grabbed. We remove it. Object is : " + obj.getObject());
                this.objects.remove(obj);
                continue;
            }
            else{
                this.objects.get(i).setRank_coeff( 1.0 * num_sub_obj / num_obj);
            }
        }
    }

    @Override
    public void calculateAll(String filePath, Object.Parameter parameter){
        if (filePath == null) {
            this.grabObjects();
        } else {
            this.grabObjects(filePath);
        }
        this.grabNum_objs();
        this.grabNum_subj_objs();
        this.calcRank_coeff();
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
    public void sortObject(Object.Parameter parameter) {
        if(parameter != Object.Parameter.IMPORTANCE &&  parameter != Object.Parameter.RANK_COEFF){
            return;
        }
        else if(parameter == Object.Parameter.RANK_COEFF){
            this.sortObject_Importance();
            this.sortObject_Rank_Coeff();
        }
        else{
            this.sortObject_Rank_Coeff();
            this.sortObject_Importance();
        }
        this.sort_order = parameter;
    }

    /**
     * print out the first @top results. If @top larger than the size of objects, then print all.
     * @param top the number, how many results will be printed.
     */
    public void printResults(int top) {
        DecimalFormat df = new DecimalFormat("#.##");

        int num = top > this.objects.size() ? this.objects.size() : top;
        System.out.println("SubjectProperty{" +
                "subject='" + subject_id + '\'' +
                ", property='" + property_id + '\'' +
                '}');
        System.out.println("Objects" + "       " + "RANK_COEFF" + "   " + "Rank_RANK_COEFF" + "   " + "IMPORTANCE" + "   " + "Rank_IMPORTANCE" + "   " + "1:1 Rank" + "   " + "1:2 Rank" + "   " + "2:1 Rank" + "   " + "1:3 Rank" + "   " + "3:1 Rank");
        for(int i=0; i< num; i++){
            Object obj = this.objects.get(i);
            System.out.println(obj.getObject() + "   " + obj.getRank_coeff() + "   " + obj.getRank_rank_coeff() + "   " + obj.getImportance() + "   " + obj.getRank_importance() + "   " + df.format(obj.getRank_rank_coeff() * 0.5 + obj.getRank_importance() * 0.5) + "   " + df.format(obj.getRank_rank_coeff() * 1.0 / 3 + obj.getRank_importance() * 2.0 / 3) + "   " + df.format(obj.getRank_rank_coeff() * 2.0 / 3 + obj.getRank_importance() * 1.0 / 3) + "   " + df.format(obj.getRank_rank_coeff() * 1.0 / 4 + obj.getRank_importance() * 3.0 / 4) + "   " + df.format(obj.getRank_rank_coeff() * 3.0 / 4 + obj.getRank_importance() * 1.0 / 4) );
        }
    }

    /**
     * This method sorts the object list according to the occurrence.
     */
    private void sortObject_Num_Object(){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(this.objects.get(0));
        for(int i=1; i<this.objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(this.objects.get(i).getNum_object() > objs.get(j).getNum_object()){
                    objs.add(j,this.objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(this.objects.get(i));
            }
        }
        this.objects = objs;
    }

    /**
     * This method sorts the object list according to the co-occurrence.
     */
    private void sortObject_Num_Sub_Object(){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(this.objects.get(0));
        for(int i=1; i<this.objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(this.objects.get(i).getNum_subj_object() > objs.get(j).getNum_subj_object()){
                    objs.add(j,this.objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(this.objects.get(i));
            }
        }
        this.objects = objs;
    }

    /**
     * This method sort the object list according to the inherent importance.
     */
    private void sortObject_Importance(){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(this.objects.get(0));
        for(int i=1; i<this.objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(this.objects.get(i).getImportance() > objs.get(j).getImportance()){
                    objs.add(j,this.objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(this.objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_importance(i + 1);
        }
        this.objects = objs;
    }

    /**
     * This method sort the object list according to the rank coefficient.
     */
    private void sortObject_Rank_Coeff(){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(this.objects.get(0));
        for(int i=1; i<this.objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(this.objects.get(i).getRank_coeff() > objs.get(j).getRank_coeff()){
                    objs.add(j,this.objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(this.objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_rank_coeff(i + 1);
        }
        this.objects = objs;
    }

}