package com.hanibalg.yeneservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ViewPageAdapter;
import com.hanibalg.yeneservice.pages.MyBarCodeFragment;
import com.hanibalg.yeneservice.pages.ScanBarCodeFragment;

public class QRProviderCodeActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;
    FirebaseAuth auth;
    String User_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrprovider_code);
        auth = FirebaseAuth.getInstance();
        User_id = auth.getCurrentUser().getUid();
        reference = FirebaseFirestore.getInstance().collection("Users").document(User_id);
        //init variable
        ViewPager viewPager = findViewById(R.id.qr_viewPage);
        TabLayout tabLayout = findViewById(R.id.qr_tab);

        final MyBarCodeFragment myFrag = new MyBarCodeFragment();
        final ScanBarCodeFragment jobNot = new ScanBarCodeFragment();

        final ViewPageAdapter myadapter = new ViewPageAdapter(getSupportFragmentManager());
        myadapter.addFragment(jobNot,"My BAR Code");
        myadapter.addFragment(myFrag,"Scan QR Code");
//        reference.get().addOnSuccessListener(documentSnapshot -> {
//            if(documentSnapshot.exists()){
//                boolean isProvider = documentSnapshot.getBoolean("isProvider");
//                if(isProvider){
//                    tabLayout.setVisibility(View.VISIBLE);
//                    viewPager.setVisibility(View.VISIBLE);
//                    myadapter.addFragment(jobNot,"My BAR Code");
//                    myadapter.addFragment(myFrag,"Scan QR Code");
//                } else {
//                    tabLayout.setVisibility(View.GONE);
//                    viewPager.setVisibility(View.GONE);
//                    myadapter.addFragment(myFrag,"Scan QR Code");
////                    loadFragment(new ScanBarCodeFragment());
//                }
//                // setup adapter
//
//            }
//        });
        viewPager.setAdapter(myadapter);
        tabLayout.setupWithViewPager(viewPager);
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
