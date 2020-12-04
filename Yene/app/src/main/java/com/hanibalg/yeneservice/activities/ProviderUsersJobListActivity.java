package com.hanibalg.yeneservice.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ProviderAppointmentUserAdapter;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProviderUsersJobListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ProviderModel> modelList;
    private List<UserModel> userModels;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private ProviderAppointmentUserAdapter appointmentUserAdapter;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_users_job_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //firebase initialize
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        lottieAnimationView = findViewById(R.id.noChat);

        //init var
        recyclerView = findViewById(R.id.providerRecyclerView);
        recyclerView.setHasFixedSize(true);
        modelList = new ArrayList<>();
        userModels = new ArrayList<>();
        appointmentUserAdapter = new ProviderAppointmentUserAdapter(this,modelList,userModels,false);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(appointmentUserAdapter);

        firebaseFirestore.collection("JobsRequests")
                .whereEqualTo("isAccepted",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(queryDocumentSnapshots.isEmpty()){
                            lottieAnimationView.setVisibility(View.VISIBLE);
                        }
                        for(final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                            final String id = doc.getDocument().getString("jobAppointedUserID");
                            final String serviceProviderId = doc.getDocument().getString("service_provider_id");

                            assert id != null;
                            if(Objects.equals(auth.getUid(), id)){
                                assert serviceProviderId != null;
                                firebaseFirestore.collection("Users")
                                        .document(serviceProviderId)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String id = documentSnapshot.getId();
                                                UserModel u = documentSnapshot.toObject(UserModel.class);
                                                u.setUserId(id);
//                                                Log.d("Request-user",documentSnapshot.getData().toString());
                                                getProviderInfo(u,id);
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void getProviderInfo(final UserModel uModel, String id) {
        String userID = uModel.getUserId();
        firebaseFirestore.collection("Service_Providers")
                .whereEqualTo("user_id",id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                            ProviderModel pr = doc.getDocument().toObject(ProviderModel.class);
                            Log.d("Request-user",doc.getDocument().getData().toString());
                            userModels.add(uModel);
                            modelList.add(pr);
                            appointmentUserAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }
}
