package com.hanibalg.yeneservice.models;

import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.List;

public class ProviderModel implements Serializable{
    private String user_id;
    private DocumentReference userInfo;
    private List<String> speciality;
    private String about_me;
    private String address;
    private String Gender;
    private int experience;
    private String phone_number;
    private DocumentReference locationId;
    private String type;
    private String city;
    private String educationLevel;
    private String certifiedCertficate;
    private List<String> showCase;

    public ProviderModel() {
    }

    public ProviderModel(String user_id, DocumentReference userInfo, List<String> speciality, String about_me, String address,
                         String gender, int experience, String phone_number, DocumentReference locationId, String type,
                         String city, String educationLevel, String certifiedCertificate, List<String> showCase) {
        this.user_id = user_id;
        this.userInfo = userInfo;
        this.speciality = speciality;
        this.about_me = about_me;
        this.address = address;
        this.Gender = gender;
        this.experience = experience;
        this.phone_number = phone_number;
        this.locationId = locationId;
        this.type = type;
        this.city = city;
        this.educationLevel = educationLevel;
        this.certifiedCertficate = certifiedCertificate;
        this.showCase = showCase;
    }

    public DocumentReference getUserInfo() {
        return userInfo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getSpeciality() {
        return speciality;
    }

    public String getAbout_me() {
        return about_me;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return Gender;
    }

    public int getExperience() {
        return experience;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public DocumentReference getLocationId() {
        return locationId;
    }

    public String getType() {
        return type;
    }

    public String getCity() {
        return city;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public String getCertifiedCertficate() {
        return certifiedCertficate;
    }

    public List<String> getShowCase() {
        return showCase;
    }
    /*
    SETTER
     */

    public void setSpeciality(List<String> speciality) {
        this.speciality = speciality;
    }

    public void setAboutMe(String about_me) {
        this.about_me = about_me;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public void setCertifiedCertficate(String certifiedCertficate) {
        this.certifiedCertficate = certifiedCertficate;
    }

    public void setShowCase(List<String> showCase) {
        this.showCase = showCase;
    }
}
