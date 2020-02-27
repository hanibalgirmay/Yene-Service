package com.example.yeneservice.Models;

public class Service {

    private String title;
    private int img;

    public Service(String title_name, int img_holder) {
        title = title_name;
        img = img_holder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
