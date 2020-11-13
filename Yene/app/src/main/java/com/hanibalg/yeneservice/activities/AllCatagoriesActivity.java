package com.hanibalg.yeneservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ServiceListAdaptor;
import com.hanibalg.yeneservice.adaptors.allAdaptor;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.servicesModel;

import java.util.ArrayList;
import java.util.List;

public class AllCatagoriesActivity extends AppCompatActivity {

    private List<servicesModel> mData;
    private allAdaptor myAdaptor;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_catagories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        //init
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.all_catagory_recyclerView);
        recyclerView.setHasFixedSize(true);
        mData = new ArrayList<>();
        firebaseFirestore.collection("Services")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DocumentChange doc: task.getResult().getDocumentChanges()){
                            servicesModel serviceListModel = doc.getDocument().toObject(servicesModel.class);
                            mData.add(serviceListModel);
                            myAdaptor.notifyDataSetChanged();
                        }
                    }
                });
        myAdaptor = new allAdaptor(this,mData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(myAdaptor);
    }
}
