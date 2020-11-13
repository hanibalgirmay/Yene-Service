package com.hanibalg.yeneservice.pages;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.GPSTracker;
import com.hanibalg.yeneservice.PermissionUtils;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.servicesModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    TextView dateTxt;
    Button dateBtn;
    AutoCompleteTextView serviceType, providerType;
    TextInputEditText desc;
    Spinner spinner;
    Switch aSwitch;
    FirebaseFirestore firebaseFirestore;
    Calendar dateD;
    FirebaseAuth auth;
    private static final String TAG = "AddJobPost";
    private String service,serviceTypeText,providerTypeText;
    private boolean permissionDenied = false;
    private LocationListener locationListener = null;
    private LocationManager locationManager = null;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private GPSTracker gpsTracker;

    public AddPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_add_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (gpsTracker.getIsGPSTrackingEnabled()){
            gpsTracker.updateGPSCoordinates();
            gpsTracker.getLatitude();
            gpsTracker.getLongitude();
        }else {
            gpsTracker.showSettingsAlert();
        }
        //init
        dateTxt = view.findViewById(R.id.txtDate);
        dateBtn = view.findViewById(R.id.post_choose_date);
        aSwitch = view.findViewById(R.id.switchValue);
        Button btnPOst = view.findViewById(R.id.postJob);
        desc = view.findViewById(R.id.post_des);

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapM);
        mapFragment.getMapAsync(this);

        dateBtn.setOnClickListener(this);
        btnPOst.setOnClickListener(this);

        serviceType = view.findViewById(R.id.post_service_type);
        providerType = view.findViewById(R.id.post_provider_type);
        spinner = view.findViewById(R.id.post_spinner);

        getSpinnerValue();
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_dropdown_item_1line, PROVIDER_TYPE);
//        serviceType.setAdapter(adapter2);

        providerType.setAdapter(adapter3);
        providerTypeText = providerType.getText().toString().trim();
        //data

    }

    private void postJob() {
//        if(TextUtils.isEmpty(s_type) || TextUtils.isEmpty(p_type) ||  TextUtils.isEmpty(problem)){
//            Toast.makeText(getContext(), "Fill all the above fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if(serviceTypeText != null || providerTypeText != null){
            Boolean isPro = aSwitch.isEnabled();
            String s = spinner.getSelectedItem().toString().trim();
            String problem = desc.getText().toString().trim();
            serviceTypeText = serviceType.getEditableText().toString().trim();
            providerTypeText = providerType.getEditableText().toString().trim();
            String ddd = DateFormat.format("EEEE, MMM d, yyyy", dateD).toString();
            int mm  = dateD.MONTH;
            int dd  = dateD.DAY_OF_MONTH;
            int yyy  = dateD.YEAR;
            Map<String,Object> jobPost = new HashMap<>();
            jobPost.put("userID",auth.getUid());
            jobPost.put("serviceName",s);
            jobPost.put("service_type",serviceTypeText);
            jobPost.put("provider_type",providerTypeText);
            jobPost.put("problem_desc",problem);
            jobPost.put("jobDate",ddd);
            jobPost.put("onlyProfessional",isPro);
            jobPost.put("timestamp", Timestamp.now());

            Log.d(TAG, String.valueOf(jobPost));
            firebaseFirestore.collection("Jobs").add(jobPost)
                    .addOnCompleteListener(task -> {
                        if(task.isComplete()) {
                            Toast.makeText(getContext(), "Job Posted", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getContext(), "Error occur, Try again", Toast.LENGTH_SHORT).show();
                    });
        }

    }

    private void getSpinnerValue() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<String> qw = new ArrayList<>();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,  qw);
        firebaseFirestore.collection("Services")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for (DocumentChange dov: queryDocumentSnapshots.getDocumentChanges()){
                        servicesModel i = dov.getDocument().toObject(servicesModel.class);
                        qw.add(dov.getNewIndex(),i.getService_name().trim());
//                        getServiceList(i.getService_name());
                    }
                    Set<String> dat = new HashSet<String>(qw);
                    qw.clear();
                    qw.add("Select Service");
                    qw.addAll(dat);
                    Log.d(TAG, String.valueOf(qw.size()));

                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerArrayAdapter);
                    spinnerArrayAdapter.notifyDataSetChanged();
                    final int index =0;
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(index == position){
//                                serviceType.setFocusable(false);
//                                serviceType.setFocusableInTouchMode(false);
                                return;
                            }
//                            serviceType.setFocusable(false);
//                            serviceType.setFocusableInTouchMode(false);
                            String p = parent.getItemAtPosition(position).toString();
                            Log.d("SERVICE_LIST",p);
                            Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                            service = p;
                            getServiceList(p);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //noting here
                        }
                    });
                });

    }

    private void getServiceList(String textCat) {
        Log.d(TAG,textCat);
        ArrayList<String> ww = new ArrayList<>();
//        String textChange = textCat.substring(0,1).toUpperCase() +textCat.substring(1).toLowerCase();
        firebaseFirestore.collection("Services_List")
                .whereEqualTo("category",textCat.toLowerCase())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for (DocumentChange dov: queryDocumentSnapshots.getDocumentChanges()){
                        ServiceListModel is = dov.getDocument().toObject(ServiceListModel.class);
                        ww.add(is.getName());
                    }
                    Set<String> vb = new HashSet<>(ww);
                    ww.clear();
                    ww.addAll(vb);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                            android.R.layout.simple_dropdown_item_1line, ww);
                    serviceType.setAdapter(adapter2);
                    serviceTypeText = serviceType.getEditableText().toString();
//                    serviceType.setValidator(new AutoCompleteTextView.Validator() {
//                        @Override
//                        public boolean isValid(CharSequence text) {
//                            Arrays.sort(ww);
//                            if(Arrays.binarySearch(ww,text.toString()) > 0){
//                                return true;
//                            }
//                            return false;
//                        }
//
//                        @Override
//                        public CharSequence fixText(CharSequence invalidText) {
//                            return null;
//                        }
//                    });
//                    serviceType.setOnFocusChangeListener((v, hasFocus) -> {
//                        if(v.getId() == R.id.post_service_type && !hasFocus){
//                            ((AutoCompleteTextView)v).performValidation();
//                        }
//                    });
                });

    }

    private static final String[] PROVIDER_TYPE = new String[] {
            "Individual", "Organizational", "Both"
    };


    //date choose
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (datePicker, year, month, date) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, month);
            calendar1.set(Calendar.DATE, date);
            String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();
            dateD = calendar1;

            dateTxt.setText(dateText);
        }, YEAR, MONTH, DATE);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(9.0707804, 38.7323016), 10));
//        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            map.setMyLocationEnabled(true);
//            map.setOnMyLocationButtonClickListener(this);
//            map.setOnMyLocationClickListener(this);
//        }
        MarkerOptions markerOptions = new MarkerOptions();
        final LatLng ET = new LatLng(9.0707804, 38.7323016);
        markerOptions.position(ET);
        markerOptions.title("test location");
        map.addMarker(markerOptions);

    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
//            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }
    // [END maps_check_location_permission_result]

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.post_choose_date){
            handleDateButton();
        }
        if(id == R.id.postJob){
            postJob();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}
