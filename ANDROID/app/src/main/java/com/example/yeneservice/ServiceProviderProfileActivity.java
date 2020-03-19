package com.example.yeneservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

public class ServiceProviderProfileActivity extends AppCompatActivity implements OnMapReadyCallback{ //implements OnMapReadyCallback

    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView name,catag,bio;
    ImageView ver,profile;
    private BottomSheetBehavior bottomSheetBehavior;
    Button c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initComponent();

        name = findViewById(R.id.fullName);
        catag = findViewById(R.id.category);
        bio = findViewById(R.id.about_me);
        ver = findViewById(R.id.verify);

        c = findViewById(R.id.view_map);
        // Recieve data
        Intent intent = getIntent();
        final String fname = intent.getExtras().getString("firstName");
        final String lname = intent.getExtras().getString("lastname");
        final String work = intent.getExtras().getString("working_area");
        final String abt = intent.getExtras().getString("about_me");
        final String img = intent.getExtras().getString("img");
        final String uId = intent.getExtras().getString("userID");

        final Double lg = intent.getExtras().getDouble("long");
        final Double lat = intent.getExtras().getDouble("lat");

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(ServiceProviderProfileActivity.this,MapsActivity.class);
                u.putExtra("la",lat);
                u.putExtra("lon",lg);
                u.putExtra("fName", fname);
                u.putExtra("lName", lname);
                u.putExtra("img", img);
                startActivity(u);
            }
        });

        name.setText(fname+ " "+ lname);
        catag.setText(work);
        bio.setText(abt);
//        tabLayout = findViewById(R.id.tanlayoutId);
//        viewPager = findViewById(R.id.viewpagerId);

//        SearchFragment frag1 = new SearchFragment();
//        AppointementFragment frag2 = new AppointementFragment();
//        ViewPageAdapter myadapter = new ViewPageAdapter(getSupportFragmentManager());

//        myadapter.addFragment(frag1, "Reviews");
//        myadapter.addFragment(frag2, "Information");

//        viewPager.setAdapter(myadapter);
//        tabLayout.setupWithViewPager(viewPager);
    }

    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottomsheet);

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        final FloatingActionButton floatingActionButton;
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                floatingActionButton.isOrWillBeHidden();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        mMap = googleMap;

        Intent intent = getIntent();
        final Double lg = intent.getExtras().getDouble("long");
        final Double lat = intent.getExtras().getDouble("lat");
//        final Double lt = intent.getExtras().getDouble("lat");

        //cusrom
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bmp = Bitmap.createBitmap(100, 100, conf);
//        Canvas canvas1 = new Canvas(bmp);

        // paint defines the text color, stroke width and size
//        Paint color = new Paint();
//        color.setTextSize(35);
//        color.setColor(Color.BLACK);

        // modify canvas
//        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.businessman_profile_cartoon_removebg), 0,0, color);
//        canvas1.drawText("Me Name!", 30, 40, color);

        // Add a marker in Sydney and move the camera
        LatLng eth = new LatLng(lat, lg);
        googleMap.addMarker(new MarkerOptions().position(eth)
                .title("Marker in AA,my place"));
        mMap.setMinZoomPreference(6.0f);
//        mMap.addMarker(new MarkerOptions().position(eth).title("Marker in Addis abeba,Ethiopia").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eth));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.provider_share,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.provider_share){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Provider full name";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "awesome job");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
