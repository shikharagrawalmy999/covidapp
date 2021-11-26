package com.example.covidapp;

import java.io.Serializable;
import java.util.Date;

public class Token implements Serializable {
    private String MAC_ADDRESS;
    private String LATITUDE;
    private String LONGITUDE;
    private String DATE;

    public Token(){

    }
    public String getMAC_ADDRESS() {
        return MAC_ADDRESS;
    }

    public void setMAC_ADDRESS(String MAC_ADDRESS) {
        this.MAC_ADDRESS = MAC_ADDRESS;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public Token(String MAC_ADDRESS, String LATITUDE, String LONGITUDE, String DATE) {
        this.MAC_ADDRESS = MAC_ADDRESS;
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;
        this.DATE = DATE;
    }
}
