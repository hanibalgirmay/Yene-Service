package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ProviderServiceListAdapter;
import com.hanibalg.yeneservice.models.LocationsModel;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProvidersUserListActivity extends AppCompatActivity {

    private List<ProviderModel> mProvider;
    private List<UserModel> mUser;
    private List<ServiceListModel> SList;
    DocumentReference reference,reference2;
    FirebaseFirestore firebaseFirestore;
    private ProviderServiceListAdapter adapter;
    private ExtendedFloatingActionButton extendedFloatingActionButton;

    //dummy
    UserModel userModel;
    ServiceListModel serviceListModel;
    LocationsModel locationModel;
    private LottieAnimationView lottieAnimationView;
    private LinearLayout noServiceLayout;
    private NestedScrollView nestedScrollView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers_user_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Provider Lists");
        setSupportActionBar(toolbar);

        //GET intent file
        Intent po = getIntent();
        String tit = po.getExtras().getString("title");
        String icon  = po.getExtras().getString("icon");
        if(tit != null){
            toolbar.setTitle(tit);
        }

        lottieAnimationView = findViewById(R.id.animation_view_list);
        noServiceLayout = findViewById(R.id.noServices);
        nestedScrollView = findViewById(R.id.mainL);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = FirebaseFirestore.getInstance().collection("Users").document("asdasd a");
        reference2 = FirebaseFirestore.getInstance().collection("Locations").document("asdasd a");
        //init variable
        extendedFloatingActionButton = findViewById(R.id.extended_fab);
        ImageView sortB = findViewById(R.id.sortBtn);

        //end

        //Spinner
        Spinner spinner = findViewById(R.id.type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                   //noting
                } else {
                    Toast.makeText(ProvidersUserListActivity.this, ""+parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
                    sortByType(position);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        extendedFloatingActionButton.setOnClickListener(v -> {
            Intent g = new Intent(ProvidersUserListActivity.this, ProviderMapsActivity.class);
            startActivity(g);
        });
        //load data
        load_Provider(tit);
    }

    private void sortByType(int position) {
        List<ProviderModel> providerModels = new ArrayList<>();

    }

    private void sortByOrganization() {
        Collections.sort(mProvider,(l1,l2) ->l1.getType().compareTo(l2.getType()));
    }

    private void sortByIndivdual() {
        Collections.sort(mProvider,(l1,l2) ->l1.getGender().compareTo(l2.getGender()));
    }

    private void load_Provider(String tit) {
        mProvider = new ArrayList<>();
        mUser = new ArrayList<>();
        SList = new ArrayList<>();
        RecyclerView myrv = findViewById(R.id.recyclerview_id);
        myrv.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myrv.setLayoutManager(mLayoutManager);

        myrv.setAdapter(adapter);
        //firebase
        getProviderData(tit);
        //end
        List<String> y = new ArrayList<>();
        y.add("tryrt");
        y.add("ghh");
        //provider
        adapter = new ProviderServiceListAdapter(this,mProvider,mUser);
        myrv.setLayoutManager(mLayoutManager);
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(adapter);
        myrv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                extendedFloatingActionButton.extend();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                extendedFloatingActionButton.shrink();
            }
        });

    }

    private void getProviderData(String tit) {
        firebaseFirestore.collection("Service_Providers")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                        if(!doc.getDocument().getData().isEmpty()){
                            final ProviderModel providerModel = doc.getDocument().toObject(ProviderModel.class);
                                if (providerModel.getSpeciality().contains(tit.toLowerCase())){
//                                    Log.d("specialityList",);
                                    if(!providerModel.getUser_id().equals(auth.getUid())){
                                        lottieAnimationView.setVisibility(View.GONE);
                                        noServiceLayout.setVisibility(View.GONE);
                                        extendedFloatingActionButton.setVisibility(View.VISIBLE);
                                        nestedScrollView.setVisibility(View.VISIBLE);

                                        //get user info
                                        firebaseFirestore.collection("Users")
                                                .document(providerModel.getUser_id())
                                                .get()
                                                .addOnCompleteListener(task -> {
                                                    if(task.isSuccessful()){
                                                        UserModel userMode = task.getResult().toObject(UserModel.class);
                                                        String id = task.getResult().getId();
                                                        userMode.setUserId(id);
                                                        Log.d("search-item",id);
                                                        mProvider.add(providerModel);
                                                        mUser.add(userMode);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                });
                                    }
                                }
//                                else{
//                                    lottieAnimationView.setVisibility(View.VISIBLE);
//                                    noServiceLayout.setVisibility(View.VISIBLE);
//                                    extendedFloatingActionButton.setVisibility(View.INVISIBLE);
//                                    nestedScrollView.setVisibility(View.GONE);
//                                }

                        } else{
                            lottieAnimationView.setVisibility(View.VISIBLE);
                            noServiceLayout.setVisibility(View.VISIBLE);
                            extendedFloatingActionButton.setVisibility(View.INVISIBLE);
                            nestedScrollView.setVisibility(View.GONE);
                        }

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.asc){
            Toast.makeText(this, "ascending clicked", Toast.LENGTH_SHORT).show();
            sortByOrganization();
        }
        if(itemId == R.id.desc){
            Toast.makeText(this, "descending clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
