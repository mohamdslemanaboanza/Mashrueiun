package com.AbuAnzeh.mashruei.Models;

public class ModelNotifications
{

    private String titleNotifications;
    private String details;

    public ModelNotifications(String titleNotifications, String details) {
        this.titleNotifications = titleNotifications;
        this.details = details;
    }

    public ModelNotifications() {

    }

    public String getTitleNotifications() {
        return titleNotifications;
    }

    public void setTitleNotifications(String titleNotifications) {
        this.titleNotifications = titleNotifications;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
