package com.example.yeneservice.Models;

public class Service {

    private String title;
    private String img;
    private String serviceID;

    public Service(String title_name, String img_holder) {
        title = title_name;
        img = img_holder;
    }

    public String getServiceID() {
        return serviceID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
