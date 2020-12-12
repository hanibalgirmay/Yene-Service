package com.hanibalg.yeneservice.patterns;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.models.IDIdentification;
import com.hanibalg.yeneservice.patterns.interfaces.OnDataAdded;

public class IDRepository {
    private static IDRepository instance;
    private IDIdentification idIdentification;
    private static final String TAG = IDRepository.class.getName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DocumentReference ref = db.collection("ID_Info").document(auth.getUid());
    private static OnDataAdded onDataAdded;

    public static IDRepository getInstance(Context context){
        if(instance == null){
            instance = new IDRepository();
        }
        onDataAdded = (OnDataAdded) context;
        return instance;
    }

    public MutableLiveData<IDIdentification> getIDInfo(){
        loadID();
        MutableLiveData<IDIdentification> data = new MutableLiveData<>();
        data.setValue(idIdentification);
        return data;
    }

    private void loadID() {
        ref.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot.exists()){
                if (!documentSnapshot.getData().isEmpty()){
                    idIdentification = documentSnapshot.toObject(IDIdentification.class);

                    onDataAdded.added();
                }
            }
        });
    }

}
