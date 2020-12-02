package com.hanibalg.yeneservice.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private DocumentReference reference;
    private TextView inputFname,inputLname,inputPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get data from the previous activity
        String t = getIntent().getStringExtra("name");
        String tt = getIntent().getStringExtra("value");

        if(t != null){
            toolbar.setTitle(t);
        } else {
            toolbar.setTitle("");
        }
        toolbar.setNavigationOnClickListener(view -> finish());
        //init
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = FirebaseFirestore.getInstance().collection("Users").document(auth.getUid());

        //initialize
        inputFname = findViewById(R.id.fname);
        inputLname = findViewById(R.id.phone);
        inputPhone = findViewById(R.id.lname);
        Button btn = findViewById(R.id.updateBtn);

        TextInputEditText name = findViewById(R.id.name);
        TextInputLayout nameLayout = findViewById(R.id.nameLayout);
        name.setText(tt);
        nameLayout.setHint(t);
//        name.setHint(t);

        btn.setOnClickListener(v -> update(t));

//        getUserInfo();
    }

    private void getUserInfo() {
        reference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                Log.d(EditProfileActivity.class.getName(), String.valueOf(userModel));
                if(userModel != null ){
                    inputFname.setText(userModel.getFirstName());
                    inputLname.setText(userModel.getLastName());
                    inputPhone.setText(userModel.getPhone());
                }

            }
            Toast.makeText(EditProfileActivity.this, "ops...", Toast.LENGTH_SHORT).show();
        });
    }

    private void update(String t){
        TextInputEditText name = findViewById(R.id.name);
        String fname = name.getText().toString().trim();

        if(TextUtils.isEmpty(fname)){
            Toast.makeText(this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
        }
        Map<String,Object> updateUSer = new HashMap<>();
        updateUSer.put(t,fname);
        reference.update(updateUSer).addOnSuccessListener(aVoid -> Toast.makeText(EditProfileActivity.this, "update successful", Toast.LENGTH_SHORT).show());
    }
}
