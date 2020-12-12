package com.hanibalg.yeneservice.models;

public class IDIdentification {
    private String userId;
    private String IDCardNumber;
    private int Wereda;
    private int Kebele;
    private String IDExpireDate;
    private String SubCity;
    private String IDScannedImage;

    public IDIdentification() {
    }

    public IDIdentification(String userId, String IDCardNumber, int wereda, int kebele, String IDExpireDate, String subCity, String IDScannedImage) {
        this.userId = userId;
        this.IDCardNumber = IDCardNumber;
        Wereda = wereda;
        Kebele = kebele;
        this.IDExpireDate = IDExpireDate;
        SubCity = subCity;
        this.IDScannedImage = IDScannedImage;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIDCardNumber() {
        return IDCardNumber;
    }

    public int getWereda() {
        return Wereda;
    }

    public int getKebele() {
        return Kebele;
    }

    public String getIDExpireDate() {
        return IDExpireDate;
    }

    public String getSubCity() {
        return SubCity;
    }

    public String getIDScannedImage() {
        return IDScannedImage;
    }
}
