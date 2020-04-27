package com.example.yeneservice.Extra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.yeneservice.Adapters.AppointmentUserAdapter;
import com.example.yeneservice.Models.AppointemntUserModel;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AppointedUsersActivity extends AppCompatActivity {
    List<AppointemntUserModel> lstUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    private static final String TAG = "MyActivity";
    AppointmentUserAdapter appointmentUserAdapter;
    RecyclerView mn;
    String user_id;
    private String img,ph,fname,lname,email,st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointed_users);

        Toolbar toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointed service provider");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(AppointementUserActivity.this,AppointementUserActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        mn = findViewById(R.id.usr_aa);

        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();

        lstUser = new ArrayList<>();
        appointmentUserAdapter = new AppointmentUserAdapter(this,lstUser,false);

        mn.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mn.setLayoutManager(mLayoutManager);

        mn.setAdapter(appointmentUserAdapter);

        // Access a Cloud Firestore instance from your Activity
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("JobsRequests").whereEqualTo("isAccepted",true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
//                    if(doc.getType() == DocumentChange.Type.MODIFIED){
                        final String id = doc.getDocument().getString("jobAppointedUserID");
                        final String serviceProviderId = doc.getDocument().getString("service_provider_id");
//                        final String working = doc.getDocument().getString("working_area");
                        assert id != null;
                        if(user_id.equals(id)){
                            Log.d(TAG,"file name: "+ serviceProviderId);
                            firebaseFirestore.collection("Users").document(serviceProviderId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().exists()){
                                            img = task.getResult().getString("image");
                                            ph = task.getResult().getString("phone");
                                            fname = task.getResult().getString("firstName");
                                            lname = task.getResult().getString("lastName");
                                            email = task.getResult().getString("email");
                                            st = task.getResult().getString("status");


                                            lstUser.add(new AppointemntUserModel(serviceProviderId, fname,email,img,st,true));
                                            appointmentUserAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });

                        }

//                    }
                }

            }
        });

    }

}
