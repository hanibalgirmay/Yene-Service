package com.example.yeneservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.yeneservice.Models.ServicesProvider;
import com.example.yeneservice.PagesFragment.ServiceListProvidersActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private BottomSheetBehavior bottomSheetBehavior;
    private static final String TAG = MapsActivity.class.getSimpleName();
    TextView name,locationName,Distance,add,add2;
    ImageView pro;
    RatingBar providerRate;
    Geocoder geocoder;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            if (mMap != null) {
//                mMap.setMyLocationEnabled(true);
//            }
//        } else {
//            // Permission to access the location is missing. Show rationale and request permission
//            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true);
//        }

        initComponent();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        name = findViewById(R.id.provider_name);
        locationName = findViewById(R.id.location_name);
        Distance = findViewById(R.id.distance_from_me);
        add = findViewById(R.id.address2);
        add2 = findViewById(R.id.address3);
        pro = findViewById(R.id.provider_profile);
        providerRate = findViewById(R.id.provider_rate);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
//            return;
//        }
//
//        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            // Enable the my location layer if the permission has been granted.
//            enableMyLocation();
//        } else {
//            // Permission was denied. Display an error message
//            // ...
//        }
//    }
//
//    /**
//     * Enables the My Location layer if the fine location permission has been granted.
//     */
//    private void enableMyLocation() {
//        // [START maps_check_location_permission]
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            if (mMap != null) {
//                mMap.setMyLocationEnabled(true);
//            }
//        } else {
//            // Permission to access the location is missing. Show rationale and request permission
//            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
//                    Manifest.permission.ACCESS_FINE_LOCATION, true);
//        }
//        // [END maps_check_location_permission]
//    }

    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

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

        FloatingActionButton floatingActionButton;
        floatingActionButton = findViewById(R.id.fab_directions);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                try {
                    mMap.animateCamera(zoomingLocation());
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

    private void listProvider(final String z){

        //query
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Service_Providers").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        final GeoPoint l = doc.getDocument().getGeoPoint("location");
                        final String id = doc.getDocument().getString("userID");
                        final String work = doc.getDocument().getString("working_area");

                        if(z.equals(work)){
                            Log.d(TAG,"working name: "+ work);
                            db.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String image = documentSnapshot.getString("image");
                                    String firstename = documentSnapshot.getString("firstName");
                                    String lastename = documentSnapshot.getString("lastName");

//                                    lstBook.add(new ServicesProvider(id,firstename,lastename,image,add,work,me,l));
//                                    serviceProviderAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            new SweetAlertDialog(MapsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("We are really sorry?")
                                    .setContentText("There is no service provider at the time for now!")
                                    .setConfirmText("Ok!")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    }
                }
            }
        });
        //end query
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

//        mMap.setMyLocationEnabled(true);
//        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.setOnMyLocationClickListener(this);

        Intent intent = getIntent();
        Double b = intent.getExtras().getDouble("lon");
        Double a = intent.getExtras().getDouble("la");
        String fname = intent.getExtras().getString("fName");
        String lname = intent.getExtras().getString("lName");
        String img = intent.getExtras().getString("img");

        final String cat = intent.getExtras().getString("category");
        String fullName = fname +" "+ lname;

//        mMap = googleMap;
        mMap = Tools.configActivityMaps(googleMap);
        // Add a marker in Sydney and move the camera
        LatLng eth = new LatLng(a, b);

//        mMap.addMarker(new MarkerOptions().position(eth).title("Marker in Addis abeba,Ethiopia"));
        MarkerOptions markerOptions = new MarkerOptions().position(eth);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(zoomingLocation());
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(eth));
        String loca = markerOptions.getPosition().toString();
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(a, b, 2);

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String postalCode2 = addresses.get(0).getSubAdminArea();
            String knownName = addresses.get(1).getFeatureName(); // Only if available else return NULL
            locationName.setText(address);
            add.setText(knownName);
        } catch (IOException e) {
            e.printStackTrace();
        }


        name.setText(fullName);
        Picasso.get().load(img).into(pro);
        Distance.setText(loca);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                try {
                    mMap.animateCamera(zoomingLocation());
                } catch (Exception e) {
                }
                return true;
            }
        });
    }
    private CameraUpdate zoomingLocation() {
        Intent intent = getIntent();
        Double b = intent.getExtras().getDouble("lon");
        Double a = intent.getExtras().getDouble("la");
        return CameraUpdateFactory.newLatLngZoom(new LatLng(a,b), 43);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}
