package com.example.covidapp;

public class QuestionFour {
    private boolean last10To14;
    private boolean last5To10;
    private boolean last0To5;
    private boolean none;

    public QuestionFour(){

    }

    public boolean isLast10To14() {
        return last10To14;
    }

    public void setLast10To14(boolean last10To14) {
        this.last10To14 = last10To14;
    }

    public boolean isLast5To10() {
        return last5To10;
    }

    public void setLast5To10(boolean last5To10) {
        this.last5To10 = last5To10;
    }

    public boolean isLast0To5() {
        return last0To5;
    }

    public void setLast0To5(boolean last0To5) {
        this.last0To5 = last0To5;
    }

    public boolean isNone() {
        return none;
    }

    public void setNone(boolean none) {
        this.none = none;
    }
}
