package com.AbuAnzeh.mashruei.Models;

public class UserModel
{
    private String fname;
    private String lname;
    private String phone;
    private String pass;
    private String city;
    private String imgUri;
    private String authKey;
    private String createAt;
    private String typeUser;

    public UserModel(String fname, String lname, String phone, String pass, String city, String imgUri,String authKey,String createAt,String typeUser) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.pass = pass;
        this.city = city;
        this.imgUri = imgUri;
        this.authKey = authKey;
        this.createAt = createAt;
        this.typeUser = typeUser;
    }

    public UserModel() {
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
