package com.example.yeneservice.Extra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeneservice.Adapters.NotificationRecyclerViewAdapter;
import com.example.yeneservice.Adapters.SuggestionProviderMapUserAdapter;
import com.example.yeneservice.Models.LocationModel;
import com.example.yeneservice.Models.NotificationModel;
import com.example.yeneservice.Models.ServicesProvider;
import com.example.yeneservice.Models.UserModel;
import com.example.yeneservice.PermissionUtils;
import com.example.yeneservice.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProviderMapActivity  extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    DocumentReference reference;
    private GoogleMap mMap;
    private String TAG = "MyActivivty";
    private BottomSheetBehavior bottomSheetBehavior;
    private Intent intent;
    TextView name,locationName,Distance,add,add2;
    RecyclerView recyclerView;
    Button appoint;
    SuggestionProviderMapUserAdapter adapter;
    ImageView pro;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LocationManager locationManager;
    List<UserModel>lstnot ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_map);
        initComponent();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = FirebaseFirestore.getInstance().collection("Locations").document();

        //id
        name = findViewById(R.id.provider_name);
        locationName = findViewById(R.id.location_name);
        Distance = findViewById(R.id.distance_from_me);
        add = findViewById(R.id.address2);
        add2 = findViewById(R.id.address3);
        pro = findViewById(R.id.provider_profile);
        appoint = findViewById(R.id.appoint_map);
        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String y = null;
                appointProvider(y);
            }
        });
        //end bootomsheet
//        getUserDataSuggestion();
        //GPS
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }
    }

    private void getUserDataSuggestion() {

        lstnot = new ArrayList<>();
        adapter = new SuggestionProviderMapUserAdapter(this,lstnot);
        recyclerView = findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("Locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if(e != null){
//                    Log.d(TAG,"error");
//                }
                for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    final LocationModel c = doc.getDocument().toObject(LocationModel.class);
                    String Id = c.getLocationId();

                    reference.collection("Users").document(Id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if(documentSnapshot.exists()){
                                UserModel u = documentSnapshot.toObject(UserModel.class);
                                String image =documentSnapshot.getString("image");

                                lstnot.add(new UserModel(image));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }


    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
//            mLocationPermissionGranted = true;
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                enableMyLocation();
//                                Intent callGPSSettingIntent = new Intent(
//                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
//        LinearLayout llBottomSheet2 = (LinearLayout) findViewById(R.id.bottom_suggestion);

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
//        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet2);
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

        FloatingActionButton floatingActionButton;
        floatingActionButton = findViewById(R.id.fab_directions);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                try {
//                    mMap.animateCamera(zoomingLocation());
                } catch (Exception e) {
                }
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
//                llBottomSheet.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        boolean gps_enabled = false;
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        }

        firebaseFirestore.collection("Locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
//                if(!e){
//                    Log.d(TAG,"error");
//                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getDocument().getData() != null){
                        final LocationModel c = doc.getDocument().toObject(LocationModel.class);
                        final Double lat = c.getLocation().getLatitude();
                        final Double lng = c.getLocation().getLongitude();
                        final String iD = c.getLocationId();

                        mMap = googleMap;

                        // Add a marker in provider place, and move the camera.
                        final LatLng sydney = new LatLng(lat,lng);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("Service providers")).setTag(iD);
                        
                        //butn

                        //end
                        //open bottomsheet
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                String t = (String) marker.getTag();
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                try {
//                                    Toast.makeText(ProviderMapActivity.this, "id:"+t, Toast.LENGTH_SHORT).show();
                                    mMap.animateCamera(zoomingLocation(lat,lng));
                                    if (t != null) {
                                        loadUserData(t);
                                    }
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                } catch (Exception e) {
                                }
                                return true;
                            }
                        });
                    } else{

                    }
                }
            }
        });
    }

    private void loadUserData(String tt) {
//        providerRate = findViewById(R.id.provider_rate);
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        final String y = tt.trim();
        if(!tt.isEmpty()){
            Toast.makeText(this, ""+tt, Toast.LENGTH_SHORT).show();
            firebaseFirestore.collection("Users").document(y).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String image = documentSnapshot.getString("image");
                    String first_name = documentSnapshot.getString("firstName");
                    String last_name = documentSnapshot.getString("lastName");
                    String fullName = first_name + " "+ last_name;
                    Toast.makeText(ProviderMapActivity.this, ""+fullName, Toast.LENGTH_SHORT).show();
                    name.setText(fullName);
                    Picasso.get().load(image).placeholder(R.drawable.businessman_profile_cartoon_removebg).into(pro);

                    //appoint
//                    appoint = findViewById(R.id.appoint_map);
//                    appoint.setOnClickListener(ProviderMapActivity.this);
                    appointProvider(y);
                }
            });

        } else {
            Toast.makeText(this, "Error id didn't found", Toast.LENGTH_SHORT).show();
        }

    }

    private void appointProvider(String y) {
        String Id = y.trim();
        if(Id.isEmpty()){
            Toast.makeText(this, "provider id is null!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "provider Id:" +Id, Toast.LENGTH_SHORT).show();
        }
    }

    private CameraUpdate zoomingLocation(Double lat, Double lng) {
        Intent intent = getIntent();
        Double b = intent.getExtras().getDouble("lon");
        Double a = intent.getExtras().getDouble("la");
        return CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 43);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "My Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
