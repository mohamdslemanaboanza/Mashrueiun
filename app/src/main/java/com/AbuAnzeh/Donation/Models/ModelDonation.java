package com.AbuAnzeh.Donation.Models;

public class ModelDonation
{
    private String id;
    private String idPublisher;
    private String phone_number;
    private String namePublisher;
    private String imgPublisher;
    private String nameDonation;
    private String dateDonation;
    private String stateDonation;
    private String detailsDonation;
    private String image1;
    private String image2;
    private String image3;
    private String latitude;
    private String longitude;
    private String city;


    public ModelDonation(String id,String idPublisher,String phone_number,String namePublisher,String imgPublisher, String nameDonation, String dateDonation, String stateDonation, String detailsDonation, String image1, String image2, String image3, String latitude, String longitude, String city) {
        this.id = id;
        this.idPublisher = idPublisher;
        this.phone_number = phone_number;
        this.namePublisher = namePublisher;
        this.imgPublisher = imgPublisher;
        this.nameDonation = nameDonation;
        this.dateDonation = dateDonation;
        this.stateDonation = stateDonation;
        this.detailsDonation = detailsDonation;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }




    public ModelDonation() {

    }


    public String getNamePublisher() {
        return namePublisher;
    }

    public void setNamePublisher(String namePublisher) {
        this.namePublisher = namePublisher;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getIdPublisher() {
        return idPublisher;
    }

    public void setIdPublisher(String idPublisher) {
        this.idPublisher = idPublisher;
    }

    public String getImgPublisher() {
        return imgPublisher;
    }

    public void setImgPublisher(String imgPublisher) {
        this.imgPublisher = imgPublisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameDonation() {
        return nameDonation;
    }

    public void setNameDonation(String nameDonation) {
        this.nameDonation = nameDonation;
    }

    public String getDateDonation() {
        return dateDonation;
    }

    public void setDateDonation(String dateDonation) {
        this.dateDonation = dateDonation;
    }

    public String getStateDonation() {
        return stateDonation;
    }

    public void setStateDonation(String stateDonation) {
        this.stateDonation = stateDonation;
    }

    public String getDetailsDonation() {
        return detailsDonation;
    }

    public void setDetailsDonation(String detailsDonation) {
        this.detailsDonation = detailsDonation;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
