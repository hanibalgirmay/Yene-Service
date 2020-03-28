package com.example.yeneservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ServiceProviderProfileActivity extends AppCompatActivity implements OnMapReadyCallback{ //implements OnMapReadyCallback

    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView name,catag,bio,t,da,appoint_desc;
    ImageView ver,profile;
    private BottomSheetBehavior bottomSheetBehavior;
    Button c,btn_date,btn_tim,btn_submit;
    FirebaseFirestore firebaseFirestore;
    String user;
    FirebaseAuth auth;
    Spinner spinner;

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

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser().getUid();

        initComponent();

        name = findViewById(R.id.fullName);
        catag = findViewById(R.id.category);
        bio = findViewById(R.id.about_me);
        ver = findViewById(R.id.verify);
        //bottomsheet
        appoint_desc = findViewById(R.id.des);
        da = findViewById(R.id.tx);
        t = findViewById(R.id.txx);
        btn_date = findViewById(R.id.dialog_bt_date);
        btn_tim = findViewById(R.id.dialog_bt_time);
        btn_submit = findViewById(R.id.appoint);
        spinner = findViewById(R.id.service_spinner);
        //end

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        btn_tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

//        appointment button submit
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appoint();
            }
        });


        c = findViewById(R.id.view_map);
        // Recieve data
        Intent intent = getIntent();
        final String docId = intent.getExtras().getString("documentID");
        final String fname = intent.getExtras().getString("firstName");
        final String lname = intent.getExtras().getString("lastname");
        final String work = intent.getExtras().getString("working_area");
        final String abt = intent.getExtras().getString("about_me");
        final String img = intent.getExtras().getString("img");
        final String uId = intent.getExtras().getString("userID");

        final Double lg = intent.getExtras().getDouble("long");
        final Double lat = intent.getExtras().getDouble("lat");


        Toast.makeText(this, "doc"+ docId, Toast.LENGTH_SHORT).show();
        //spinner data from firebase load
        firebaseFirestore.collection("Services_List/"+ docId+ "/services").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                final List<String> areas = new ArrayList<String>();
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    String o = doc.getDocument().getData().toString();
                    areas.add(o);
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(ServiceProviderProfileActivity.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(areasAdapter);
            }
        });
        //end spinner
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

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                t.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }

    private void appoint() {
        final String description = appoint_desc.getText().toString().trim();
        final String dateAppoint = da.getText().toString().trim();
        final String timeAppoint = t.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            appoint_desc.setError("Enter problem description!");
//            Toast.makeText(getApplicationContext(), "Enter problem description!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(description) ){
//            progressBar.setVisibility(View.VISIBLE);
//            user = auth.getCurrentUser().getUid();
            Intent intent = getIntent();
            String idd = intent.getExtras().getString("userID");
            String nm = intent.getExtras().getString("firstName");

            Map<String, Object> appointMap = new HashMap<>();
            appointMap.put("appointedID", user);
            appointMap.put("service_provider_id", idd);
            appointMap.put("description", description);
            appointMap.put("date", dateAppoint);
            appointMap.put("time", timeAppoint);
            appointMap.put("timestamp", FieldValue.serverTimestamp());
            appointMap.put("isAccept", false);

//            appointMap.put("createdAt", date.toString());
            final DocumentReference ref = firebaseFirestore.collection("Appointments").document();
            firebaseFirestore.collection("Appointments").document().set(appointMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        String myId = ref.getId();
//                        Toast.makeText(ServiceProviderInfoActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                        new SweetAlertDialog(ServiceProviderProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Good job!")
                                .setContentText("You Appointed Successfully!")
                                .show();

                        Intent mainIntent = new Intent(ServiceProviderProfileActivity.this, HomeActivity.class);
                        mainIntent.putExtra("doccumentId",myId);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ServiceProviderProfileActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                    }
//                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

            Map<String, Object> notficationMessage = new HashMap<>();
            notficationMessage.put("messages",description);
            notficationMessage.put("from", user);

            firebaseFirestore.collection("Users/" + idd + "/Notifications").add(notficationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(ServiceProviderProfileActivity.this, "notification sent", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                da.setText(dateText);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
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
