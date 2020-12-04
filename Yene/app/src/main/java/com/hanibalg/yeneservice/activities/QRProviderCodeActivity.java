package com.hanibalg.yeneservice.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ViewPageAdapter;
import com.hanibalg.yeneservice.pages.MyBarCodeFragment;
import com.hanibalg.yeneservice.pages.ScanBarCodeFragment;

public class QRProviderCodeActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference reference;
    FirebaseAuth auth;
    String User_id;
    private boolean isProvider;
    private static final int CAMERA_PERMISSION_CODE=101;

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

        /*
        ---------------------------check camera permission----------------
         */
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,CAMERA_PERMISSION_CODE);
        //----------------------------------------------

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

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,new String[] { permission },requestCode);
        }
        else {
            Toast.makeText(this,"Permission already granted",Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Showing the toast message
                Toast.makeText(this,"Camera Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Camera Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
//        else if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getActivity(),"Storage Permission Granted",Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(getActivity(),"Storage Permission Denied",Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
