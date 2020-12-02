package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

public class LocationsModel {

    private DocumentReference user;
    private GeoPoint providerLocation;
    private Timestamp timestamp;

    public LocationsModel(){}

    public LocationsModel(DocumentReference user, GeoPoint providerLocation, Timestamp timestamp) {
        this.user = user;
        this.providerLocation = providerLocation;
        this.timestamp = timestamp;
    }

    public DocumentReference getUser() {
        return user;
    }

    public GeoPoint getProviderLocation() {
        return providerLocation;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
