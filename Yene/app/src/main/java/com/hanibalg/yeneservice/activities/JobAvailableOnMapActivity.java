package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.JobListPublic;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

public class JobAvailableOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private static final String TAG = JobAvailableOnMapActivity.class.getSimpleName();
    private FirebaseFirestore firebaseFirestore;
    private BottomSheetBehavior bottomSheetBehavior;
    private CardView cardBottomsheet;
    private ImageView pro;
    private TextView n,job;
    private RatingBar rate;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_available_on_map);
        //mapJobs
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapJobs);
        mapFragment.getMapAsync(this);

        //imt
        firebaseFirestore = FirebaseFirestore.getInstance();
        cardBottomsheet = findViewById(R.id.bottom_sheet_user);
        bottomSheetBehavior = BottomSheetBehavior.from(cardBottomsheet);
        pro = cardBottomsheet.findViewById(R.id.provider_img);
        n = cardBottomsheet.findViewById(R.id.provider_name);
        job = cardBottomsheet.findViewById(R.id.provider_specialize);
        btn = cardBottomsheet.findViewById(R.id.appt);
        rate = cardBottomsheet.findViewById(R.id.providerRating);
        initBottomSheet();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(9.0707804, 38.7323016), 10));
        boolean gps_enabled = false;
        map.setMyLocationEnabled(false);
        map.setIndoorEnabled(false);
        //-=========================
        firebaseFirestore.collection("Jobs")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                        JobListPublic jobListPublic = doc.getDocument().toObject(JobListPublic.class);
                        Double lng = jobListPublic.getLocation().getLongitude();
                        Double lat = jobListPublic.getLocation().getLatitude();

                        final LatLng ET = new LatLng(lat, lng);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(ET);
                        markerOptions.snippet(jobListPublic.getProblem_desc());
                        markerOptions.title(jobListPublic.getServiceName());
                        map.addMarker(markerOptions);
                        map.setOnMarkerClickListener(marker -> {
                            initBottomSheet();
                            Toast.makeText(this, "marker clicked", Toast.LENGTH_SHORT).show();
                            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                getUser(jobListPublic);
//                                btn_bottom_sheet.setText("Close sheet");
                            } else {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                                btn_bottom_sheet.setText("Expand sheet");
                            }
                            return false;
                        });

                    }
                });

    }
    
    private void getUser(JobListPublic id){
        job.setText(id.getServiceName());
        rate.setRating(4.1f);
        String userId = id.getUserID().trim();
        if(userId.length() != 0){
            firebaseFirestore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()){
                            UserModel u = documentSnapshot.toObject(UserModel.class);
                            Toast.makeText(this, "get user", Toast.LENGTH_SHORT).show();
                            btn.setOnClickListener(v -> Toast.makeText(JobAvailableOnMapActivity.this, "clicked", Toast.LENGTH_SHORT).show());
                            Picasso.get().load(u.getImage()).placeholder(R.drawable.placeholder_profile).into(pro);
                            n.setText(u.getFirstName());
                            //test

                        }
                    });
        }else{
            Picasso.get().load("").placeholder(R.drawable.placeholder_profile).into(pro);
            n.setText("user Unknown");
            rate.setRating(2.5f);
        }

                
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                cardBottomsheet.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        return super.dispatchTouchEvent(event);
    }
    private void initBottomSheet() {
        // get the bottom sheet view
        // init the bottom sheet behavior
        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // this part hides the button immediately and waits bottom sheet
                // to collapse to show
//                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
//                    fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
//                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
//                    fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
//                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });
//        final FloatingActionButton floatingActionButton;
//        floatingActionButton = findViewById(R.id.fab);
//        floatingActionButton.setOnClickListener(v -> {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            floatingActionButton.isOrWillBeHidden();
//        });
    }
}
