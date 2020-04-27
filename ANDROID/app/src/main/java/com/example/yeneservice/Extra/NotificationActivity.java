package com.example.yeneservice.Extra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.yeneservice.Adapters.NotificationRecyclerViewAdapter;
import com.example.yeneservice.Adapters.ViewPageAdapter;
import com.example.yeneservice.Models.NotificationModel;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    DocumentReference reference;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        reference = FirebaseFirestore.getInstance().collection("Users").document(User_id);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("userID");

        //pageviewer-----------------
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpagerId);

        final RequestedNotificationFragment frag1 = new RequestedNotificationFragment();
        final JobNotificationFragment frag2 = new JobNotificationFragment();

        final ViewPageAdapter myadapter = new ViewPageAdapter(getSupportFragmentManager());
        // add fragment
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                boolean isProvider = documentSnapshot.getBoolean("isProvider");

                if(isProvider){
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    myadapter.addFragment(frag1, "Sent");
                    myadapter.addFragment(frag2, "Receive");
                } else {
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    loadFragment(new RequestedNotificationFragment());
//                    myadapter.addFragment(frag1, "Sent");
                }
                // setup adapter
                viewPager.setAdapter(myadapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        //endpageviewer

//        lstnot = new ArrayList<>();
//        notificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter(this,lstnot);
//        RecyclerView myrv = findViewById(R.id.recycleView);
//        myrv.setHasFixedSize(true);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        myrv.setLayoutManager(mLayoutManager);
//        myrv.setAdapter(notificationRecyclerViewAdapter);
//
//        // Access a Cloud Firestore instance from your Activity
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String current_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        db.collection("Users").document(current_id).collection("Notifications").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if(e != null){
//                    Log.d(TAG,"Error: "+ e.getMessage());
//                }
//                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
//                    String id = doc.getDocument().getId();
//                    NotificationModel notification = doc.getDocument().toObject(NotificationModel.class);
//                    notification.setDocID(id);
//                    lstnot.add(notification);
//                    Log.d(TAG,"notfication: "+ notification.getDocID());
////                        lstnot.add(new Notification(id,firstename));
//                    notificationRecyclerViewAdapter.notifyDataSetChanged();
//                }
//            }
//        });
    }

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_not, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
