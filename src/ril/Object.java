package ril;

/**
 * This class saves the information of @object and @ranking-coefficient regarding @subject and @property.
 */
public class Object {

    String subject;
    String object;
    String property;
    double rank_coeff;
    double importance;

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
}
