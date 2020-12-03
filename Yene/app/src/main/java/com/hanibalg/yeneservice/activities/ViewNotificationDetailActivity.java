<<<<<<< HEAD
package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.NotificationAdaptor;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ViewNotificationDetailActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private TextView name,msg,date,time;
    private ImageView profile;
    private MaterialButton btnAccept,btnDecline;
    private String TAG = ViewNotificationDetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification_detail);

        firebaseFirestore = FirebaseFirestore.getInstance();
        //init var
        name = findViewById(R.id.nName);
        profile = findViewById(R.id.nProfile);
        msg = findViewById(R.id.nMsg);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        btnAccept = findViewById(R.id.btn_accept);
        btnDecline = findViewById(R.id.btn_decline);

        String intent_user = getIntent().getStringExtra("userID");
        String intent_msg = getIntent().getStringExtra("dataMsg");
        String intent_job = getIntent().getStringExtra("jobID");
        String intent_type = getIntent().getStringExtra("dataType");
        String intent_accept = getIntent().getStringExtra("dataAccepted");

        assert intent_user != null;
        if(!intent_user.equals("")){
            firebaseFirestore.collection("Users").document(intent_user)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        UserModel u = Objects.requireNonNull(task.getResult()).toObject(UserModel.class);
                        assert u != null;
                        name.setText(u.getFirstName());
                        Picasso.get().load(u.getImage()).into(profile);
                    }
                }
            });
        }
//        checkJobAvailability(intent_job);
        msg.setText(intent_msg);
//        date.setText(intent);
        checkJobAvailability(intent_job);
    }

    private void checkJobAvailability(String jobID) {
        try{
            firebaseFirestore.collection("JobsRequests")
                    .document(jobID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isComplete()){
                            AppointmentJobModel appointmentJobModel = task.getResult().toObject(AppointmentJobModel.class);
                            appointmentJobModel.setDocID(task.getResult().getId());
                            Log.d(TAG,"_**_"+task.getResult().getData());
                            if(appointmentJobModel.JobAccepted()){
//                                btnAccept.setEnabled(false);
//                                btnDecline.setEnabled(false);
                                btnAccept.setVisibility(View.GONE);
                                btnDecline.setVisibility(View.GONE);
                            }
                        } else{
                            Toast.makeText(this, "JOb not found", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                e.printStackTrace();
                Log.d(TAG,""+e.getMessage());
            });
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,""+e.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
=======
package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.NotificationAdaptor;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ViewNotificationDetailActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private TextView name,msg,date,time;
    private ImageView profile;
    private MaterialButton btnAccept,btnDecline;
    private String TAG = ViewNotificationDetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification_detail);

        firebaseFirestore = FirebaseFirestore.getInstance();
        //init var
        name = findViewById(R.id.nName);
        profile = findViewById(R.id.nProfile);
        msg = findViewById(R.id.nMsg);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        btnAccept = findViewById(R.id.btn_accept);
        btnDecline = findViewById(R.id.btn_decline);

        String intent_user = getIntent().getStringExtra("userID");
        String intent_msg = getIntent().getStringExtra("dataMsg");
        String intent_job = getIntent().getStringExtra("jobID");
        String intent_type = getIntent().getStringExtra("dataType");
        String intent_accept = getIntent().getStringExtra("dataAccepted");

        assert intent_user != null;
        if(!intent_user.equals("")){
            firebaseFirestore.collection("Users").document(intent_user)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        UserModel u = Objects.requireNonNull(task.getResult()).toObject(UserModel.class);
                        assert u != null;
                        name.setText(u.getFirstName());
                        Picasso.get().load(u.getImage()).into(profile);
                    }
                }
            });
        }
//        checkJobAvailability(intent_job);
        msg.setText(intent_msg);
//        date.setText(intent);
        checkJobAvailability(intent_job);
    }

    private void checkJobAvailability(String jobID) {
        try{
            firebaseFirestore.collection("JobsRequests")
                    .document(jobID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isComplete()){
                            AppointmentJobModel appointmentJobModel = task.getResult().toObject(AppointmentJobModel.class);
                            appointmentJobModel.setDocID(task.getResult().getId());
                            Log.d(TAG,"_**_"+task.getResult().getData());
                            if(appointmentJobModel.JobAccepted()){
//                                btnAccept.setEnabled(false);
//                                btnDecline.setEnabled(false);
                                btnAccept.setVisibility(View.GONE);
                                btnDecline.setVisibility(View.GONE);
                            }
                        } else{
                            Toast.makeText(this, "JOb not found", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                e.printStackTrace();
                Log.d(TAG,""+e.getMessage());
            });
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,""+e.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
>>>>>>> 38ad388af22ba498a9929d1337ddd1faf64803bc
