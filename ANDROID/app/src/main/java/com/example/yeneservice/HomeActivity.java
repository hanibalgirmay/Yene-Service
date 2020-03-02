package com.example.yeneservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.yeneservice.Extra.AppointedUsersActivity;
import com.example.yeneservice.Extra.MessageActivity;
import com.example.yeneservice.PagesFragment.AppointementFragment;
import com.example.yeneservice.PagesFragment.HomeFragment;
import com.example.yeneservice.PagesFragment.SearchFragment;
import com.example.yeneservice.Users.PhoneAuthenticateActivity;
import com.example.yeneservice.Users.RegisterInformationActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    private ActionBar toolbar;
    private FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);

//        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
//                    // Collapsed (make button visible and fab invisible)
//                } else if (verticalOffset == 0) {
//                    // Expanded (make fab visible and toolbar button invisible)
//                } else {
//                    // Somewhere in between
//                }
//            }
//        });

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigation());

        loadFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.menu_appointments:
//                    toolbar.setTitle("Appointment");
                    fragment = new AppointementFragment();
                    loadFragment(fragment);
                    return true;
//                case R.id.menu_geosearch:
//                    toolbar.setTitle("Geo Search");
//                    fragment = new LocationSearch();
//                    loadFragment(fragment);
//                    return true;
                case R.id.menu_home:
//                    toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.menu_navigation_profile:
//                    toolbar.setTitle("Profile");
//                    fragment = new Profile();
//                    loadFragment(fragment);
                    return true;
                case R.id.menu_search:
//                    toolbar.setTitle("Search");
                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(HomeActivity.this, MessageActivity.class));
            return true;
        }
        if (id == R.id.action_msg) {
            startActivity(new Intent(HomeActivity.this, AppointedUsersActivity.class));
            return true;
        }
        if(id == R.id.action_notification){
            SearchView view = (SearchView) MenuItemCompat.getActionView(item);
            view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
//                    searchDatat(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        if (id == R.id.action_notification) {
//            Intent n = new Intent(HomeActivity.this, NotificationActivity.class);
//            n.putExtra("userID", auth.getUid());
//            startActivity(n);
            return true;
        }
        else if (id == R.id.action_about){
//            startActivity(new Intent(HomeActivity.this, AboutActivity.class));
            Toast.makeText(this, "this is about button menu", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.action_logout){
            signOut();
        }
//        else if(id == R.id.action_prov){
//            startActivity(new Intent(HomeActivity.this, ProviderListActivity.class));
//            Toast.makeText(this, "this is about button menu", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
    //sign out method
    public void signOut() {
        auth.signOut();
        Intent lg = new Intent(this, RegisterInformationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lg);
        finish();
    }
}
