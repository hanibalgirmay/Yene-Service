package com.example.yeneservice.Models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class AppointementID {

    @Exclude
    public String AppointementID;

    public <T extends AppointementID> T withId(@NonNull final String id) {
        this.AppointementID = id;
        return (T) this;
    }
}
