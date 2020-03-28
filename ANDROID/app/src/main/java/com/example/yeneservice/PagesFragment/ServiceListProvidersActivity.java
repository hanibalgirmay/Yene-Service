package com.example.yeneservice.PagesFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.yeneservice.Adapters.ServiceProviderAdapter;
import com.example.yeneservice.MapsActivity;
import com.example.yeneservice.Models.ServicesProvider;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ServiceListProvidersActivity extends AppCompatActivity {

    LinearLayout btn;
    List<ServicesProvider> lstBook ;
    private static final String TAG = "MyActivity";
    ServiceProviderAdapter serviceProviderAdapter;
    ExtendedFloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Provider Lists");

        fab = findViewById(R.id.fab_list);
        // Recieve data
        Intent intent = getIntent();
        final String tte = intent.getExtras().getString("name").toLowerCase();
        final String serviceId = intent.getExtras().getString("serviceID");

        if(tte != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setTitle(tte);
            }
        }
        lstBook = new ArrayList<>();
        serviceProviderAdapter = new ServiceProviderAdapter(this,lstBook);

        RecyclerView myrv = findViewById(R.id.recyclerview_id);
        myrv.setHasFixedSize(true);
//        ServiceProviderAdapter myAdapter = new ServiceProviderAdapter(this,lstBook);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myrv.setLayoutManager(mLayoutManager);

        myrv.setAdapter(serviceProviderAdapter);

        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Service_Providers").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
//                        String id = doc.;
                        final String id = doc.getDocument().getString("userID");
//                        final String firstename = doc.getDocument().getString("firstName");
                        final String cty = doc.getDocument().getString("city");
                        final String work = doc.getDocument().getString("working_area");
                        final String add = doc.getDocument().getString("address");
                        final String me = doc.getDocument().getString("about_me");
                        final GeoPoint l = doc.getDocument().getGeoPoint("location");
//                        final Double g = doc.getDocument().getDouble("longitude");

                        if(tte.equals(work)){
                            Log.d(TAG,"file name: "+ cty);
                            final String documentId = doc.getDocument().getId();
                            db.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String image = documentSnapshot.getString("image");
                                    String firstename = documentSnapshot.getString("firstName");
                                    String lastename = documentSnapshot.getString("lastName");
                                    lstBook.add(new ServicesProvider(serviceId,id,firstename,lastename,image,add,work,me,l));
                                    serviceProviderAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            new SweetAlertDialog(ServiceListProvidersActivity.this, SweetAlertDialog.WARNING_TYPE)
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

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent o = new Intent(ServiceListProvidersActivity.this, MapsActivity.class);
                        o.putExtra("category",tte);
                        startActivity(o);
                    }
                });
//                new SweetAlertDialog(ServiceListProvidersActivity.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("We are really sorry?")
//                        .setContentText("Something is wrong!")
//                        .setConfirmText("Ok!")
//                        .showCancelButton(true)
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                            }
//                        })
//                        .show();
            }
        });
    }
}
