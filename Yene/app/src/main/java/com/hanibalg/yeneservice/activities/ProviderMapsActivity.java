package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.GPSTracker;
import com.hanibalg.yeneservice.PermissionUtils;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.OnMapProviderListsAdaptor;
import com.hanibalg.yeneservice.models.LocationsModel;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProviderMapsActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private DocumentReference reference;
    private GoogleMap mMap;
    private static final String TAG = ProviderMapsActivity.class.getSimpleName();
    private OnMapProviderListsAdaptor adaptor;
    private List<ProviderModel> models;
    private List<UserModel> mUser;
    private RecyclerView recyclerView;
    private String gen;
    private LocationManager locationManager;
    GPSTracker gpsTracker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private Location currentLocation;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_maps);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase initialize instance
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
//        reference = FirebaseFirestore.getInstance().collection("Locations").document();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
//        gpsTracker = new GPSTracker(this);
//        if (!gpsTracker.getIsGPSTrackingEnabled()){
//            gpsTracker.showSettingsAlert();
//            gpsTracker.stopUsingGPS();
//        }else{
//            gpsTracker.updateGPSCoordinates();
//        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //init variable
        recyclerView = findViewById(R.id.recyclerMap);
        recyclerView.setHasFixedSize(true);

        models = new ArrayList<>();
        mUser = new ArrayList<>();
        adaptor = new OnMapProviderListsAdaptor(this,models,mUser);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        //---------------------
        getProviders();
        //---------------------
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View v = snapHelper.findSnapView(mLayoutManager);
                int pos = mLayoutManager.getPosition(v);
                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(pos);
                CardView linearLayout = holder.itemView.findViewById(R.id.card_provider);

                if(newState == RecyclerView.SCROLL_INDICATOR_LEFT){
                    linearLayout.animate().setDuration(350).scaleX(0.8f).scaleY(0.8f).setInterpolator(new AccelerateInterpolator()).start();
                } else if(newState == RecyclerView.SCROLL_INDICATOR_RIGHT){
                    linearLayout.animate().setDuration(350).scaleX(0.8f).scaleY(0.8f).setInterpolator(new AccelerateInterpolator()).start();
                } else {
                    linearLayout.animate().setDuration(350).scaleX(1).scaleY(1).setInterpolator(new AccelerateInterpolator()).start();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //---------------------------------------================================

    }

    private void fetchLocation() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if(location != null){
                currentLocation = location;
//                Toast.makeText(gpsTracker, ""+currentLocation.getLatitude()+""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        (dialog, id) -> {
                            enableMyLocation();
//                                Intent callGPSSettingIntent = new Intent(
//                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                startActivity(callGPSSettingIntent);
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
//            return;
//        }
//        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            // Enable the my location layer if the permission has been granted.
//            enableMyLocation();
//        } else {
//            // Permission was denied. Display an error message
//            // Display the missing permission error dialog when the fragments resume.
//            permissionDenied = true;
//        }
//        switch (requestCode){
//            case REQUEST_CODE:
//                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Enable the my location layer if the permission has been granted.
//                    fetchLocation();
//                }
//                break;
//        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
//        if (!gpsTracker.getIsGPSTrackingEnabled()) {
//            // Permission was not granted, display error dialog.
//            showMissingPermissionError();
//            permissionDenied = false;
//        }
    }

    public void getProviders(){
        firebaseFirestore.collection("Locations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.isEmpty()){
                        Log.d(TAG,"data not found");
                    }
                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                        final LocationsModel locationModel = doc.getDocument().toObject(LocationsModel.class);
                        DocumentReference ref = locationModel.getUser();
                        ref.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                final ProviderModel providerModel = task.getResult().toObject(ProviderModel.class);
                                String user_id = task.getResult().getId();
//                                        userModel.setUserID(user_id);
                                Log.d("map-pro", String.valueOf(task.getResult().getData()));
//                                        getProviderInfo(userModel.getUserID());
                                DocumentReference userRef = providerModel.getUserInfo();
                                userRef.get().addOnSuccessListener(document -> {
                                    if(document.exists()){
                                        UserModel info = document.toObject(UserModel.class);
                                        String id = document.getId();
                                        info.setUserId(id);
                                        Log.d("USer-info",document.getData().toString());
                                        models.add(providerModel);
                                        mUser.add(info);
                                        adaptor.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(e -> Toast.makeText(ProviderMapsActivity.this, "Firebase error location", Toast.LENGTH_SHORT).show());
    }

    private void getProviderInfo(String userId) {
        recyclerView.setPadding(0,0,0,10);
        Log.d("Provider-mmmm",userId);
        firebaseFirestore.collection("Service_Providers")
                .whereEqualTo("userID",userId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                        String docID = doc.getDocument().getId();
                        ProviderModel providerModel= doc.getDocument().toObject(ProviderModel.class);
                        providerModel.setUser_id(docID);

                        Log.d("user-provider",providerModel.toString());
                        models.add(providerModel);
                        adaptor.notifyDataSetChanged();
                    }
                });

    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
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
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
//        LatLngBounds ethiopia_lat = new LatLngBounds(new LatLng(9.1450,40.4897))
//        mMap.setLatLngBoundsForCameraTarget(ethiopia_lat,ethiopia_longt);
//        zoomingLocation(9.0707804,38.7323016);
        /**
         * my location coordinate
         */

//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la, lng), 10));
//            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//                mMap.setMyLocationEnabled(true);
//                mMap.setOnMyLocationButtonClickListener(this);
//                mMap.setOnMyLocationClickListener(this);
//            }

        //get all locations from firebase
        firebaseFirestore.collection("Locations").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                        LocationsModel l = doc.getDocument().toObject(LocationsModel.class);
                        //get latitudes and longitudes
                        final double latitude = l.getProviderLocation().getLatitude();
                        final double longitude = l.getProviderLocation().getLongitude();
                        final LatLng ET = new LatLng(latitude, longitude);
                        l.getUser().get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                ProviderModel m = task.getResult().toObject(ProviderModel.class);

                                // Add a marker in Sydney and move the camera
                                MarkerOptions markerOptions = new MarkerOptions();
                                assert m != null;
                                markerOptions.title(m.getAbout_me());
                                markerOptions.position(ET);
//                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder_icon));
                                markerOptions.snippet("provider is here");
                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ET, 15));
                                // // Zoom in, animating the camera.
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//                                        mMap.setMinZoomPreference(5.13f);
//                                        mMap.setMaxZoomPreference(30.0f);
                                mMap.setOnMarkerClickListener(this);
                            }
                        });
                    }
                });
        //current location
        if(currentLocation != null){
            LatLng MylatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(MylatLng)
                    .radius(200)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE));
            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setIntValues(0, 100);
            valueAnimator.setDuration(3000);
            valueAnimator.setEvaluator(new IntEvaluator());
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(valueAnimator1 -> {
                float animatedFraction = valueAnimator1.getAnimatedFraction();
                circle.setRadius(animatedFraction * 100 * 2);
            });
            valueAnimator.start();
            MarkerOptions myLocation = new MarkerOptions().position(MylatLng).title("my location");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(MylatLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MylatLng,5));
            googleMap.addMarker(myLocation);
        }

        //end firebase
//        enableMyLocation();
    }
    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
    private CameraUpdate zoomingLocation(Double lat, Double lng) {
//        Intent intent = getIntent();
//        Double b = intent.getExtras().getDouble("lon");
//        Double a = intent.getExtras().getDouble("la");
        return CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 43);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "My Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.i(TAG, "onActivityResult: GPS Enabled by user");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.i(TAG, "onActivityResult: User rejected GPS request");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        String clickCount =  marker.getId();
        // Check if a click count was set, then display the click count.

        Toast.makeText(this,
                marker.getTitle() +
                        " has been clicked " + clickCount + " times.",
                Toast.LENGTH_SHORT).show();
        return false;
    }
}
