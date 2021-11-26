package com.example.covidapp;

public class RecyclerData {

    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String date;
    private String time;
    private String healthStatus;

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public RecyclerData(String city, String state, String country, String postalCode, String date, String time, String healthStatus) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.date = date;
        this.time = time;
        this.healthStatus = healthStatus;
    }
}

