package ril;

import java.util.List;
import java.util.Map;

/**
 * We achieve Subject-property-pairs using this interface.
 * The parameters of the generator are @subject(String), @property(String).
 * Using @API_Wiki to grab the list of objects @objects(List<String>), and remove the correct facts.
 * Using @API_Google to grab the number of results @num_obj with input @objects, and the number of results @num_subj_obj with input @subject and @object, which will be saved in maps @num_subj_objs(Map<...>).
 * If we have a @filePath, we can also read the @objects from the file
 * Then calculate the coefficients @rank_coeff(Map<...>) by @num_subj_obj / @num_obj.
 */

public interface SubjectProperty {

    /**
     * @return the name of subject.
     */
    public String getSubject();

    /**
     * @return the name of property.
     */
    public String getProperty();

    /**
     * Grab the list of all negative facts with the fixed @subject and @property using @API_Wiki and SPARQL .
     * @return the list of objects.
     */
    public List<Object> grabObjects();

    /**
     * Grab the number of results for each @object in the list @objects using @API_Google.
     * @return the map for @objects and the corresponding number of results.
     */
    public Map<String, Integer> grabNum_objs();

    /**
     * Read and save the @objects and @numb_objs from our local file.
     * @param filePath the local file path.
     * @return the map for @objects and the corresponding number of results.
     */
    public List<Object> grabObjects(String filePath);

    /**
     * Grab the number of results for @subject and each @object in the list @objects using @API_Google
     * @return the map for @subject, @objects and the corresponding number of results.
     */
    public Map<String, Integer> grabNum_subj_objs();

    /**
     * Calculate the ranking-coefficients by @num_subj_obj / @num_obj.
     * @return the List according to the order.
     */
    public List<Object> calcRank_coeff();

    /**
    * @return the list of objects.
    */
    public List<Object> getObjects();

    /**
     * @return the map for @objects and the corresponding number of results.
     */
    public Map<String, Integer> getNum_objs();

    /**
     * @return the map for @subject, @objects and the corresponding number of results.
     */
    public Map<String, Integer> getNum_subj_objs();

    /**
     * @return the List according to the order.
     */
    public List<Object> getRank_coeff();

    /**
     * If file == null, then run this.grabObjects(), this.grabNum_objs(), this.grabNum_subj_objs() and this.calcRank_coeff() to calculate all parameters we need.
     * Otherwise, run this.grabObjects(file), this.grabNum_objs(), this.grabNum_subj_objs() and this.calRank_coeff() to calculate all parameters we need.
     * @param filePath the file path for objects.
     * @throws Exception
     */
    public void calculateAll(String filePath) throws Exception;
}
