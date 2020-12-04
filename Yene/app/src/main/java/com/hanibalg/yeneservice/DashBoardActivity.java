package com.hanibalg.yeneservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.Users.LoginActivity;
import com.hanibalg.yeneservice.Users.MyProfileActivity;
import com.hanibalg.yeneservice.activities.JobLIstActivity;
import com.hanibalg.yeneservice.activities.NotificationActivity;
import com.hanibalg.yeneservice.activities.ProviderUsersJobListActivity;
import com.hanibalg.yeneservice.activities.ReportProblemActivity;
import com.hanibalg.yeneservice.activities.SearchActivity;
import com.hanibalg.yeneservice.models.UserModel;
import com.hanibalg.yeneservice.pages.AddPostFragment;
import com.hanibalg.yeneservice.pages.AppointedJobFragment;
import com.hanibalg.yeneservice.pages.BookmarkFragment;
import com.hanibalg.yeneservice.pages.HomeFragment;
import com.hsalf.smileyrating.SmileyRating;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private int numVisibleChildren = 4;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarLayout appBarLayout;
    private CardView cardView;
    FirebaseFirestore firebaseFirestore;
    private Toolbar toolbar;
    private TextView searchInputText;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = DashBoardActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        //check theme
        if(loadState() == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppTheme);
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }

        checkInternate();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        appBarLayout =  findViewById(R.id.appbar);
        cardView = findViewById(R.id.searchLayout);
        searchInputText = findViewById(R.id.searchableText);
        searchInputText.setOnClickListener(v->{
            Intent searchActivity = new Intent(this,SearchActivity.class);
            startActivity(searchActivity);
        });
        drawerLayout = findViewById(R.id.drawler);
        navigationView = findViewById(R.id.nav_sidebar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openDrawerNav,
                R.string.closeDrawerNav
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        View header = navigationView.getHeaderView(0);
        MaterialFavoriteButton floatingActionButton = header.findViewById(R.id.darkTheme);
//        floatingActionButton.setOnClickListener(this);
        floatingActionButton.setOnFavoriteChangeListener((buttonView, favorite) -> {
            if(buttonView.isFavorite()){
                Toast.makeText(this, "black", Toast.LENGTH_SHORT).show();
                themeUtils.changeToTheme(this, themeUtils.BLACK);
//                    themeUtils.changeToTheme(DashBoardActivity.this,themeUtils.BLACK);
            }else {
                Toast.makeText(this, "light", Toast.LENGTH_SHORT).show();
                themeUtils.changeToTheme(this, themeUtils.BLUE);
//                    themeUtils.changeToTheme(DashBoardActivity.this,themeUtils.BLUE);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        //Bottom navigation
        if(mAuth.getUid() == null){
            Intent l = new Intent(this,LoginActivity.class);
            startActivity(l);
        }else{
            try {
                firebaseFirestore.collection("Users").document(mAuth.getUid()).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        ImageView o = findViewById(R.id.profileImg);
                        TextView nm = findViewById(R.id.pname);
                        TextView as = findViewById(R.id.add);
                        UserModel t = task.getResult().toObject(UserModel.class);
                        if(t != null) {
                            Picasso.get().load(t.getImage()).placeholder(R.drawable.placeholder_profile).into(o);
                            nm.setText(t.getFirstName());
                            as.setText(t.getEmail());
                        }
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        MaterialButton buttonLog = findViewById(R.id.btnLogout);
        buttonLog.setOnClickListener(v -> {
            Toast.makeText(this, "logout btn clicked", Toast.LENGTH_SHORT).show();
        });

        //bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*
        @load Home fragment
        */
        loadFragment(new HomeFragment());
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    cardView.setVisibility(View.VISIBLE);
                    loadFragment(fragment);
                    return true;
                case R.id.job:
                    fragment = new AppointedJobFragment();
                    cardView.setVisibility(View.GONE);
                    appBarLayout.getLayoutParams().height = AppBarLayout.LayoutParams.WRAP_CONTENT;
                    loadFragment(fragment);
                    return true;
                case R.id.addPost:
                    fragment = new AddPostFragment();
                    cardView.setVisibility(View.GONE);
                    appBarLayout.getLayoutParams().height = AppBarLayout.LayoutParams.WRAP_CONTENT;
                    loadFragment(fragment);
                    return true;
                case R.id.favourite:
                    //toolbar.setTitle("Search");
                    fragment = new BookmarkFragment();
                    cardView.setVisibility(View.GONE);
                    appBarLayout.getLayoutParams().height = AppBarLayout.LayoutParams.WRAP_CONTENT;
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
        appBarLayout.setExpanded(false,true);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout){
            signOut();
        }
        if(id == R.id.notification){
            Intent se = new Intent(this, NotificationActivity.class);
            startActivity(se);
        }
        if(id == R.id.report){
            Intent see = new Intent(this, JobLIstActivity.class);
            startActivity(see);
        }
        if(id == R.id.msg){
            Intent se = new Intent(this, ProviderUsersJobListActivity.class);
            startActivity(se);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkInternate(){
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            boolean isWiFi = nInfo.getType() == ConnectivityManager.TYPE_WIFI;

            return connected;
        }catch (Exception e){
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;

    }
    //sign out method
    private void signOut() {
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> {
//                        updateUI(null);
                });
        Intent lg = new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        lg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        lg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lg);
    }
    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();
        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                task -> {
//                        updateUI(null);
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.one){
            Intent st = new Intent(DashBoardActivity.this, MyProfileActivity.class);
            startActivity(st);
        }
        if(id == R.id.drawer_report){
            Intent rt = new Intent(DashBoardActivity.this, ReportProblemActivity.class);
            startActivity(rt);
        }
        if(id == R.id.rateUs){
            Toast.makeText(this, "rate clicked", Toast.LENGTH_SHORT).show();
            AppRate();
            drawerLayout.closeDrawers();
        }
        if(id == R.id.theme){
            item.setActionView(R.layout.theme_switch);
            final Switch themeSwitch = (Switch) item.getActionView().findViewById(R.id.action_switch);
            if(loadState() == true){
                themeSwitch.setChecked(true);
            }
            themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveState(true);
                    recreate();
                } else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveState(false);
//                        recreate();
                }
            });

        }
