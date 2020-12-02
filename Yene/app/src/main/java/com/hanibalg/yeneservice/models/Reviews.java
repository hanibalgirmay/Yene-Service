package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

public class Reviews {
    private String id;
    private String job_id;
    private String comments;
    private float rating;
    private Timestamp timestamp;

    public Reviews() {
    }

    public Reviews(String id, String job_id,float rating,String comments, Timestamp timestamp) {
        this.id = id;
        this.job_id = job_id;
        this.rating = rating;
        this.comments = comments;
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getJob_id() {
        return job_id;
    }

    public String getComments() {
        return comments;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public float getRating() {
        return rating;
    }
}
