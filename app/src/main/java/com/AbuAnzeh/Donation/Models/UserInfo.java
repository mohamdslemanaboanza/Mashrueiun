package com.AbuAnzeh.Donation.Models;

public class UserInfo
{
    private String id;
   private String city;
   private String phoneNumber;
   private String imgUri;
   private String pass;
   private String email;
   private String name;



   public UserInfo(){}


    public UserInfo(String id,String city, String phoneNumber, String imgUri, String pass, String email, String name) {
        this.id = id;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.imgUri = imgUri;
        this.pass = pass;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserName() {
        return email;
    }

    public void setUserName(String userName) {
        this.email = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
