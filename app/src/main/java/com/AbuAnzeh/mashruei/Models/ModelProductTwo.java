package com.AbuAnzeh.mashruei.Models;

import java.util.ArrayList;

public class ModelProductTwo {
    private String nameDonation;
    private String detailsDonation;


    public ModelProductTwo() {

    }

    public ModelProductTwo(String nameDonation, String detailsDonation) {
        this.nameDonation = nameDonation;
        this.detailsDonation = detailsDonation;
    }

    public String getNameDonation() {
        return nameDonation;
    }

    public void setNameDonation(String nameDonation) {
        this.nameDonation = nameDonation;
    }

    public String getDetailsDonation() {
        return detailsDonation;
    }

    public void setDetailsDonation(String detailsDonation) {
        this.detailsDonation = detailsDonation;
    }

}
