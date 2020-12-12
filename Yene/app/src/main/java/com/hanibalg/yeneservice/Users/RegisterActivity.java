package com.hanibalg.yeneservice.Users;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.DashBoardActivity;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.config.CustomSweetAlert;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register-Activity";
    EditText inputEmail, inputPhone,inputPassword,inputPassword2,inputFname,inputLname;
    MaterialButton register;
    SignInButton google;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private CustomSweetAlert myAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //firebase init
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //init
        inputEmail = findViewById(R.id.txtEmail);
        inputPhone = findViewById(R.id.txtPhone);
        inputFname = findViewById(R.id.txtFirstName);
        inputLname = findViewById(R.id.txtLastName);
        inputPassword = findViewById(R.id.txtPassword);
        inputPassword2 = findViewById(R.id.txtPassConfirm);
        register = findViewById(R.id.btnReg);

//        facebook = findViewById(R.id.btnFace);
//        twitter= findViewById(R.id.btnTwit);
        google = findViewById(R.id.btnLink);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        google.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        register.setOnClickListener(v -> {
            final String email = inputEmail.getText().toString().trim();
            final String first_name = inputFname.getText().toString().trim();
            final String last_name = inputLname.getText().toString().trim();
            final String phone = inputPhone.getText().toString().trim();
            final String pass = inputPassword.getText().toString().trim();
            final String pass2 = inputPassword2.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                inputEmail.setError("Enter email address!");
                inputEmail.setFocusable(true);
                return;
            }
            if (TextUtils.isEmpty(first_name)) {
                inputFname.setError("Enter first name!");
                inputFname.setFocusable(true);
                return;
            }
            if (TextUtils.isEmpty(last_name)) {
                inputLname.setError("Enter last name!");
                inputLname.setFocusable(true);
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                inputPhone.setError("Enter phone number!");
                inputPhone.setFocusable(true);
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                inputPassword.setError("password is required!");
                inputPassword.setFocusable(true);
                return;
            }
            if (TextUtils.isEmpty(pass2)) {
                inputPassword2.setError("password is required!");
                inputPassword2.setFocusable(true);
                return;
            }
            if (pass.length() < 6) {
                inputPassword.setError("Password too short, enter minimum 6 characters!");
                return;
            }
            if(!pass.equals(pass2)){
                inputPassword.setError("Password is not same");
                return;
            }
            else {
//                myAlert.alertLoading("loading....");
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                auth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                //save user
                                Map<String,Object> usr = new HashMap<>();
                                usr.put("firstName",first_name);
                                usr.put("lastName",last_name);
                                usr.put("email",email);
                                usr.put("phone",phone);
                                usr.put("receiveNews",true);
                                usr.put("Provider",false);
                                usr.put("image","default");
//                                myAlert.alertLoading("registering...");
                                String id = task.getResult().getUser().getUid();
                                firebaseFirestore.collection("Users").document(id)
                                        .set(usr).addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()){
//                                                myAlert.stopAlert();
                                                pDialog.dismiss();
                                                Toast.makeText(RegisterActivity.this, "data saved successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, DashBoardActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error saving users....", Toast.LENGTH_SHORT).show());
                            }
                        });
                pDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        registerUser(user);
                        
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        
                    }
                });
        pDialog.dismiss();
    }

    private void registerUser(FirebaseUser user) {
        Map<String,Object> register = new HashMap<>();
        register.put("firstName",user.getDisplayName());
        register.put("lastName","");
        register.put("phone",user.getPhoneNumber());
        register.put("email",user.getEmail());
        register.put("image",user.getPhotoUrl());
        register.put("receiveNews",true);
        register.put("Provider",false);
        try {
            myAlert.alertLoading("saving...");
            firebaseFirestore.collection("Users").document(user.getUid())
                    .set(register).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,DashBoardActivity.class));
                    finish();
                } else{
                    Toast.makeText(this, "error,Something happen!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,"error saving user to firebase"+e.getMessage());
        }

    }

}
