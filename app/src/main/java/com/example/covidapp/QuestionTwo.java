package com.example.covidapp;

public class QuestionTwo {
    private boolean lungDisease;
    private boolean asthma;
    private boolean diabetes;
    private boolean hypertension;
    private boolean kidneyDisorder;
    private boolean none;

    public QuestionTwo(){

    }

    public boolean isLungDisease() {
        return lungDisease;
    }

    public void setLungDisease(boolean lungDisease) {
        this.lungDisease = lungDisease;
    }

    public boolean isAsthma() {
        return asthma;
    }

    public void setAsthma(boolean asthma) {
        this.asthma = asthma;
    }

    public boolean isDiabetes() {
        return diabetes;
    }

    public void setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
    }

    public boolean isHypertension() {
        return hypertension;
    }

    public void setHypertension(boolean hypertension) {
        this.hypertension = hypertension;
    }

    public boolean isKidneyDisorder() {
        return kidneyDisorder;
    }

    public void setKidneyDisorder(boolean kidneyDisorder) {
        this.kidneyDisorder = kidneyDisorder;
    }

    public boolean isNone() {
        return none;
    }

    public void setNone(boolean none) {
        this.none = none;
    }
}
