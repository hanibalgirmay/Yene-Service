package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ViewPageAdapter;
import com.hanibalg.yeneservice.models.UserModel;
import com.hanibalg.yeneservice.pages.MyBarCodeFragment;
import com.hanibalg.yeneservice.pages.ScanBarCodeFragment;

public class QRProviderCodeActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference reference;
    FirebaseAuth auth;
    String User_id;
    private boolean isProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrprovider_code);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        User_id = auth.getCurrentUser().getUid();
        reference = FirebaseFirestore.getInstance().collection("Users").document(User_id);
        //init variable
        ViewPager viewPager = findViewById(R.id.qr_viewPage);
        TabLayout tabLayout = findViewById(R.id.qr_tab);

        final MyBarCodeFragment myFrag = new MyBarCodeFragment();
        final ScanBarCodeFragment jobNot = new ScanBarCodeFragment();
        final ViewPageAdapter myadapter = new ViewPageAdapter(getSupportFragmentManager());

        reference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                isProvider = documentSnapshot.getBoolean("Provider");
                if(isProvider){
                    myadapter.addFragment(jobNot,"Scan QR Code");
                    myadapter.addFragment(myFrag,"My BAR Code");
                } else {
                    myadapter.addFragment(jobNot,"Scan QR Code");
                }
                myadapter.notifyDataSetChanged();
            }
        });
//        if(isProvider){
//            myadapter.addFragment(jobNot,"Scan QR Code");
//            myadapter.addFragment(myFrag,"My BAR Code");
//            myadapter.notifyDataSetChanged();
//        }else{
//            myadapter.addFragment(jobNot,"Scan QR Code");
//            myadapter.notifyDataSetChanged();
//        }
        viewPager.setAdapter(myadapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
