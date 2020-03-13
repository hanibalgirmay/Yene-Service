package com.example.yeneservice.Models;

import java.util.Comparator;

public class AppointementModel  {

//    private String DocId;
    private String name;
    private int image;
    private String userId;
    private String serviceProviderId;
    private String description;
    private String date;
    private String time;
    private String DocId;
//    private String pay;

    public AppointementModel(){}
//String userId,String serviceProviderId
    public AppointementModel(String name, int image , String description, String date, String time, String DocId) {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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

    public static final Comparator<AppointementModel> BY_DATE_ASCENDINGS = new Comparator<AppointementModel>() {
        @Override
        public int compare(AppointementModel o1, AppointementModel o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    };

    public static final Comparator<AppointementModel> BY_DATE_DESCENDINGS = new Comparator<AppointementModel>() {
        @Override
        public int compare(AppointementModel o1, AppointementModel o2) {
            return o2.getDate().compareTo(o1.getDate());
        }
    };
}
