package com.example.yeneservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReviewActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String user_id,dcId;
    private FirebaseFirestore firebaseFirestore;
    private RatingBar ratingBar;
    private Button btnSend;
    private EditText content;
    private TextView result;
    private static final String TAG = "ReviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        dcId = firebaseFirestore.collection().getUid();

//        addListenerOnBtnComment();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        content = (EditText) findViewById(R.id.content);
        result = (TextView) findViewById(R.id.result);
        // Recieve data
        Intent intent = getIntent();
        String fname = intent.getExtras().getString("desc");
        final String id = intent.getExtras().getString("userID");
        final String provider_ID = intent.getExtras().getString("provider_id");
        final String DocId = intent.getExtras().getString("docID");
        final String document = intent.getExtras().getString("documentID");

        result.setText(fname +" "+ document);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            private Object Date;

            @Override
            public void onClick(View view) {
                Log.d("rating",String.valueOf(ratingBar.getRating()));
                Log.d("content", String.valueOf(content.getText()));
//                result.setText("Rating : "+ String.valueOf(ratingBar.getRating())+"\nComment: " +String.valueOf(content.getText()));
                result.setText(DocId);
                final String cont = content.getText().toString().trim();
                if(!TextUtils.isEmpty(cont)){
                    Map<String, Object> reviewMap = new HashMap<>();
                    reviewMap.put("reviewID", user_id);
                    reviewMap.put("service_provider_id", provider_ID);
                    reviewMap.put("content", cont);
                    reviewMap.put("rate", String.valueOf(ratingBar.getRating()));
                    reviewMap.put("appointementID", document);
                    reviewMap.put("timestamp", Timestamp.now());

//                    final Map<String, Float> ratingwMap = new HashMap<>();
////                    ratingwMap.put("reviewID", user_id);
////                    ratingwMap.put("service_provider_id", provider_ID);
//                    ratingwMap.put("rate", Float.valueOf(String.valueOf(ratingBar.getRating())));


                    firebaseFirestore.collection("Reviews").document(document).set(reviewMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            new SweetAlertDialog(ReviewActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Good job!")
                                    .setContentText("You reviewed Successfully!")
                                    .show();
                        }
                    });

                }
            }
        });
    }
}
