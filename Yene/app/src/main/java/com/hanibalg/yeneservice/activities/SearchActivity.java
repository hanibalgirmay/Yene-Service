package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ProvidersAdaptor;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private ImageView mSearchBtn;
    private FloatingActionButton btn_filter;
    private RecyclerView mResultList;
    private static final String TAG = "MyActivity";
    ProvidersAdaptor searchAdapter;
    List<ProviderModel> lstProvider ;
    List<UserModel> mUser ;
    private FirebaseFirestore db;
    private BottomSheetBehavior bottomSheetBehavior;
    Spinner spinner;
    HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //inin firebase
        db = FirebaseFirestore.getInstance();

        //bottomSheet
        spinner = findViewById(R.id.service_spinner);
        SeekBar seekBar = findViewById(R.id.seekBar);
        AppCompatButton button = findViewById(R.id.BtnFilter);
        final TextView textView = findViewById(R.id.sampleRate);
//        RecyclerView filterHorizontal = findViewById(R.id.search_horizontal);
        horizontalScrollView = findViewById(R.id.hor);

        button.setOnClickListener(v -> Toast.makeText(SearchActivity.this, "filter button clicked", Toast.LENGTH_SHORT).show());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setTextSize((float)(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //=====================================
        //init var
        searchView = findViewById(R.id.search);
        btn_filter = findViewById(R.id.fabFilter);

        mResultList = findViewById(R.id.search_recycler);
        mResultList.setHasFixedSize(true);
        lstProvider = new ArrayList<>();
        mUser = new ArrayList<>();
        searchAdapter = new ProvidersAdaptor(this, lstProvider,mUser);
        mResultList.setAdapter(searchAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProvider(query);
                mUser.clear();
//                lstProvider.clear();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProvider(newText);
                mUser.clear();
//                lstProvider.clear();
                return true;
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mResultList.setLayoutManager(mLayoutManager);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
        mResultList.setAdapter(searchAdapter);

        //filter
//        initComponent();
        btn_filter.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    /*
    * GET PROVIDER FROM FIREBASE
    */
    private void searchProvider(String newText) {
        db.collection("Service_Providers")
                .orderBy("city")
                .startAt(newText).endAt(newText + "\uf8ff")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                            final ProviderModel providerModel = doc.getDocument().toObject(ProviderModel.class);
                            //adaptor
                            Log.d("filterData,",doc.getDocument().getData().toString());
                            lstProvider.add(providerModel);
//                                lstProvider.add(providerModel);
//                                searchAdapter.notifyDataSetChanged();
                            DocumentReference userRef = providerModel.getUserInfo();
                            userRef.get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                                        //adaptor
                                        mUser.add(userModel);
//                                                lstProvider.add(providerModel);
                                        searchAdapter.notifyDataSetChanged();
                                    });
                        }
                    });
    }

    /*
    FILTER BOOTOMSHEET BEHAVIOR
     */
    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet = findViewById(R.id.filter);

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
        btn_filter = findViewById(R.id.fabFilter);
        btn_filter.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event){
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {
//                Rect outRect = new Rect();
////                llBottomSheet.getGlobalVisibleRect(outRect);
//                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.filter){
            Toast.makeText(this, "filter clicked", Toast.LENGTH_SHORT).show();
            //TODO filter horizontal
//            horizontalScrollView.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchView.setSubmitButtonEnabled(true);
        searchView.setEnabled(true);
        searchView.onWindowFocusChanged(true);
    }
}
