package com.hanibalg.yeneservice.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ViewPageAdapter;
import com.hanibalg.yeneservice.models.UserModel;
import com.hanibalg.yeneservice.pages.AvailableJobsFragment;
import com.hanibalg.yeneservice.pages.JobOfferForMeFragment;

public class JobLIstActivity extends AppCompatActivity {
    private FloatingActionButton mapFabButton;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Available Jobs");
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

//        mapFabButton = findViewById(R.id.float_map);
        //init variable
        ViewPager viewPager = findViewById(R.id.job_viewPage);
        TabLayout tabLayout = findViewById(R.id.job_tab);

        //initial fragment
        final JobOfferForMeFragment jobOfferForMeFragment = new JobOfferForMeFragment();
        final AvailableJobsFragment availableJobsFragment = new AvailableJobsFragment();
        final ViewPageAdapter myadapter = new ViewPageAdapter(getSupportFragmentManager());

        try {
            firebaseFirestore.collection("Users")
                    .document(auth.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isComplete()){
                            UserModel userModel = task.getResult().toObject(UserModel.class);
                            if (userModel.getProvider()){
                                // setup adapter
                                myadapter.addFragment(jobOfferForMeFragment,"Job For Me");
                                myadapter.addFragment(availableJobsFragment,"Available Jobs");
                            } else{
                                // setup adapter
//                                myadapter.addFragment(jobOfferForMeFragment,"Job For Me");
                                myadapter.addFragment(availableJobsFragment,"Available Jobs");
                            }
                            myadapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(JobLIstActivity.this, "error", Toast.LENGTH_SHORT).show());

        } catch (Exception e){
            e.printStackTrace();
        }

        viewPager.setAdapter(myadapter);
        tabLayout.setupWithViewPager(viewPager);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position){
//                    case 0:
//                        mapFabButton.setVisibility(View.GONE);
//                        break;
//                    case 1:
//                        mapFabButton.setVisibility(View.VISIBLE);
//                        break;
//                    default:
//                        mapFabButton.setVisibility(View.INVISIBLE);
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }


}
