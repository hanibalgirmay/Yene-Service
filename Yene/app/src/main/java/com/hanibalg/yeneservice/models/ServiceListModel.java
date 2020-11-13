package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

public class ServiceListModel {

    private String serviceListId;
    private String serviceId;
    private String name;
    private String category;
    private String image; //will change
    private Timestamp timestamp;

    public ServiceListModel(){}

    public ServiceListModel(String serviceListId, String category, String name, String image) {
        this.serviceListId = serviceListId;
        this.category = category;
        this.name = name;
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getServiceListId() {
        return serviceListId;
    }

    public String getImage() {
        return image;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
