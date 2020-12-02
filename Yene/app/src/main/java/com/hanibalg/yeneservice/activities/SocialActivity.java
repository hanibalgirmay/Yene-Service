package com.hanibalg.yeneservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hanibalg.yeneservice.DashBoardActivity;
import com.hanibalg.yeneservice.MainActivity;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.Users.PhoneAuthenticationActivity;
import com.hanibalg.yeneservice.Users.RegisterActivity;

public class SocialActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnTwitter,btnFacebook,btnLinkdn;
    AppCompatButton logBtn;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    Button phoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        auth = FirebaseAuth.getInstance();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        firebaseUser = auth.getCurrentUser();

        //init
        logBtn = findViewById(R.id.phone_btn);
        TextView textView = findViewById(R.id.nreUser);
        phoneBtn = findViewById(R.id.phoneBtn);
//        btnFacebook.setOnClickListener(this);
//        btnLinkdn.setOnClickListener(this);
//        btnTwitter.setOnClickListener(this);
        logBtn.setOnClickListener(this);
        textView.setOnClickListener(this);
        phoneBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
//            case R.id.twitter_button:
//                Toast.makeText(this, "twitter button clicked", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.facebook_button:
//                Toast.makeText(this, "facebook button clicked", Toast.LENGTH_SHORT).show();
//                break;
//
            case R.id.nreUser:
//                Toast.makeText(this, "Register button clicked", Toast.LENGTH_SHORT).show();
                Intent r = new Intent(SocialActivity.this, RegisterActivity.class);
                startActivity(r);
                break;
            case R.id.phone_btn:
                Intent s = new Intent(SocialActivity.this, DashBoardActivity.class);
                startActivity(s);
                break;
            case R.id.phoneBtn:
                Intent ph = new Intent(SocialActivity.this, PhoneAuthenticationActivity.class);
                startActivity(ph);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if(auth.getUid() != null){
            startActivity(new Intent(SocialActivity.this, DashBoardActivity.class));
            finish();
        }
    }
}
