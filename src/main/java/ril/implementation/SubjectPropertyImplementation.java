package ril.implementation;

import ril.API_Factory;
import ril.Object;
import ril.SubjectProperty;

import java.io.*;
import java.util.ArrayList;

public class SubjectPropertyImplementation implements SubjectProperty {

    String subject;
    String property ;

    ArrayList<Object> objects;
    public Object.Parameter sort_order = null;

    public SubjectPropertyImplementation(String subject, String property) {
        this.subject = subject;
        this.property = property;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public String getProperty() {
        return this.property;
    }

    //TODO
    @Override
    public void grabObjects(){
        return ;
    }

    @Override
    public void grabNum_objs(){
        if(this.objects == null)
            return;
        for(int i = 0; i< objects.size(); i++){
            try {
                int count = API_Factory.scrapeBing(objects.get(i).getObject());
                objects.get(i).setNum_object(count);
            }catch (IOException e){
                System.out.println("IOException" + e);
                e.printStackTrace();
                return ;
            }
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

                ArrayList<String> positive_objects = API_Factory.scrapeWiki(this.subject, this.property);
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

                        Object obj = new Object(this.subject, this.property,object,num);
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
            try {
                int count = API_Factory.scrapeBing(objects.get(i).getSubject() + " " + objects.get(i).getObject());
                objects.get(i).setNum_subj_object(count);
            }catch (IOException e){
                System.out.println("IOException" + e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void calcRank_coeff() {
        for(int i=0; i<objects.size();i++){
            int num_obj = objects.get(i).getNum_object();
            int num_sub_obj = objects.get(i).getNum_subj_object();
            if( num_obj== -1 ||  num_sub_obj== -1){
                System.out.println(" Num_object or Num_subj_object is not grabbed.");
                return;
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
        if(parameter != Object.Parameter.IMPORTANCE && parameter != Object.Parameter.NUM_OBJ && parameter!= Object.Parameter.NUM_SUB_OBJ && parameter != Object.Parameter.RANK_COEFF){
            return;
        }
        else if(parameter == Object.Parameter.RANK_COEFF){
            this.sortObject_Rank_Coeff();
        }
        else if(parameter == Object.Parameter.NUM_SUB_OBJ){
            this.sortObject_Num_Sub_Object();
        }
        else if(parameter == Object.Parameter.IMPORTANCE){
            this.sortObject_Importance();
        }
        else{
            this.sortObject_Num_Object();
        }
        this.sort_order = parameter;
    }

    /**
     * print out the first @top results. If @top larger than the size of objects, then print all.
     * @param top the number, how many results will be printed.
     */
    public void printResults(int top) {
        int num = top > this.objects.size() ? this.objects.size() : top;
        System.out.println("SubjectProperty{" +
                "subject='" + subject + '\'' +
                ", property='" + property + '\'' +
                '}');
        System.out.println("Objects" + "                " + "RANK_COEFF" + "            " + "NUM_SUB_OBJ" + "           " + "NUM_OBJ" + "           " + "IMPORTANCE");
        for(int i=0; i< num; i++){
            System.out.println(this.objects.get(i).getObject() + "   " + this.objects.get(i).getRank_coeff() + "   " + this.objects.get(i).getNum_subj_object() + "   " + this.objects.get(i).getNum_object() + "   " + this.objects.get(i).getImportance());
        }
    }

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
        this.objects = objs;
    }

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
        this.objects = objs;
    }

}