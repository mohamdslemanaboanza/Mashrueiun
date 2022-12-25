package com.AbuAnzeh.mashruei.Models;

public class ModelMostWanted
{
    private int img;
    private String name;
    private double lat;
    private double lon;
    private String phoneNumber;
    private String city;
    private String desk;
    private String url;

    public ModelMostWanted(int img, String name, double lat, double lon, String phoneNumber, String city, String desk, String url) {
        this.img = img;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.desk = desk;
        this.url = url;
    }

    public ModelMostWanted() {

    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
