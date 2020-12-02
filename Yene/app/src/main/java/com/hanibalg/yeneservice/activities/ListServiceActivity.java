package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ServiceListAdaptor;
import com.hanibalg.yeneservice.models.ServiceListModel;

import java.util.ArrayList;
import java.util.List;

public class ListServiceActivity extends AppCompatActivity {

    private List<ServiceListModel> serviceListModels;
    private ServiceListAdaptor adaptor;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_service);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        firebaseFirestore = FirebaseFirestore.getInstance();

        //init variables
        RecyclerView lists = findViewById(R.id.all_service_recycler);
        LinearLayout noLayout = findViewById(R.id.noServices);
        LottieAnimationView lottieAnimationView  = findViewById(R.id.animation_view_list);

        lists.setHasFixedSize(true);
        serviceListModels = new ArrayList<>();

        //get Intent file
        String cata = getIntent().getStringExtra("categoryName").toLowerCase();
        toolbar.setTitle(cata + " services");
        firebaseFirestore.collection("Services_List")
                .whereEqualTo("category",cata)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (DocumentChange doc: task.getResult().getDocumentChanges()){
                            if (!doc.getDocument().getData().isEmpty()){
                                noLayout.setVisibility(View.GONE);
                                lottieAnimationView.setVisibility(View.GONE);
                                ServiceListModel listModel = doc.getDocument().toObject(ServiceListModel.class);
                                serviceListModels.add(listModel);
                                adaptor.notifyDataSetChanged();
                            }else{
                                noLayout.setVisibility(View.VISIBLE);
                                lottieAnimationView.setVisibility(View.VISIBLE);
                            }

                        }
                    }else {
                        noLayout.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                    }
                })
        .addOnFailureListener(e -> {
            noLayout.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.VISIBLE);
        });
        adaptor = new ServiceListAdaptor(this,serviceListModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        lists.setLayoutManager(mLayoutManager);
        lists.setLayoutManager(new GridLayoutManager(this,3));
        lists.setAdapter(adaptor);

    }
}