//        if (id == R.id.darkTheme){
//            MaterialFavoriteButton floatingActionButton = findViewById(R.id.darkTheme);
//            floatingActionButton.setOnFavoriteChangeListener((buttonView, favorite) -> {
//                if(buttonView.isFavorite()){
//                    Toast.makeText(this, "black", Toast.LENGTH_SHORT).show();
//                    themeUtils.changeToTheme(this, themeUtils.BLUE);
////                    themeUtils.changeToTheme(DashBoardActivity.this,themeUtils.BLACK);
//                }else {
//                    Toast.makeText(this, "light", Toast.LENGTH_SHORT).show();
//                    themeUtils.changeToTheme(this, themeUtils.BLACK);
////                    themeUtils.changeToTheme(DashBoardActivity.this,themeUtils.BLUE);
//                }
//            });
//        }

        return false;
    }

    private boolean loadState() {
        SharedPreferences sharedPreferences = getSharedPreferences("ABHPostive",MODE_PRIVATE);
        boolean state = sharedPreferences.getBoolean("NightMode",false);

        return state;
    }
    private void saveState(Boolean state){
        SharedPreferences sharedPreferences = getSharedPreferences("ABHPostive",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode",state);
        editor.apply();
    }

    private void AppRate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Get layout
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rate_application,null);
        builder.setView(dialogView);
        SmileyRating smileyRating = dialogView.findViewById(R.id.smile_rating);
        smileyRating.setSmileySelectedListener(type -> {
            if(SmileyRating.Type.GREAT == type){
                Log.d(TAG,"Wow, the use give high rating");
            }
            //get rating Value between 1-5
            int rating = type.getRating();
            saveRate(rating);
        });
                // Add action busmile_ratingttons
//                .setPositiveButton("Skip", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
////                            LoginDialogFragment.this.getDialog().cancel();
//                    }
//                });
        builder.create();
        builder.show();
    }

    private void saveRate(float rateValue){
        Map<String,Object> rateInfo = new HashMap<>();
        rateInfo.put("ratedUser",mAuth.getUid());
        rateInfo.put("rating",rateValue);
        rateInfo.put("timestamp", Timestamp.now());
        firebaseFirestore.collection("RatingApp")
                .document(mAuth.getUid())
                .set(rateInfo)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(this, "Thanks for you replay", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Log.d(TAG,e.getMessage()));
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(checkInternate()){
//            toolbar.setTitle("connected");
        }else{
            toolbar.setTitle("connecting...");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.darkTheme){
            MaterialFavoriteButton floatingActionButton = findViewById(R.id.darkTheme);
            floatingActionButton.setOnFavoriteChangeListener((buttonView, favorite) -> {
                if(buttonView.isFavorite()){
                    Toast.makeText(this, "black", Toast.LENGTH_SHORT).show();
                    themeUtils.changeToTheme(this, themeUtils.BLUE);
//                    themeUtils.changeToTheme(DashBoardActivity.this,themeUtils.BLACK);
                }else {
                    Toast.makeText(this, "light", Toast.LENGTH_SHORT).show();
                    themeUtils.changeToTheme(this, themeUtils.BLACK);
//                    themeUtils.changeToTheme(DashBoardActivity.this,themeUtils.BLUE);
                }
            });
        }
    }
}
