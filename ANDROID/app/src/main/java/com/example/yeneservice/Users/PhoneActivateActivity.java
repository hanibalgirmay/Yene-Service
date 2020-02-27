package com.example.yeneservice.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeneservice.HomeActivity;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneActivateActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText editText;
    private TextView ph;
    private Button sign;
    private String Verificationid,mRToken;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_activate);

        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();

        firebaseFirestore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        ph = findViewById(R.id.ph);
        sign = findViewById(R.id.buttonSignIn);

        String phone = getIntent().getStringExtra("phoneNumber");
        sendVerificationCode(phone);
        ph.setText(phone);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String code = editText.getText().toString().trim();
            if ((code.isEmpty() || code.length() < 6)){
                editText.setError("Enter code...");
                editText.requestFocus();
                return;
            }
            verifyCode(code);
            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("Users").document();

                        String token_id = FirebaseInstanceId.getInstance().getToken();
                        String current_id = mAuth.getCurrentUser().getUid();
                        Map<String, Object> tokenMap = new HashMap<>();
                        tokenMap.put("token_id", token_id);

                        firebaseFirestore.collection("Users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(PhoneActivateActivity.this, RegisterInformationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
    //                    Intent intent = new Intent(PhoneActivateActivity.this, RegisterInformationActivity.class);
    //                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    //                    startActivity(intent);

                    } else {
                        Toast.makeText(PhoneActivateActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
        Toast.makeText(this, "sending verification...", Toast.LENGTH_SHORT).show();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.d(TAG, "onCodeSent:" + s);
            super.onCodeSent(s, forceResendingToken);
            Verificationid = s;
            Toast.makeText(PhoneActivateActivity.this, "code: "+ s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
                Toast.makeText(PhoneActivateActivity.this, "code: "+code, Toast.LENGTH_SHORT).show();
//                signInWithCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneActivateActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Toast.makeText(PhoneActivateActivity.this, "invalid request", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(PhoneActivateActivity.this, "To many request", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
