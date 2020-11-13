package com.hanibalg.yeneservice.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button sendLink;
    private EditText inputEmail;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //init
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        inputEmail = findViewById(R.id.email);
        sendLink = findViewById(R.id.forgotBtn);

        sendLink.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                inputEmail.setError("Enter your email address!");
                inputEmail.setFocusable(true);
                return;
            }else{
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if(task.isComplete()){
                                Toast.makeText(ForgotPasswordActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Opps.., try again", Toast.LENGTH_SHORT).show();
                    Log.e("Error","error sending message",e);
                        });
            }

        });
    }
}
