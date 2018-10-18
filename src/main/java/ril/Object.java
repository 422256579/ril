package ril;

import ril.data.ComplexData;
import ril.data.DoubleData;
import ril.data.IntegerData;

/**
 * This class saves the information of @object and @ranking-coefficient regarding @subject_ID and @property_ID.
 */
public class Object {

    String subject_ID;
    String object_label;
    String object_ID;
    String property_ID;

    // Below stand the parameters we need. We set them -1 as default.
    IntegerData importance = new IntegerData();
    ComplexData bing = new ComplexData();
    ComplexData wikipedia = new ComplexData();
    IntegerData countTriple = new IntegerData();
    DoubleData groundTruth = new DoubleData();

    // True, if this entity is an positive object for the given subject_ID and property_ID.
    // Otherwise, false.
    boolean positive_obj = false;


    /**
     *
     * @param subject_ID The subject_ID we use.
     * @param property_ID The property_ID we use.
     * @param object_ID  The object fixed by @subject_ID and @property_ID.
     */
    public Object(String subject_ID, String property_ID, String object_ID, String object_label) {
        this.subject_ID = subject_ID;
        this.object_ID = object_ID;
        this.property_ID = property_ID;
        this.object_label = object_label;
    }

    public String getSubject_ID() {
        return subject_ID;
    }

    public String getProperty_ID() {
        return property_ID;
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

    public void setCountTriple(int count_triple) {
        this.countTriple.setValue(count_triple);
    }

    public int getCountTriple() {
        return this.countTriple.getValue();
    }

    public void setRank_CountTriple(int rank_countTriple){
        this.countTriple.setRank(rank_countTriple);
    }

    public int getRank_CountTriple(){ return this.countTriple.getRank();}

    public double getGroundTruth() {
        return this.groundTruth.getValue();
    }

    public void setGroundTruth(double groundTruth) {
        this.groundTruth.setValue(groundTruth);
    }

    public int getRank_GroundTruth(){
        return this.groundTruth.getRank();
    }

    public void setRank_GroundTruth(int rank_groundTruth){
        this.groundTruth.setRank(rank_groundTruth);
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
  We define two objects are equal if and only if the @subject_ID, @property_ID and @object all are same.
     */
    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Object object1 = (Object) o;
        return object1.subject_ID.equals(this.subject_ID) && object1.property_ID.equals(this.property_ID) && object1.object_ID.equals(this.object_ID);
    }

}
