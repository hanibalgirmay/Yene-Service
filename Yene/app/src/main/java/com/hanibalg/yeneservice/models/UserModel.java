package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String userID;
    private String firstName;
    private String lastName;
    private String status;
    private String email;
    private String image;
    private String phone;
    private String city;
    private boolean Online;
    private boolean Provider;
    private boolean receiveNews;

    public UserModel(){}

    public UserModel(String userID, String firstName, String lastName,String phone, String email, String image,String city,
                     boolean Online, boolean Provider, boolean receiveNews) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.city = city;
        this.Online = Online;
        this.Provider = Provider;
        this.receiveNews = receiveNews;
//        this.timestamp = timestamp;
    }

    public String getCity() {
        return city;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userId) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImageProfile(String image) {
        this.image = image;
    }

    public boolean getOnline() {
        return Online;
    }

    public void setOnline(boolean online) {
        Online = online;
    }

    public boolean getProvider() {
        return Provider;
    }

    public boolean getReceiveNews() {
        return receiveNews;
    }

    public void setReceiveNews(boolean receiveNews) {
        this.receiveNews = receiveNews;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}
