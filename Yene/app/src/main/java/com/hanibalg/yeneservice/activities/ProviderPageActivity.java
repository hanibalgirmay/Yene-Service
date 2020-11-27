package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.RecentJobUserAdaptor;
import com.hanibalg.yeneservice.adaptors.ReviewAdapter;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.LocationsModel;
import com.hanibalg.yeneservice.models.Reviews;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ProviderPageActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    Toolbar toolbar;
    FloatingActionButton fab;
    TextView da,t;
    private EditText appoint_desc;
    private Spinner spinner;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private GoogleMap mMap;
    private Button pageOpen;
    private RecyclerView commentRecycler;
    private ReviewAdapter reviewAdapter;
    private List<Reviews> mReview;
    private List<UserModel> mUser;
    private DocumentReference locationReference;
    private String providerID;
    private TextView viewAllReview;
    private RecentJobUserAdaptor recentJobUserAdaptor;
    private List<UserModel>recentUsers;
    private String TAG = ProviderPageActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_page);
        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);

        //firebase int
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //GET data
        Intent intentA = getIntent();
//        String m =  intentA.getStringExtra("userID");
        UserModel u = (UserModel) intentA.getSerializableExtra("userData");
        String working = intentA.getStringExtra("providerDataSpe");
        String aboutIntent = intentA.getStringExtra("providerDataAbout");
        providerID =  intentA.getStringExtra("providerDataInfo");

        String exp =  intentA.getStringExtra("providerExpr");
        String ty =  intentA.getStringExtra("providerType");
        String edu =  intentA.getStringExtra("providerEducation");
        String add =  intentA.getStringExtra("providerAddress");

        if(providerID != null){
            Log.d("providerInformationId","-"+ providerID);
            Log.d("providerInformationId","-"+ locationReference);
        }
        locationReference = FirebaseFirestore.getInstance().collection("Locations").document(providerID);
        if(u != null){
            Log.d("providerInformationId","_"+ u.getUserID());
        }
        //Attach data
        ImageView imageView = findViewById(R.id.providerImg);
        TextView name = findViewById(R.id.providerName);
        Chip work = findViewById(R.id.workingArea);
        TextView about = findViewById(R.id.about);
        TextView expriance = findViewById(R.id.pExpriance);
        TextView type = findViewById(R.id.pType);
        TextView education = findViewById(R.id.pEducation);
        TextView address = findViewById(R.id.pAddress);

        viewAllReview = findViewById(R.id.viewAllReview);
        viewAllReview.setOnClickListener(v -> {
            Intent re = new Intent(this,ReviewActivity.class);
            re.putExtra("providerID",providerID);
            startActivity(re);
        });
        fab = findViewById(R.id.fab);
        pageOpen = findViewById(R.id.openBtm);

        mReview = new ArrayList<>();
        mUser = new ArrayList<>();

        pageOpen.setOnClickListener(v -> {
            initBottomSheet();
            CardView llBottomSheet = findViewById(R.id.bottom_sheet);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            // set callback for changes
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    // this part hides the button immediately and waits bottom sheet
                    // to collapse to show
                    bottomSheetBehavior.setPeekHeight(450);
                    if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                        fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                    } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                        fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                }
            });
        });
        //bottom
        Button btn_date = findViewById(R.id.dialog_bt_date);
        Button btn_tim = findViewById(R.id.dialog_bt_time);
        AppCompatButton btn_submit = findViewById(R.id.appoint);
        spinner = findViewById(R.id.service_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        appoint_desc = findViewById(R.id.des);
        da = findViewById(R.id.tx);
        t = findViewById(R.id.txx);
        btn_date.setOnClickListener(this);
        btn_tim.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        //end

        String fullname = u.getFirstName()+" " +u.getLastName();
        Picasso.get().load(u.getImage()).placeholder(R.drawable.placeholder_profile).into(imageView);
        name.setText(fullname);
        about.setText(aboutIntent);
        expriance.setText(exp);
        type.setText(ty);
        education.setText(edu);
        address.setText(add);

//        List<String> speciality = m.getSpeciality();
//        working.toCharArray();
        work.setText(working);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabMode); // get the reference of TabLayout
        TabLayout.Tab firstTab = tabLayout.newTab(); // Create a new Tab names
        TabLayout.Tab secondTab = tabLayout.newTab(); // Create a new Tab names
        TabLayout.Tab thirdTab = tabLayout.newTab(); // Create a new Tab names
        firstTab.setText("First Tab"); // set the Text for the first Tab
        secondTab.setText("Two Tab"); // set the Text for the first Tab
        thirdTab.setText("third Tab"); // set the Text for the first Tab
