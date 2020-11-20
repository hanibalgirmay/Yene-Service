package com.hanibalg.yeneservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ViewPageAdapter;
import com.hanibalg.yeneservice.pages.JobNotificationFragment;
import com.hanibalg.yeneservice.pages.MyNotificationFragment;

public class NotificationActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;
    FirebaseAuth auth;
    String User_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        auth = FirebaseAuth.getInstance();
        User_id = auth.getCurrentUser().getUid();
        reference = FirebaseFirestore.getInstance().collection("Users").document(User_id);

        //init variable
        //ViewPager viewPager = findViewById(R.id.notification_viewPage);
        //TabLayout tabLayout = findViewById(R.id.notification_tab);

        //final MyNotificationFragment myFrag = new MyNotificationFragment();
        //final JobNotificationFragment jobNot = new JobNotificationFragment();

        //final ViewPageAdapter myadapter = new ViewPageAdapter(getSupportFragmentManager());
        // setup adapter

//        reference.get().addOnSuccessListener(documentSnapshot -> {
//            if(documentSnapshot.exists()){
//                boolean isProvider = documentSnapshot.getBoolean("isProvider");
//                if(isProvider){
//                    tabLayout.setVisibility(View.VISIBLE);
//                    viewPager.setVisibility(View.VISIBLE);
//                    myadapter.addFragment(jobNot,"Received");
//                    myadapter.addFragment(myFrag,"Sent");
//                } else {
//                    tabLayout.setVisibility(View.GONE);
//                    viewPager.setVisibility(View.GONE);
//                    loadFragment(new MyNotificationFragment());
//                }
//
//            }
//        });
        //myadapter.addFragment(jobNot,"Received");
        //myadapter.addFragment(myFrag,"Sent");
        // setup adapter
        //viewPager.setAdapter(myadapter);
        //tabLayout.setupWithViewPager(viewPager);
    }
    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
