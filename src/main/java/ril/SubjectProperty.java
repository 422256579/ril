package ril;

import ril.facotry.Sort_Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * We achieve Subject-property_ID-pairs using this interface.
 * The parameters of the generator are @subject_ID(String), @property_ID(String), which are the IDs in the wikidata. e.g. "Q17714" refers to the subject_ID for "Stephen Hawking", and "P166" refers to the property_ID for "awards received".
 * Using the methods grabObjects() and grabObjects(String filePath) we can retrieve the all negative objects for the fixed subject_ID and property_ID in WIKIDATA and a local file, respectively.
 * Using the method grabOccurrence() we retrieve the occurrence of each object in the object-list.
 * Using the method grabCo_Occurrence() we retrieve the co-occurrence of subject_ID and each object in the object-list.
 * Using computeCo_Occurrence_Coefficient() we calculate the rank coefficient which is defined by the co-occurrence divided by occurrence.
 * Using sortObject(Object.Parameter parameter) we can sort the list according the @parameter: "NUM_SUB_OBJ" for co-occurrence , "NUM_OBJ" for occurrence, "CO_OCCURRENCE_COEFFICIENT" for rank coefficient,"IMPORTANCE" for inherent importance. We sort the object-list by rank coefficient as default.
 * The method calculateAll(String filePath, Object.Parameter parameter) is a method to do all the  jobs above and get the final results.
 */

public interface SubjectProperty {

    public enum API_Occurrence{Wikipedia, Bing}

    /**
     * @return the ID of subject_ID.
     */
    public String getSubject_id();

    /**
     * @return the label of subject_ID.
     */
    public String getSubject_label();
    /**
     * @return the ID of property_ID.
     */
    public String getProperty_id();

    /**
     * Retrieve the list of all negative facts with the fixed subject_ID and property_ID in Wikidata.
     */
    public void grabObjects();

    /**
     * Retrieve the number of occurrence for each object in the retrieved object-list on Bing.
     */
    public void grabOccurrence(API_Occurrence api);


    public String getProperty_label();

    /**
     * Grab the number of co-occurrence for subject_ID and each object in the retrieved object-list.
     */
    public void grabCo_Occurrence(API_Occurrence api);

    /**
     * Calculate the ranking-coefficients by num_subj_obj / num_obj.
     */
    public void computeCo_Occurrence_Coefficient(API_Occurrence api);

    /**
    * @return the list of objects.
    */
    public ArrayList<Object> getObjects();

    public void setObjects(ArrayList<Object> objects);

    /**
     * @return the list of negative objects;
     */

    public List<Object> getNegativeObjects();


    public ArrayList<Object> sortObject(List<Object> objects, Sort_Factory.Parameter parameter);

    public void sortObject(Sort_Factory.Parameter parameter);

    public double computeNDCG(Sort_Factory.Parameter parameter);

    public double getIDCG();

    public void grabNumSuperclass();

    public void grabNumSubclass();

    public void grabDistance();

    public void grabNumSubject();

    public void grabNumSubjectProperty();

        /**
         * If file == null, then run this.grabObjects(), this.grabOccurrence(), this.grabCo_Occurrence() and this.computeCo_Occurrence_Coefficient() to calculate all parameters we need.
         * Otherwise, run this.grabObjects(file), this.grabOccurrence(), this.grabCo_Occurrence() and this.calRank_coeff() to calculate all parameters we need.
         * Finally, we sort the list according parameter
         * @param filePath the file path for objects.
         * @param parameter the list sorted by @parameter.
         */
    public void calculateAll(String filePath, Sort_Factory.Parameter parameter, API_Occurrence api);
}
