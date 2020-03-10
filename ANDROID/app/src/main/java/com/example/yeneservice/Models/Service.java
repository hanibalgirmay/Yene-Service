package com.example.yeneservice.Models;

public class Service {

    private String title;
    private String img;

    public Service(String title_name, String img_holder) {
        title = title_name;
        img = img_holder;
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
