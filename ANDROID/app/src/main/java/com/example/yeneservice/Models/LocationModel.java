package com.example.yeneservice.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.GeoPoint;

public class LocationModel {

    @DocumentId
    private String LocationId;
    private GeoPoint location;
    private Timestamp timestamp;

    public LocationModel() {
    }

    public LocationModel(String LocationId, GeoPoint location, Timestamp timestamp) {
        this.LocationId = LocationId;
        this.location = location;
        this.timestamp = timestamp;
    }

    public String getLocationId() {
        return LocationId;
    }

    public void setLocationId(String locationId) {
        LocationId = locationId;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
