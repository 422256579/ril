package ril;

import java.util.List;

/**
 * We achieve Subject-property-pairs using this interface.
 * The parameters of the generator are @subject(String), @property(String), which are the IDs in the wikidata. e.g. "Q17714" refers to the subject for "Stephen Hawking", and "P166" refers to the property for "awards received".
 * Using the methods grabObjects() and grabObjects(String filePath) we can retrieve the all negative objects for the fixed subject and property in WIKIDATA and a local file, respectively.
 * Using the method grabNum_objs() we retrieve the occurrence of each object in the object-list.
 * Using the method grabNum_subj_objs() we retrieve the co-occurrence of subject and each object in the object-list.
 * Using calcRank_coeff() we calculate the rank coefficient which is defined by the co-occurrence divided by occurrence.
 * Using sortObject(Object.Parameter parameter) we can sort the list according the @parameter: "NUM_SUB_OBJ" for co-occurrence , "NUM_OBJ" for occurrence, "RANK_COEFF" for rank coefficient,"IMPORTANCE" for inherent importance. We sort the object-list by rank coefficient as default.
 * The method calculateAll(String filePath, Object.Parameter parameter) is a method to do all the  jobs above and get the final results.
 */

public interface SubjectProperty {

    /**
     * @return the ID of subject.
     */
    public String getSubject();

    /**
     * @return the ID of property.
     */
    public String getProperty();

    /**
     * Retrieve the list of all negative facts with the fixed subject and property in Wikidata.
     */
    public void grabObjects();

    /**
     * Retrieve the number of occurrence for each object in the retrieved object-list on Bing.
     */
    public void grabNum_objs();

    /**
     * Read and save the objects and their inherent importance from our local file.
     * @param filePath the local file path.
     */
    public void grabObjects(String filePath);

    /**
     * Grab the number of co-occurrence for subject and each object in the retrieved object-list.
     */
    public void grabNum_subj_objs();

    /**
     * Calculate the ranking-coefficients by num_subj_obj / num_obj.
     */
    public void calcRank_coeff();

    /**
    * @return the list of objects.
    */
    public List<Object> getObjects();

    /**
     * @param parameter is "NUM_SUB_OBJ"  for co-occurrence of subject and object,
     *                   or "NUM_OBJ" for occurrence of object,
     *                   or "IMPORTANCE" for inherent importance,
     *                   or "RANK_COEFF" for the rank coefficient (Namely, num_sub_obj / sum_obj).
     * @return the decreasing sorted list according the parameter.
     */
    public void sortObject(Object.Parameter parameter);

    /**
     * If file == null, then run this.grabObjects(), this.grabNum_objs(), this.grabNum_subj_objs() and this.calcRank_coeff() to calculate all parameters we need.
     * Otherwise, run this.grabObjects(file), this.grabNum_objs(), this.grabNum_subj_objs() and this.calRank_coeff() to calculate all parameters we need.
     * Finally, we sort the list according parameter
     * @param filePath the file path for objects.
     * @param parameter the list sorted by @parameter.
     */
    public void calculateAll(String filePath, Object.Parameter parameter);
}
