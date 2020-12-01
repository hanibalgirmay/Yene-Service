package com.hanibalg.yeneservice.payments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hanibalg.yeneservice.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PayPalPaymentActivity extends AppCompatActivity {

    TextView txtId,txtAmount,txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_payment);

        //init
        txtId = findViewById(R.id.textId);
        txtAmount = findViewById(R.id.textAmount);
        txtStatus = findViewById(R.id.textStatus);

        /**
         * Get Intent
         */
        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtAmount.setText("$"+paymentAmount);
            txtStatus.setText(response.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}