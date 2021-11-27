package com.example.covidapp;

public class QuestionOne {
    private boolean fever;
    private boolean soreThroat;
    private boolean cough;
    private boolean difficultyInBreathing;
    private boolean bodyAche;
    private boolean smellTaste;
    private boolean pinkEyes;
    private boolean hearingImpairment;
    private boolean none;

    public QuestionOne(){

    }

    public boolean isDifficultyInBreathing() {
        return difficultyInBreathing;
    }

    public void setDifficultyInBreathing(boolean difficultyInBreathing) {
        this.difficultyInBreathing = difficultyInBreathing;
    }

    public boolean isBodyAche() {
        return bodyAche;
    }

    public void setBodyAche(boolean bodyAche) {
        this.bodyAche = bodyAche;
    }

    public boolean isSmellTaste() {
        return smellTaste;
    }

    public void setSmellTaste(boolean smellTaste) {
        this.smellTaste = smellTaste;
    }

    public boolean isPinkEyes() {
        return pinkEyes;
    }

    public void setPinkEyes(boolean pinkEyes) {
        this.pinkEyes = pinkEyes;
    }

    public boolean isHearingImpairment() {
        return hearingImpairment;
    }

    public void setHearingImpairment(boolean hearingImpairment) {
        this.hearingImpairment = hearingImpairment;
    }

    public boolean isNone() {
        return none;
    }

    public void setNone(boolean none) {
        this.none = none;
    }

    public boolean isFever() {
        return fever;
    }

    public void setFever(boolean fever) {
        this.fever = fever;
    }

    public boolean isSoreThroat() {
        return soreThroat;
    }

    public void setSoreThroat(boolean soreThroat) {
        this.soreThroat = soreThroat;
    }

    public boolean isCough() {
        return cough;
    }

    public void setCough(boolean cough) {
        this.cough = cough;
    }
}
