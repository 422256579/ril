package ril;

import ril.data.ComplexData;
import ril.data.SimpleData;

/**
 * This class saves the information of @object and @ranking-coefficient regarding @subject and @property.
 */
public class Object {

    /*Here the @subject and @property are the IDs on Wikidata, but @object is the name of the object.
     e.g. "Q17714" is the subject for "Stephen Hawking", "P166" is the property for "awards received" and "Nobel Price" is the subject for "Nobel Price".
     We use this setting because @subject and @property are in the higher hierarchy of our design and it will be convenient for us to retrieve some information on Wikidata.
     But @object is in the lowest hierarchy of our design and we don't need retrieve anything on Wikidata depending on it.
     */
    String subject;
    String object_label;
    String object_ID;
    String property;

    // Below stand the parameters we need. We set them -1 as default.
    SimpleData importance = new SimpleData();
    ComplexData bing = new ComplexData();
    ComplexData wikipedia = new ComplexData();
    SimpleData countTriple = new SimpleData();

    // True, if this entity is an positive object for the given subject and property.
    // Otherwise, false.
    boolean positive_obj = false;


    /**
     *
     * @param subject The subject we use.
     * @param property The property we use.
     * @param object_ID  The object fixed by @subject and @property.
     */
    public Object(String subject, String property, String object_ID, String object_label) {
        this.subject = subject;
        this.object_ID = object_ID;
        this.property = property;
        this.object_label = object_label;
    }

    public String getSubject() {
        return subject;
    }

    public String getProperty() {
        return property;
    }

    public String getObject_ID() {
        return object_ID;
    }

    public String getObject_label() {
        return object_label;
    }

    public void setImportance(int importance) {
        this.importance.setValue(importance);
    }

    public int getImportance() {
        return this.importance.getValue();
    }

    public void setRank_importance(int rank_importance) {
        this.importance.setRank(rank_importance);
    }

    public int getRank_importance() {
        return this.importance.getRank();
    }

    public void setOccurrence(int occurrence, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing) {
            this.bing.setOccurrence(occurrence);
        }else{
            this.wikipedia.setOccurrence(occurrence);
        }
    }

    public int getOccurrence(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.bing.getOccurrence();
        }else{
            return this.wikipedia.getOccurrence();
        }
    }

    public void setCo_Occurrence(int co_occurrence, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            this.bing.setCo_occurrence(co_occurrence);
        }else{
            this.wikipedia.setCo_occurrence(co_occurrence);
        }
    }

    public int getCo_Occurrence(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.bing.getCo_occurrence();
        }else{
            return this.wikipedia.getCo_occurrence();
        }
    }

    public void setCo_occur_coeff(double co_occur_coeff, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            this.bing.setValue(co_occur_coeff);
        }else{
            this.wikipedia.setValue(co_occur_coeff);
        }
    }

    public double getCo_occur_coeff(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.bing.getValue();
        }else{
            return this.wikipedia.getValue();
        }
    }

    public void setRank_co_occur_coeff(int rank_co_occur_coeff, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            this.bing.setRank(rank_co_occur_coeff);
        }else{
            this.wikipedia.setRank(rank_co_occur_coeff);
        }
    }

    public int getRank_co_occur_coeff(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.bing.getRank();
        }else{
            return this.wikipedia.getRank();
        }
    }

    public void setCount_triple(int count_triple) {
        this.countTriple.setValue(count_triple);
    }

    public int getCount_triple() {
        return this.countTriple.getValue();
    }

    public void incrementCount_triple(){
        this.countTriple.setValue(this.countTriple.getValue() + 1);
    }

    public boolean isPositive_obj() {
        return positive_obj;
    }

    public void setPositive_obj(boolean positive_obj) {
        this.positive_obj = positive_obj;
    }

    /*
  We define two objects are equal if and only if the @subject, @property and @object all are same.
     */
    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Object object1 = (Object) o;
        return object1.subject.equals(this.subject) && object1.property.equals(this.property) && object1.object_ID.equals(this.object_ID);
    }

}
