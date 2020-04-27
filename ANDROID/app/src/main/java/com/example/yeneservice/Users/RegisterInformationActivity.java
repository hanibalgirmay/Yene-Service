package com.example.yeneservice.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yeneservice.HomeActivity;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterInformationActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword, inputConfirmPassword, inputFirstName,inputLastName;
    private Spinner inputPhone;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String user_id;
    private boolean isChanged = false;
    private Bitmap compressedImageFile;
    private CircleImageView setupImage;
    private Uri mainImageURI = null;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_information);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
//        user_id = auth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnSignIn = (Button) findViewById(R.id.btn_to_login);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.confirmPass);
        inputFirstName = (EditText) findViewById(R.id.firstname);
        inputLastName = (EditText) findViewById(R.id.lastname);
//        inputPhone = findViewById(R.id.city);
        progressBar = (ProgressBar) findViewById(R.id.progressRegsiter);
//        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
//        btnSignUp.setEnabled(false);

        final Spinner spinner = (Spinner) findViewById(R.id.city_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent lo = new Intent(RegisterInformationActivity.this,LoginActivity.class);
                startActivity(lo);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstName = inputFirstName.getText().toString().trim();
                final String lastName = inputLastName.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String city = spinner.getSelectedItem().toString().trim();
                Toast.makeText(RegisterInformationActivity.this, "city: "+city, Toast.LENGTH_SHORT).show();
                final String confipassword = inputConfirmPassword.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String token_id = FirebaseInstanceId.getInstance().getToken();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter email address!");
                    inputEmail.requestFocus();
//                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError(getString(R.string.input_error_email_invalid));
                    inputEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(firstName)) {
                    inputFirstName.setError("first name required!");
                    inputFirstName.requestFocus();
//                    Toast.makeText(getApplicationContext(), "Enter first name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    inputLastName.setError("last name is required!");
                    inputLastName.requestFocus();
//                    Toast.makeText(getApplicationContext(), "Enter last name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confipassword)) {
                    inputConfirmPassword.setError("Enter confirm password!");
                    inputConfirmPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Enter password, required!");
                    inputPassword.requestFocus();
//                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    inputPassword.setError("Password too short, enter minimum 6 characters!");
                    inputPassword.requestFocus();
//                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(city)){
//                    progressBar.setVisibility(View.VISIBLE);
                    if(password.equals(confipassword)){
                        progressBar.setVisibility(View.VISIBLE);
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    assert firebaseUser != null;
                                    user_id = firebaseUser.getUid();

                                    SaveUserInfo(firstName,lastName,email,city,token_id);
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterInformationActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else {
                        inputPassword.setError("password do not match!!!");
                    }
                }

//                progressBar.setVisibility(View.VISIBLE);

            }
        });

    }

    private void SaveUserInfo(String firstname, String lastname, String email, String city, String token_id) {
        Uri download_uri;

        final SweetAlertDialog pDialog = new SweetAlertDialog(RegisterInformationActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", firstname);
        userMap.put("lastName", lastname);
        userMap.put("email", email);
        userMap.put("city", city);
        userMap.put("isProvider",false);
        userMap.put("type","user");
        userMap.put("username","");
        userMap.put("image","default");
        userMap.put("receiveNews",true);
        userMap.put("timestamp", Timestamp.now());
        userMap.put("token_id", token_id);

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pDialog.cancel();
                    Toast.makeText(RegisterInformationActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(RegisterInformationActivity.this, ProfileRegisterActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(RegisterInformationActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                }
//                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        progressBar.setVisibility(View.GONE);
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if(currentUser != null){
//            Intent start = new Intent(RegisterInformationActivity.this,HomeActivity.class);
//            startActivity(start);
//        } else {
//            Intent lo = new Intent(RegisterInformationActivity.this,PhoneAuthenticateActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(lo);
//        }
    }

    public void openLoginPage(View view) {
    }
}
