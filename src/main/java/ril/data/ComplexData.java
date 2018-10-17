package ril.data;

public class ComplexData {

    double value = -1.0;
    int rank = -1;
    int occurrence = -1;
    int co_occurrence = -1;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

    public int getCo_occurrence() {
        return co_occurrence;
    }

    public void setCo_occurrence(int co_occurrence) {
        this.co_occurrence = co_occurrence;
    }
}