//        firstTab.setIcon(R.drawable.ic_bookmark); // set an icon for the first tab
        tabLayout.addTab(firstTab,true); // add  the tab to the TabLayout
        tabLayout.addTab(secondTab); // add  the tab to the TabLayout
        tabLayout.addTab(thirdTab); // add  the tab to the TabLayout

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        } else {
            throw new NullPointerException("Something went wrong");
        }

        //=============================================================================
        initBottomSheet();
        commentRecycler = findViewById(R.id.commentRecyclerView);
        commentRecycler.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter(this,mReview,mUser);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        commentRecycler.setLayoutManager(mLayoutManager);
        commentRecycler.setAdapter(reviewAdapter);
        getFeedBacks(providerID);
        getLastFiveJob();
        //---------------------------------------------------------------------------------------
    }

    private void getFeedBacks(String providerID) {
        Log.d("ProviderInfomrationId","DATA"+providerID);
//        if(u != null){
            firebaseFirestore
                    .collection("Users")
                    .document(providerID)
                    .collection("rating")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                                String i = doc.getDocument().getId();
                                Reviews reviews = doc.getDocument().toObject(Reviews.class);
                                reviews.setId(i);
                                assert reviews != null;
                                Log.d("UserReviewJob","+"+reviews.getId());
                                Log.d("UserReviewJob","+"+reviews.getJob_id());
                                getJobInfo(reviews);

//                            Log.d("UserReviewJob", "Data does not exist");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d("UserReviewJob",e.getMessage());
                    });

//        }

    }

    private void getJobInfo(Reviews jobId) {
//        Log.d("UserReviewJob",jobId.getJobId());
        if(jobId.getJob_id() != null){
            firebaseFirestore.collection("JobsRequests")
                    .document(jobId.getJob_id())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            AppointmentJobModel appointmentJobModel = task.getResult().toObject(AppointmentJobModel.class);
                            if (appointmentJobModel != null){
                                getUser(jobId,appointmentJobModel);
                                Log.d("UserReviewJob","provider"+appointmentJobModel.getService_provider_id());
                            }
                        }
                    }).addOnFailureListener(e -> Log.d("UserReviewJob",e.getMessage()));
        }
    }

    private void getUser(Reviews jobId, AppointmentJobModel appointmentJobModel) {
        firebaseFirestore.collection("Users")
                .document(appointmentJobModel.getService_provider_id())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        UserModel userModel = task.getResult().toObject(UserModel.class);
                        mUser.add(userModel);
                        mReview.add(jobId);
                        reviewAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initBottomSheet() {
        // get the bottom sheet view
        CardView llBottomSheet = (CardView) findViewById(R.id.bottom_sheet);
        // init the bottom sheet behavior
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // this part hides the button immediately and waits bottom sheet
                // to collapse to show
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });
        final FloatingActionButton floatingActionButton;
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            floatingActionButton.isOrWillBeHidden();
        });
    }

    private void providerInfo(String id){
        firebaseFirestore.collection("Service_Providers")
                .whereEqualTo("user_id",id)
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentChange doc:task.getResult().getDocumentChanges()){
                        UserModel userModel2 = doc.getDocument().toObject(UserModel.class);
                    }
                }).addOnFailureListener(e -> Toast.makeText(ProviderPageActivity.this, "error getting user", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.dialog_bt_date:
                handleDateButton();
                break;
            case R.id.dialog_bt_time:
                handleTimeButton();
                break;
            case R.id.appoint:
                appoint();
                break;
        }
    }
    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        //material
        calendar.set(Calendar.MONTH,Calendar.JANUARY);
        long JAN = calendar.getTimeInMillis();
        calendar.set(Calendar.MONTH,Calendar.DECEMBER);
        long DES = calendar.getTimeInMillis();

        //calander constrain
        CalendarConstraints.Builder conBuilder = new CalendarConstraints.Builder();
        conBuilder.setStart(JAN);
        conBuilder.setEnd(DES);

