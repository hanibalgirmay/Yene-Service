package com.hanibalg.yeneservice.patterns;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.patterns.interfaces.OnDataAdded;

import java.util.ArrayList;
import java.util.List;

public class ServiceListRepoitory {
    private static ServiceListRepoitory instance;
    private ArrayList<ServiceListModel> serviceListModel = new ArrayList<>();
    private static final String TAG = ServiceListRepoitory.class.getName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static OnDataAdded onDataAdded;

    //Services_List

    public static ServiceListRepoitory getInstance(Context context){
        if(instance == null){
            instance = new ServiceListRepoitory();
        }
//        onDataAdded = (OnDataAdded) context;
        return instance;
    }

    public MutableLiveData<ArrayList<ServiceListModel>> getServicesList(){
        loadServices();
        MutableLiveData<ArrayList<ServiceListModel>> data = new MutableLiveData<>();
        data.setValue(serviceListModel);
        return data;
    }

    private void loadServices() {
        db.collection("Services_List")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot:list){
                            serviceListModel.add(documentSnapshot.toObject(ServiceListModel.class));
                        }

//                            onDataAdded.added();
                    }
                }).addOnFailureListener(e -> Log.d(TAG,"onFailure"+e.getMessage()));
    }
}
