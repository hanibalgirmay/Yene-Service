package com.example.yeneservice.Models;

public class AppointemntUserModel {
    private String id,service_provider_id, firstName,lastName,email;
    private String expriance, phone_number, status;
    private int profile_img;

    public AppointemntUserModel(){}

//    public AppointemntUserModel( String firstName, String lastName, String email, String phone_number, String profile_img) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.phone_number = phone_number;
//        this.profile_img = profile_img;
//    }
    public AppointemntUserModel(String serviceProviderId, String firstName, String phone_number, int profile_img, String status, boolean b) {
        this.service_provider_id = serviceProviderId;
        this.firstName = firstName;
        this.phone_number = phone_number;
        this.profile_img = profile_img;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService_provider_id() {
        return service_provider_id;
    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
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

    public String getExpriance() {
        return expriance;
    }

    public void setExpriance(String expriance) {
        this.expriance = expriance;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(int profile_img) {
        this.profile_img = profile_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
