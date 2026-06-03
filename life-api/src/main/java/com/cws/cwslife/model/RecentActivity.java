package com.cws.cwslife.model;

public class RecentActivity {

    private String message;
    private String time;
    private String createdBy;

    public RecentActivity(String message, String time, String createdBy) {
        this.message = message;
        this.time = time;
        this.createdBy = createdBy;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}