package com.hanibalg.yeneservice;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class YeneFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("Tag","Token referesher: "+ s);

//        sendRegistrationToServer(token);
    }

}
