package com.hanibalg.yeneservice.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

public class LocationsModel {

    private DocumentReference user;
    private GeoPoint location;
    private Timestamp timestamp;

    public LocationsModel(){}

    public LocationsModel(DocumentReference user, GeoPoint location, Timestamp timestamp) {
        this.user = user;
        this.location = location;
        this.timestamp = timestamp;
    }

    public DocumentReference getUser() {
        return user;
    }
    public GeoPoint getLocation() {
        return location;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
