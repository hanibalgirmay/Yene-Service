package com.example.yeneservice.Models;

import com.google.firebase.firestore.GeoPoint;

public class ServicesProvider {
    private String documentId,userID, firstName,lastName,middleName,address,city,working_area,about_me;
    private Float rating;
    private Number expriance, age, phone_number;
    private String profile_img;
    private GeoPoint longt,lat;

    public ServicesProvider() {}

    public ServicesProvider(String documentId,String userID, String firstName, String lastName, String image , String address,
                            String working_area, String about,GeoPoint longt) {
        this.userID = userID;
        this.firstName = firstName;
        this.documentId= documentId;
        this.lastName = lastName;
        this.profile_img = image;
        this.address = address;
//        this.city = city;
        this.working_area = working_area;
//        this.expriance = expriance;
//        this.age = age;
        this.about_me = about;
        this.longt = longt;
//        this.lat = lat;
    }

    public ServicesProvider(String documentId,String userID, String firstName, String lastName, String working_area, String about_me, String profile_img, Float rating) {
        this.documentId = documentId;
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.working_area = working_area;
        this.about_me = about_me;
        this.profile_img = profile_img;
        this.rating = rating;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDocumentId() {
        return documentId;
    }

    public GeoPoint getLongt() {
        return longt;
    }

    public void setLongt(GeoPoint longt) {
        this.longt = longt;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAbout() {
        return about_me;
    }

    public void setAbout(String about) {
        this.about_me = about;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWorking_area() {
        return working_area;
    }

    public void setWorking_area(String working_area) {
        this.working_area = working_area;
    }

    public Number getExpriance() {
        return expriance;
    }

    public void setExpriance(Number expriance) {
        this.expriance = expriance;
    }

    public Number getAge() {
        return age;
    }

    public void setAge(Number age) {
        this.age = age;
    }

    public Number getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Number phone_number) {
        this.phone_number = phone_number;
    }
}
