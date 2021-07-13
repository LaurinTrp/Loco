package com.example.firsttest.Excel;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class ExcelObject implements Serializable {

    private static final long serialVersionUID = -1892561327013038124L;

    private int plz;
    private String matchCode, companyName, location, interentAddress, time, additional;
    private String[] articles;
    private boolean bio;
    private transient LatLng latLng;
    private double lat, lng;

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String[] getArticles() {
        return articles;
    }

    public void setArticles(String[] articles) {
        this.articles = articles;
    }

    public boolean isBio() {
        return bio;
    }

    public void setBio(boolean bio) {
        this.bio = bio;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
        if(latLng != null){
            this.lat = latLng.latitude;
            this.lng = latLng.longitude;
        }
    }

    public String getInterentAddress() {
        return interentAddress;
    }

    public void setInterentAddress(String interentAddress) {
        this.interentAddress = interentAddress;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getAdditional() {
        return additional;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getString(){
        return "PLZ: " + getPlz() + ", Matchcode: " + getMatchCode() + ", Company: " + getCompanyName() + ", Location: " + getLocation() + ", Bio: " + (isBio() ? "yes" : "no");
    }

}
