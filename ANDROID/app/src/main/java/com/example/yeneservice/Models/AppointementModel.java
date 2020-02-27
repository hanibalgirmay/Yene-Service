package com.example.yeneservice.Models;

public class AppointementModel  {

//    private String DocId;
    private String name;
    private String image;
    private String userId;
    private String serviceProviderId;
    private String description;
    private String date;
    private String time;
    private String DocId;
//    private String pay;

    public AppointementModel(){}

    public AppointementModel(String name, String image, String userId, String serviceProviderId, String description, String date, String time, String DocId) {
        this.name = name;
        this.image = image;
        this.userId = userId;
        this.serviceProviderId = serviceProviderId;
        this.description = description;
        this.date = date;
        this.time = time;
        this.DocId = DocId;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
