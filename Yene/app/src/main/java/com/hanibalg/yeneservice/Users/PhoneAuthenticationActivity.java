package com.hanibalg.yeneservice.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.hanibalg.yeneservice.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneAuthenticationActivity extends AppCompatActivity implements View.OnClickListener {
    private PinView pinView;
    private Button next;
    private TextView topText,textU;
    private EditText userName, userPhone;
    private ConstraintLayout first, second;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private String Verificationid;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //init
        progressBar = findViewById(R.id.progressbar);
        topText = findViewById(R.id.topText);
        pinView = findViewById(R.id.pinView);
        next = findViewById(R.id.button);
        userName = findViewById(R.id.username);
        userPhone = findViewById(R.id.userPhone);
        first = findViewById(R.id.first_step);
        second = findViewById(R.id.secondStep);
        textU = findViewById(R.id.textView_noti);
        first.setVisibility(View.VISIBLE);

        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (next.getText().equals("Let's go!")) {
            String name = userName.getText().toString();
            String phone = userPhone.getText().toString().trim();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                if(phone.length() < 9){
                    userPhone.setError("Valid number is required");
                    userPhone.requestFocus();
                    return;
                }
                String phoneNumber = "+" + "251" + phone;
                sendVerificationCode(phoneNumber);
                next.setText("Verify");
                first.setVisibility(View.GONE);
                second.setVisibility(View.VISIBLE);
                topText.setText("I Still don't trust you.nTell me something that only two of us know.");
            } else {
                Toast.makeText(PhoneAuthenticationActivity.this, "Please enter the details", Toast.LENGTH_SHORT).show();
            }
        } else if (next.getText().equals("Verify")) {
            String OTP = pinView.getText().toString();
            verifyCode(OTP);
//            if (OTP.isEmpty()) {
//                pinView.setLineColor(Color.GREEN);
//                textU.setText("OTP Verified");
//                textU.setTextColor(Color.GREEN);
//                next.setText("Next");
//            } else {
//                pinView.setLineColor(Color.RED);
//                textU.setText("X Incorrect OTP");
//                textU.setTextColor(Color.RED);
//            }
        }

    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
        Toast.makeText(this, "sending verification..."+ phoneNumber, Toast.LENGTH_SHORT).show();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Verificationid = s;
            Toast.makeText(PhoneAuthenticationActivity.this, "code: "+ s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code); 
                Toast.makeText(PhoneAuthenticationActivity.this, "verify code: "+ code, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneAuthenticationActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Toast.makeText(PhoneAuthenticationActivity.this, "invalid request", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(PhoneAuthenticationActivity.this, "To many request", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Verificationid, code);
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        storageReference = FirebaseStorage.getInstance().getReference();
//                        firebaseFirestore.collection("Users").document();

                        String token_id = FirebaseInstanceId.getInstance().getToken();
                        String current_id = auth.getCurrentUser().getUid();
                        Map<String, Object> tokenMap = new HashMap<>();
                        tokenMap.put("token_id", token_id);

                        firebaseFirestore.collection("Users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Intent intent = new Intent(PhoneAuthenticationActivity.this, HomeActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(PhoneAuthenticationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}