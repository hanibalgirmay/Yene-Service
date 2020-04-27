package com.example.yeneservice.Users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.yeneservice.HomeActivity;
import com.example.yeneservice.R;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneAuthenticateActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText editText;
    Button login, create;
    Button cont,usingEmail_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authenticate);

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
        editText = findViewById(R.id.phone_number);

        cont = findViewById(R.id.cont);
        usingEmail_btn = findViewById(R.id.using_email);

        usingEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lo = new Intent(PhoneAuthenticateActivity.this,LoginActivity.class);
                startActivity(lo);
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
            String number = editText.getText().toString().trim();

            if(number.isEmpty() || number.length() < 8){
                editText.setError("phone number is required");
                editText.requestFocus();
                return;
            }
            String phoneNumber = "+" + code + number;
            Intent t = new Intent(PhoneAuthenticateActivity.this, PhoneActivateActivity.class);
            t.putExtra("phoneNumber", phoneNumber);
            startActivity(t);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }

}
