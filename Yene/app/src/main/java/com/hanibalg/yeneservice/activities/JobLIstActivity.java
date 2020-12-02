package com.hanibalg.yeneservice.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.PublicJobListAdaptor;
import com.hanibalg.yeneservice.adaptors.ViewPageAdapter;
import com.hanibalg.yeneservice.models.JobListPublic;
import com.hanibalg.yeneservice.pages.AvailableJobsFragment;
import com.hanibalg.yeneservice.pages.JobOfferForMeFragment;

import java.util.ArrayList;
import java.util.List;

public class JobLIstActivity extends AppCompatActivity {
    private FloatingActionButton mapFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Available Jobs");

//        mapFabButton = findViewById(R.id.float_map);

        //init variable
        ViewPager viewPager = findViewById(R.id.job_viewPage);
        TabLayout tabLayout = findViewById(R.id.job_tab);

        //initial fragment
        final JobOfferForMeFragment jobOfferForMeFragment = new JobOfferForMeFragment();
        final AvailableJobsFragment availableJobsFragment = new AvailableJobsFragment();
        final ViewPageAdapter myadapter = new ViewPageAdapter(getSupportFragmentManager());

        // setup adapter
        myadapter.addFragment(jobOfferForMeFragment,"Job For Me");
        myadapter.addFragment(availableJobsFragment,"Available Jobs");
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
