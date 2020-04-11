package com.AbuAnzeh.Donation.Models;

public class News
{
    private String commit;

    public News(String commit) {
        this.commit = commit;
    }

    public News() {

    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }
}