//        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
//        builder.setTitleText("Select Date");
//        builder.setSelection(today);
//        builder.setCalendarConstraints(conBuilder.build());
//        final MaterialDatePicker materialDatePicker = builder.build();
//
//        materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
        //end material
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, date) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, month);
            calendar1.set(Calendar.DATE, date);
            String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

            da.setText(dateText);
        }, YEAR, MONTH, DATE);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i("time", "onTimeSet: " + hour + minute);
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
        String priority = spinner.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(description)) {
            appoint_desc.setError("Enter problem description!");
            return;
        }
        if(!TextUtils.isEmpty(description) ){
            Intent intent = getIntent();
            UserModel u = (UserModel) intent.getSerializableExtra("userData");
            String nm = intent.getExtras().getString("firstName");

            Map<String, Object> appointMap = new HashMap<>();
            appointMap.put("jobAppointedUserID", auth.getUid());
            appointMap.put("service_provider_id", u.getUserID());
            appointMap.put("problem_description", description);
            appointMap.put("date", dateAppoint);
            appointMap.put("time", timeAppoint);
            appointMap.put("timestamp", Timestamp.now());
            appointMap.put("isAccepted", false);
            appointMap.put("priority", priority);

            final DocumentReference ref = firebaseFirestore.collection("JobsAppointments").document();
            firebaseFirestore.collection("JobsRequests").document().set(appointMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        String myId = ref.getId();
                        Toast.makeText(ProviderPageActivity.this, "Appointment successful.", Toast.LENGTH_LONG).show();
//                        new SweetAlertDialog(ServiceProviderProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                .setTitleText("Good job!")
//                                .setContentText("You request appointment Successfully!")
//                                .show();

//                        Intent mainIntent = new Intent(ServiceProviderProfileActivity.this, HomeActivity.class);
//                        mainIntent.putExtra("doccumentId",myId);
//                        startActivity(mainIntent);
                        finish();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(ProviderPageActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                    }
//                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    }


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
                Log.e("ty", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("ghj", "Can't find style. Error: ", e);
        }
        Intent intentB = getIntent();
        UserModel uu = (UserModel) intentB.getSerializableExtra("userData");
        String id = intentB.getStringExtra("providerDataInfo");
        Log.d("locationUser","_Id: "+providerID);
        if (id != null) {

            firebaseFirestore.collection("Locations")
                    .document(providerID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            LocationsModel locationModel = task.getResult().toObject(LocationsModel.class);
//                          Log.d("locationUser", String.valueOf(locationModel.getUser()));
//                            GeoPoint loc = task.getResult().getGeoPoint("location");
                            if(locationModel != null){
                                double lat = locationModel.getProviderLocation().getLatitude();
                                double lng = locationModel.getProviderLocation().getLongitude();
//                                Log.d("locationUser","_"+locationModel.getProviderLocation());
                                Log.d("locationUser","___"+task.getResult().getData());
                                final LatLng pLocation = new LatLng(lat, lng);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(pLocation);
                                markerOptions.title("my location");
                                mMap.addMarker(markerOptions);
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pLocation,10));
                            }

                        }else{
                            Log.d("locationUser","data not complete");
                        }
                    }).addOnFailureListener(e -> Log.d("locationUser","_"+e));


        } else{
            Log.d("locationUser","++Id not found");
        }

    }

    private void getLastFiveJob(){
        recentUsers = new ArrayList<>();
        RecyclerView recentRecyclerView = findViewById(R.id.recent_job);
        recentRecyclerView.setHasFixedSize(true);
        firebaseFirestore.collection("JobsRequests")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                        AppointmentJobModel appointmentJobModel = doc.getDocument().toObject(AppointmentJobModel.class);
                        Log.d("Recentjob",""+appointmentJobModel.getJobAppointedUserID());
                        recentUsers(appointmentJobModel);

                    }
                }).addOnFailureListener(e -> Log.d(TAG,"error"+e));
        recentJobUserAdaptor = new RecentJobUserAdaptor(this,recentUsers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recentRecyclerView.setLayoutManager(mLayoutManager);
        recentRecyclerView.setAdapter(recentJobUserAdaptor);
    }

    private void recentUsers(AppointmentJobModel jobAppointedUserID) {
        if(jobAppointedUserID.getService_provider_id().equals(this.providerID)){
            Log.d(TAG,"recentJob_"+jobAppointedUserID.getJobAppointedUserID());
            firebaseFirestore.collection("Users")
                    .document(jobAppointedUserID.getJobAppointedUserID().trim())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            UserModel t = task.getResult().toObject(UserModel.class);
                            assert t != null;
                            Log.d(TAG,"recentJob_"+t.getFirstName());
                            recentUsers.add(t);
                            recentJobUserAdaptor.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(e -> Log.d(TAG,"error"+e));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.provider_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bookmark){
            bookmarkProvider(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void bookmarkProvider(MenuItem item) {
        Map<String,Object> favProvider = new HashMap<>();
        favProvider.put("documentID",providerID);
        favProvider.put("timestamp",Timestamp.now());
        firebaseFirestore.collection("Users")
                .document(auth.getUid())
                .collection("Favorite")
                .add(favProvider)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
//                        if(item.getIcon() == R.drawable.ic_bookmark){
//
//                        }
                        item.setIcon(R.drawable.ic_bookmark_black_24dp);
                        Toast.makeText(ProviderPageActivity.this, "provider saved", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
