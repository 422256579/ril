package ril.facotry;

import ril.Object;
import ril.SubjectProperty;

import java.util.ArrayList;

public class Sort_Factory {

    /**
     * This is the enum for sorting the object list.
     * "CO_OCCURRENCE_COEFFICIENT" for rank coefficient.
     * "IMPORTANCE" for inherent importance.
     */
    public enum Parameter{
        OCCURENCE_Bing, OCCURRENCE_Wikipedia, CO_OCCURRENCE_Bing, CO_OCCURRENCE_Wikipedia, CO_OCCURRENCE_COEFFICIENT,IMPORTANCE
    }

    /**
     * This method sorts the object list according to the occurrence.
     */
    public static ArrayList<Object> sortObject_Occurrence(ArrayList<Object> objects, SubjectProperty.API_Occurrence api){
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
    public static ArrayList<Object> sortObject_Co_Occurrence(ArrayList<Object> objects, SubjectProperty.API_Occurrence api){
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
    public static ArrayList<Object> sortObject_Importance(ArrayList<Object> objects){
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
     * This method sort the object list according to the rank coefficient.
     */
    public static ArrayList<Object> sortObject_Rank_Coeff(ArrayList<Object> objects, SubjectProperty.API_Occurrence api){
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

}
