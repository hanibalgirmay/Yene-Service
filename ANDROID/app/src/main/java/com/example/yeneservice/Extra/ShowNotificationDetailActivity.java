package com.example.yeneservice.Extra;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ShowNotificationDetailActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    TextView msg,frmm,time;
    Button accept,decline;
    ImageView profile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification_detail);

        //intitalize
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance().collection("Appointments").document();
        
        if (firebaseUser.getUid().isEmpty()){
            Toast.makeText(this, "You are not registered or something wrong", Toast.LENGTH_SHORT).show();
        }
        msg = findViewById(R.id.mesg);
        time = findViewById(R.id.tim);
        frmm = findViewById(R.id.from);
        profile = findViewById(R.id.img);
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);

        String dataMessage = getIntent().getStringExtra("message");
        String dataFrom = getIntent().getStringExtra("from_user_id");
        String dataTo = getIntent().getStringExtra("to");
        String timestamp = getIntent().getStringExtra("timestamp");
        final String DocID = getIntent().getStringExtra("documentID");

        msg.setText(dataMessage);
        time.setText(timestamp);
//        frm.setText("From: "+ dataFrom);
        
        getUser(dataFrom);
        
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowNotificationDetailActivity.this, "accept button clicked"+DocID, Toast.LENGTH_SHORT).show();
                acceptJob(DocID,true);
            }
        });
        
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowNotificationDetailActivity.this, "decline button is clicked", Toast.LENGTH_SHORT).show();
                acceptJob(DocID,false);
            }
        });
    }
    
    private void acceptJob(String Id,boolean mesg) {
        String d = Id.trim();
        boolean ms= mesg;
        Map<String,Object> not = new HashMap<>();
        not.put("isAccepted",ms);
        firebaseFirestore.collection("Users/"+firebaseUser.getUid()+"/Notifications/")
                .document(d)
                .update(not).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ShowNotificationDetailActivity.this, "Users Notifications updated", Toast.LENGTH_SHORT).show();
            }
        });
//        reference.collection("Users/"+firebaseUser.getUid()+"/Notifications/").document(d).set(not);
//        reference.set(not);

    }

    private void getUser(final String frm) {
        String ID = frm.trim();
        if(ID.isEmpty()){
            Toast.makeText(this, "error field required!", Toast.LENGTH_SHORT).show();
        }
        firebaseFirestore.collection("Users").document(ID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    String img = documentSnapshot.getString("image");
                    String fname = documentSnapshot.getString("firstName");

                    Picasso.get().load(img).placeholder(R.drawable.businessman_profile_cartoon_removebg).into(profile);
                    frmm.setText(fname);
                }
            }
        });
    }
}
