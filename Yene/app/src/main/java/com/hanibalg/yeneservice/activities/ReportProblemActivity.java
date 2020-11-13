package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;

import java.util.HashMap;
import java.util.Map;

public class ReportProblemActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        //init
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        
        TextInputEditText msg_input = findViewById(R.id.report_msg);
        Button btn = findViewById(R.id.btn_report);
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = msg_input.getText().toString().trim();
                if(TextUtils.isEmpty(data)){
                    msg_input.setError("Write the issue here...");
                    return;
                } else {
                    Map<Object,Object> report = new HashMap<>();
                    report.put("user_reported", auth.getUid());
                    report.put("reported_description",data);
                    report.put("timestamp", Timestamp.now());
                    firebaseFirestore.collection("Reports").add(report)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(ReportProblemActivity.this, "Thanks for your report , we'll gladly review and fix it as soon as possible", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
