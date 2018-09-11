package ril.implementation;
import ril.Object;
import ril.SubjectProperty;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubjectPropertyImplementation implements SubjectProperty {

    public final String API_Google = null;
    public final String API_Wiki = null;

    String subject;
    String property;

    ArrayList<String> objects;
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

    //TODO
    @Override
    public ArrayList<Object> grabObjects(String file) {
        return null;
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
    public void calculateAll(String file) throws Exception {
        ArrayList<Object> objects;
        if(file == null){
            objects = this.grabObjects();
        }
        else{
            objects = this.grabObjects(file);
        }

        if(objects == null) throw new Exception("Cannot grab objects with Subject: " + this.subject + " and property: " + this.property);

        Map<String, Integer> num_objs = this.grabNum_objs();
        if(num_objs == null) throw new Exception("Cannot grab num_objs with Subject: " + this.subject + " and property: " + this.property);

        Map<String, Integer> num_subj_objs = this.grabNum_subj_objs();
        if(num_subj_objs == null) throw new Exception("Cannot grab num_subj_objs with Subject: " + this.subject + " and property: " + this.property);

        ArrayList<Object> obj_coeff = this.calcRank_coeff();
        if(obj_coeff == null) throw new Exception("Cannot calculate obj_coeff with Subject: " + this.subject + " and property: " + this.property);
    }

    @Override
    public ArrayList<String> getObjects() {
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
