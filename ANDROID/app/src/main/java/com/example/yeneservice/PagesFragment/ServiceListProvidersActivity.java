package com.example.yeneservice.PagesFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.yeneservice.Adapters.ServiceProviderAdapter;
import com.example.yeneservice.Models.ServicesProvider;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Provider Lists");

        // Recieve data
        Intent intent = getIntent();
        final String tte = intent.getExtras().getString("name").toLowerCase();

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
                        final String firstename = doc.getDocument().getString("firstName");
                        final String lastname = doc.getDocument().getString("lastName");
                        final String work = doc.getDocument().getString("working area");
                        final String add = doc.getDocument().getString("address");
                        final String me = doc.getDocument().getString("about me");

                        if(tte.equals(work)){
                            Log.d(TAG,"file name: "+ firstename);
                            db.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String image = documentSnapshot.getString("image");
                                    lstBook.add(new ServicesProvider(id,firstename,lastname,image,add,work,me));
                                    serviceProviderAdapter.notifyDataSetChanged();
                                }
                            });

                            // stop animating Shimmer and hide the layout
//                            mShimmerViewContainer.stopShimmerAnimation();
//                            mShimmerViewContainer.setVisibility(View.GONE);
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

                new SweetAlertDialog(ServiceListProvidersActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("We are really sorry?")
                        .setContentText("Something is wrong!")
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
        });
    }
}
