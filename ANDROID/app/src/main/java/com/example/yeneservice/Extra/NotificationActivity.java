package com.example.yeneservice.Extra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.yeneservice.Adapters.NotificationRecyclerViewAdapter;
import com.example.yeneservice.Models.NotificationModel;
import com.example.yeneservice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class NotificationActivity extends AppCompatActivity {
    private List<NotificationModel> notificationList;
    RecyclerView recyclerView;
    NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;
    FirebaseAuth auth;
    String User_id;
    List<NotificationModel> lstnot ;
    private static final String TAG = "NotificationActivity";
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        User_id = auth.getCurrentUser().getUid();

        Intent intent = getIntent();
        String id = intent.getExtras().getString("userID");

        lstnot = new ArrayList<>();
        notificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter(this,lstnot);
        RecyclerView myrv = findViewById(R.id.recycleView);
        myrv.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myrv.setLayoutManager(mLayoutManager);
        myrv.setAdapter(notificationRecyclerViewAdapter);

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String current_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Users").document(current_id).collection("Notifications").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    NotificationModel notification = doc.getDocument().toObject(NotificationModel.class);
                    lstnot.add(notification);
//                        String id = doc.getDocument().getString("userID");
//                        String firstename = doc.getDocument().getString("firstName");
//                        Log.d(TAG,"file name: "+ firstename);
//                        lstnot.add(new Notification(id,firstename));
                    notificationRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
