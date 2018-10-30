package ril;

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
    IntegerData occurrence_Bing = new IntegerData();
    IntegerData co_Occurrence_Bing = new IntegerData();
    DoubleData co_Occurrence_Coefficient_Bing = new DoubleData();
    IntegerData occurrence_Wikipedia = new IntegerData();
    IntegerData co_Occurrence_Wikipedia = new IntegerData();
    DoubleData co_Occurrence_Coefficient_Wikipedia = new DoubleData();
    IntegerData countFacts = new IntegerData();
    DoubleData groundTruth = new DoubleData();
    IntegerData numSuperclass = new IntegerData();
    IntegerData numSubclass = new IntegerData();
    IntegerData distance = new IntegerData();
    IntegerData numSubject = new IntegerData();
    IntegerData numSubjectProperty = new IntegerData();


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

    // importance Part
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

    // occurrence Part
    public void setOccurrence(int occurrence, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing) {
            this.occurrence_Bing.setValue(occurrence);
        }else{
            this.occurrence_Wikipedia.setValue(occurrence);
        }
    }

    public int getOccurrence(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.occurrence_Bing.getValue();
        }else{
            return this.occurrence_Wikipedia.getValue();
        }
    }

    public void setRank_Occurrence(int rank_Occurrence, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing) {
            this.occurrence_Bing.setRank(rank_Occurrence);
        }else{
            this.occurrence_Wikipedia.setRank(rank_Occurrence);
        }
    }

    public int getRank_Occurrence(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.occurrence_Bing.getRank();
        }else{
            return this.occurrence_Wikipedia.getRank();
        }
    }

    // co-occurrence part
    public void setCo_Occurrence(int co_occurrence, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            this.co_Occurrence_Bing.setValue(co_occurrence);
        }else{
            this.co_Occurrence_Wikipedia.setValue(co_occurrence);
        }
    }

    public int getCo_Occurrence(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.co_Occurrence_Bing.getValue();
        }else{
            return this.co_Occurrence_Wikipedia.getValue();
        }
    }

    public void setRank_Co_Occurrence(int rank_Co_Occurrence, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing) {
            this.co_Occurrence_Bing.setRank(rank_Co_Occurrence);
        }else{
            this.co_Occurrence_Wikipedia.setRank(rank_Co_Occurrence);
        }
    }

    public int getRank_Co_Occurrence(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.co_Occurrence_Bing.getRank();
        }else{
            return this.co_Occurrence_Wikipedia.getRank();
        }
    }

    // co-occurrence coefficient part
    public void setCo_occur_coeff(double co_occur_coeff, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            this.co_Occurrence_Coefficient_Bing.setValue(co_occur_coeff);
        }else{
            this.co_Occurrence_Coefficient_Wikipedia.setValue(co_occur_coeff);
        }
    }

    public double getCo_occur_coeff(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.co_Occurrence_Coefficient_Bing.getValue();
        }else{
            return this.co_Occurrence_Coefficient_Wikipedia.getValue();
        }
    }

    public void setRank_co_occur_coeff(int rank_co_occur_coeff, SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            this.co_Occurrence_Coefficient_Bing.setRank(rank_co_occur_coeff);
        }else{
            this.co_Occurrence_Coefficient_Wikipedia.setRank(rank_co_occur_coeff);
        }
    }

    public int getRank_co_occur_coeff(SubjectProperty.API_Occurrence api) {
        if(api == SubjectProperty.API_Occurrence.Bing){
            return this.co_Occurrence_Coefficient_Bing.getRank();
        }else{
            return this.co_Occurrence_Coefficient_Wikipedia.getRank();
        }
    }

    // countFacts part
    public void setCountFacts(int count_triple) {
        this.countFacts.setValue(count_triple);
    }

    public int getCountFacts() {
        return this.countFacts.getValue();
    }

    public void setRank_CountFacts(int rank_countFacts){
        this.countFacts.setRank(rank_countFacts);
    }

    public int getRank_CountFacts(){ return this.countFacts.getRank();}

    // groundTruth part
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

    // isPositive part
    public boolean isPositive_obj() {
        return positive_obj;
    }

    public void setPositive_obj(boolean positive_obj) {
        this.positive_obj = positive_obj;
    }

    // numSuperclass part
    public void setNumSuperclass(int numSuperclass){
        this.numSuperclass.setValue(numSuperclass);
    }

    public int getNumSuperclass(){ return this.numSuperclass.getValue();}

    public void setRank_NumSuperclass(int rank_numSuperclass){ this.numSuperclass.setRank(rank_numSuperclass);}

    public int getRank_NumSuperclass(){ return this.numSuperclass.getRank();}

    // numSubclass part
    public void setNumSubclass(int numSubclass){
        this.numSubclass.setValue(numSubclass);
    }

    public int getNumSubclass(){ return this.numSubclass.getValue();}

    public void setRank_NumSubclass(int rank_numSubclass){ this.numSubclass.setRank(rank_numSubclass);}

    public int getRank_NumSubclass(){ return this.numSubclass.getRank();}

    // distance part
    public void setDistance(int distance){
        this.distance.setValue(distance);
    }

    public int getDistance(){ return this.distance.getValue();}

    public void setRank_Distance(int rank_distance){ this.distance.setRank(rank_distance);}

    public int getRank_Distance(){ return this.distance.getRank();}

    // numSubject part
    public void setNumSubject(int numSubject){
        this.numSubject.setValue(numSubject);
    }

    public int getNumSubject(){ return this.numSubject.getValue();}

    public void setRank_NumSubject(int rank_numSubject){ this.numSubject.setRank(rank_numSubject);}

    public int getRank_NumSubject(){ return this.numSubject.getRank();}

    // numSubjectProperty part
    public void setNumSubjectProperty(int numSubjectProperty){
        this.numSubjectProperty.setValue(numSubjectProperty);
    }

    public int getNumSubjectProperty(){ return this.numSubjectProperty.getValue();}

    public void setRank_NumSubjectProperty(int rank_numSubjectProperty){ this.numSubjectProperty.setRank(rank_numSubjectProperty);}

    public int getRank_NumSubjectProperty(){ return this.numSubjectProperty.getRank();}



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
