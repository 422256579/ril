package ril;

/**
 * This class saves the information of @object and @ranking-coefficient regarding @subject and @property.
 */
public class Object {

    public enum Parameter{
        NUM_SUB_OBJ, NUM_OBJ,RANK_COEFF,IMPORTANCE
    }

    String subject;
    String object;
    String property;
    double rank_coeff = -1.0;
    double importance = -1.0;
    int num_object = -1;
    int num_subj_object = -1;

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setImportance(double importance) {
        this.importance = importance;
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

    /**
     *
     * @param subject The subject we use.
     * @param property The property we use.
     * @param object  The object fixed by @subject and @property.
     * @param importance The inherent importance of @object. e.g. the population of each county, the viewers or the number of winner of each award.
     */
    public Object(String subject, String property, String object, double importance) {
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

    public double getImportance() {
        return importance;
    }

    public double getRank_coeff() {
        return rank_coeff;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Object object1 = (Object) o;
        return object1.subject.equals(this.subject) && object1.property.equals(this.property) && object1.object.equals(this.object);
    }

}
