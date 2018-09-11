package ril.implementation;
import ril.Object;
import ril.SubjectProperty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubjectPropertyImplementation implements SubjectProperty {

    public final String API_Google = null;
    public final String API_Wiki = null;

    String subject;
    String property;

    ArrayList<Object> objects;
    Map<String, Integer> num_objs;
    Map<String, Integer> num_subj_objs;
    ArrayList<Object> rank_coeff;

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
    public ArrayList<Object> grabObjects() {
        return null;
    }

    //TODO
    @Override
    public Map<String, Integer> grabNum_objs() {
        return null;
    }

    @Override
    public ArrayList<Object> grabObjects(String filePath) {
        try {
            String encoding = "Unicode";
            ArrayList<Object> objects = new ArrayList<>();
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //judge whether the file exists.
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// the files should be in Unicode form.
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] line = lineTxt.split("\\s+");
                    String number = line[line.length - 1];
                    try {
                        Integer num = Integer.parseInt(number);
                        if( num instanceof  Integer == false ){
                            System.out.println("The inherent importance is not Integer.");
                            return null;
                        }
                        String object = "";
                        for(int i = 0; i<line.length-2; i++){
                            object = object + line[i]+ " ";
                        }
                        object = object + line[line.length-2];
                        Object obj = new Object(this.subject, this.property,object,num);
                        objects.add(obj);
                    }
                    catch(NumberFormatException e) {
                        System.out.println("Error while reading inherent importance." + "   " + number );
                        return null;
                    }
                }
                read.close();
                this.objects = objects;
                return objects;
            } else {
                System.out.println("The specified document does not exist.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error while reading document.");
            return null;
        }
    }

    //TODO
    @Override
    public Map<String, Integer> grabNum_subj_objs() {
        return null;
    }

    //TODO
    @Override
    public ArrayList<Object> calcRank_coeff() {
        return null;
    }

    @Override
    public void calculateAll(String filePath) throws Exception {
        ArrayList<Object> objects;
        if (filePath == null) {
            objects = this.grabObjects();
        } else {
            objects = this.grabObjects(filePath);
        }

        if (objects == null)
            throw new Exception("Cannot grab objects with Subject: " + this.subject + " and property: " + this.property);

        Map<String, Integer> num_objs = this.grabNum_objs();
        if (num_objs == null)
            throw new Exception("Cannot grab num_objs with Subject: " + this.subject + " and property: " + this.property);

        Map<String, Integer> num_subj_objs = this.grabNum_subj_objs();
        if (num_subj_objs == null)
            throw new Exception("Cannot grab num_subj_objs with Subject: " + this.subject + " and property: " + this.property);

        ArrayList<Object> obj_coeff = this.calcRank_coeff();
        if (obj_coeff == null)
            throw new Exception("Cannot calculate obj_coeff with Subject: " + this.subject + " and property: " + this.property);
    }

    @Override
    public ArrayList<Object> getObjects() {
        return this.objects;
    }

    @Override
    public Map<String, Integer> getNum_objs() {
        return this.num_objs;
    }

    @Override
    public Map<String, Integer> getNum_subj_objs() {
        return this.num_subj_objs;
    }

    @Override
    public ArrayList<Object> getRank_coeff() {
        return this.rank_coeff;
    }


}