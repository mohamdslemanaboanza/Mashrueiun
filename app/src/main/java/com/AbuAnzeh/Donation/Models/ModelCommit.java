package com.AbuAnzeh.Donation.Models;

public class ModelCommit
{
    private String id;
    private String commit;
    private String img;

    public ModelCommit(String id, String commit, String img) {
        this.id = id;
        this.commit = commit;
        this.img = img;
    }

    public ModelCommit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
