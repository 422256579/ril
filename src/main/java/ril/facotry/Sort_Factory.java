package ril.facotry;

import ril.Object;
import ril.SubjectProperty;

import java.util.ArrayList;
import java.util.List;

public class Sort_Factory {

    /**
     * This is the enum for sorting the object list.
     * "CO_OCCURRENCE_COEFFICIENT" for rank coefficient.
     * "IMPORTANCE" for inherent importance.
     */
    public enum Parameter{
        OCCURENCE_Bing, OCCURRENCE_Wikipedia, CO_OCCURRENCE_Bing, CO_OCCURRENCE_Wikipedia, CO_OCCURRENCE_COEFFICIENT_Bing,CO_OCCURRENCE_COEFFICIENT_Wikipedia, IMPORTANCE, GROUND_TRUTH, COUNTFACTS, NUMSUPERCLASS, NUMSUBCLASS, DISTANCE, NUMSUBJECT, NUMSUBJECTPROPERTY
    }

    /**
     * This method sorts the object list according to the occurrence.
     */
    public static ArrayList<Object> sortObject_Occurrence(List<Object> objects, SubjectProperty.API_Occurrence api){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getOccurrence(api) > objs.get(j).getOccurrence(api)){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        return objs;
    }

    /**
     * This method sorts the object list according to the co-occurrence.
     */
    public static ArrayList<Object> sortObject_Co_Occurrence(List<Object> objects, SubjectProperty.API_Occurrence api){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getCo_Occurrence(api) > objs.get(j).getCo_Occurrence(api)){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        return objs;
    }

    /**
     * This method sort the object list according to the inherent importance.
     */
    public static ArrayList<Object> sortObject_Importance(List<Object> objects){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getImportance() > objs.get(j).getImportance()){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_importance(i + 1);
        }
        return objs;
    }

    /**
     * This method sort the object list according to the COUNTFACTS.
     */
    public static ArrayList<Object> sortObject_countTriple(List<Object> objects){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getCountFacts() > objs.get(j).getCountFacts()){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_CountFacts(i + 1);
        }
        return objs;
    }

    /**
     * This method sort the object list according to the groundTruth.
     */
    public static ArrayList<Object> sortObject_GoundTruth(List<Object> objects){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getGroundTruth() > objs.get(j).getGroundTruth()){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_GroundTruth(i + 1);
        }
        return objs;
    }

    /**
     * This method sort the object list according to the rank coefficient.
     */
    public static ArrayList<Object> sortObject_Rank_Coeff(List<Object> objects, SubjectProperty.API_Occurrence api){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getCo_occur_coeff(api) > objs.get(j).getCo_occur_coeff(api)){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_co_occur_coeff(i+1, api);
        }
        return objs;
    }

    /**
     * This method sort the object list according to the number of superclass.
     */
    public static ArrayList<Object> sortObject_NumSuperclass(List<Object> objects){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getNumSuperclass() > objs.get(j).getNumSuperclass()){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_NumSuperclass(i + 1);
        }
        return objs;
    }

    /**
     * This method sort the object list according to the number of subclass.
     */
    public static ArrayList<Object> sortObject_NumSubclass(List<Object> objects){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getNumSubclass() > objs.get(j).getNumSubclass()){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_NumSubclass(i + 1);
        }
        return objs;
    }

    /**
     * This method sort the object list according to the distance.
     */
    public static ArrayList<Object> sortObject_Distance(List<Object> objects){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getDistance() > objs.get(j).getDistance()){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_Distance(i + 1);
        }
        return objs;
    }

    /**
     * This method sort the object list according to the number of subject based on fixed property and object.
     */
    public static ArrayList<Object> sortObject_NumSubject(List<Object> objects){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getNumSubject() > objs.get(j).getNumSubject()){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_NumSubject(i + 1);
        }
        return objs;
    }

    /**
     * This method sort the object list according to the number of subject based on fixed object.
     */
    public static ArrayList<Object> sortObject_NumSubjectProperty(List<Object> objects){
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(objects.get(0));
        for(int i=1; i<objects.size(); i++){
            boolean insert = false;
            for(int j=0;j<objs.size();j++){
                if(objects.get(i).getNumSubjectProperty() > objs.get(j).getNumSubjectProperty()){
                    objs.add(j,objects.get(i));
                    insert = true;
                    break;
                }
            }
            if(insert == true){
                continue;
            }
            else{
                objs.add(objects.get(i));
            }
        }
        for(int i=0; i<objs.size();i++){
            objs.get(i).setRank_NumSubjectProperty(i + 1);
        }
        return objs;
    }
}
