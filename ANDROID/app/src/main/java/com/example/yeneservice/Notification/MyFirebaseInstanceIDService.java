package com.example.yeneservice.Notification;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService {

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if(firebaseUser != null){
            updateToken(refreshedToken);
        }

    }

    private void updateToken(String refreshedToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Token").document();
        Token token = new Token(refreshedToken);
        firebaseFirestore.collection("Tokens").document(firebaseUser.getUid()).set(token);
    }
}
