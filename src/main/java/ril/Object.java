package ril;

/**
 * This class saves the information of @object and @ranking-coefficient regarding @subject and @property.
 */
public class Object {

    /**
     * This is the enum for sorting the object list.
     * "NUM_SUB_OBJ" for co-occurrence .
     * "NUM_OBJ" for occurrence.
     * "RANK_COEFF" for rank coefficient.
     * "IMPORTANCE" for inherent importance.
     */
    public enum Parameter{
        NUM_SUB_OBJ, NUM_OBJ,RANK_COEFF,IMPORTANCE
    }

    /*Here the @subject and @property are the IDs on Wikidata, but @object is the name of the object.
     e.g. "Q17714" is the subject for "Stephen Hawking", "P166" is the property for "awards received" and "Nobel Price" is the subject for "Nobel Price".
     We use this setting because @subject and @property are in the higher hierarchy of our design and it will be convenient for us to retrieve some information on Wikidata.
     But @object is in the lowest hierarchy of our design and we don't need retrieve anything on Wikidata depending on it.
     */
    String subject;
    String object;
    String property;

    // Below stand the parameters we need. We set them -1 as default.
    double rank_coeff = -1.0;
    int importance = -1;
    int num_object = -1;
    int num_subj_object = -1;

    int rank_rank_coeff = -1;
    int rank_importance = -1;

    /**
     *
     * @param subject The subject we use.
     * @param property The property we use.
     * @param object  The object fixed by @subject and @property.
     * @param importance The inherent importance of @object. e.g. the population of each county, the viewers or the number of winner of each award.
     */
    public Object(String subject, String property, String object, int importance) {
        this.subject = subject;
        this.object = object;
        this.property = property;
        this.importance = importance;
    }

    public String getSubject() {
        return subject;
    }

    public String getObject() {
        return object;
    }

    public String getProperty() {
        return property;
    }

    public void setRank_coeff(double rank_coeff) {
        this.rank_coeff = rank_coeff;
    }

    public int getImportance() {
        return importance;
    }

    public double getRank_coeff() {
        return rank_coeff;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getRank_rank_coeff() {
        return rank_rank_coeff;
    }

    public void setRank_rank_coeff(int rank_rank_coeff) {
        this.rank_rank_coeff = rank_rank_coeff;
    }

    public int getRank_importance() {
        return rank_importance;
    }

    public void setRank_importance(int rank_importance) {
        this.rank_importance = rank_importance;
    }

    public int getNum_object() {
        return num_object;
    }

    public void setNum_object(int num_object) {
        this.num_object = num_object;
    }

    public int getNum_subj_object() {
        return num_subj_object;
    }

    public void setNum_subj_object(int num_subj_object) {
        this.num_subj_object = num_subj_object;
    }
    /*
    We define two objects are equal if and only if the @subject, @property and @object all are same.
     */
    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Object object1 = (Object) o;
        return object1.subject.equals(this.subject) && object1.property.equals(this.property) && object1.object.equals(this.object);
    }

}
