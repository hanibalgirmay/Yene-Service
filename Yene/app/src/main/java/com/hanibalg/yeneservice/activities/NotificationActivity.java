package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.NotificationAdaptor;
import com.hanibalg.yeneservice.adaptors.OnMapProviderListsAdaptor;
import com.hanibalg.yeneservice.adaptors.ViewPageAdapter;
import com.hanibalg.yeneservice.models.NotificationModel;
import com.hanibalg.yeneservice.pages.JobNotificationFragment;
import com.hanibalg.yeneservice.pages.MyNotificationFragment;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;
    FirebaseAuth auth;
    String User_id;
    List<NotificationModel> notificationList;
    NotificationAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        User_id = auth.getCurrentUser().getUid();
        reference = FirebaseFirestore.getInstance().collection("Users").document(User_id);

        RecyclerView notificationLayer = findViewById(R.id.notification);
        LottieAnimationView lottieAnimationView = findViewById(R.id.noNotification);
        notificationLayer.setHasFixedSize(true);

        adaptor = new NotificationAdaptor(this,notificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        notificationLayer.setLayoutManager(mLayoutManager);
        notificationLayer.setAdapter(adaptor);
        if(getNofification().size()>0){
            getNofification();
        }else{
            notificationLayer.setVisibility(View.VISIBLE);
            notificationLayer.setVisibility(View.GONE);
        }
        //init variable
//        ViewPager viewPager = findViewById(R.id.notification_viewPage);
//        TabLayout tabLayout = findViewById(R.id.notification_tab);

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

    private List<NotificationModel> getNofification(){
        notificationList = new ArrayList<>();
        firebaseFirestore.collection("Users")
                .document(auth.getUid())
                .collection("Notifications")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (DocumentChange doc: task.getResult().getDocumentChanges()){
                            NotificationModel notificationModel = doc.getDocument().toObject(NotificationModel.class);
                            notificationList.add(notificationModel);
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(NotificationActivity.this, "error geting notification", Toast.LENGTH_SHORT).show());

        return notificationList;
    }
}
