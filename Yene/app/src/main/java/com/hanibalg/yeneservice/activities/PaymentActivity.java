package com.hanibalg.yeneservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.PaymentCardAdaptor;
import com.hanibalg.yeneservice.models.PaymentCard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private List<PaymentCard>  paymentCards;
    private PaymentCard card;
    private PaymentCardAdaptor adaptor;
    private RecyclerView recyclerView;
    private TextInputEditText aCode,aUnicCode,cceCode,expire;
    private TextInputLayout aCodelayout,aUnicCodeLayout,cceCodeLayout,expireLayout;
    private ImageButton payBtn;
    private TextView textView;
    private static final String TAG = PaymentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //init
        aCode = findViewById(R.id.accountNumber);
        aUnicCode = findViewById(R.id.accountSecretCode);
        cceCode = findViewById(R.id.accountCCECode);
        expire = findViewById(R.id.accountExpire);
        textView = findViewById(R.id.text);
        //layout
        aCodelayout = findViewById(R.id.accountLayout);
        aUnicCodeLayout = findViewById(R.id.secretLayout);
        cceCodeLayout = findViewById(R.id.cceLayout);
        expireLayout = findViewById(R.id.expireLayout);
        payBtn = findViewById(R.id.pay);

        payBtn.setOnClickListener(this);
        recyclerView = findViewById(R.id.cardsLayout);
        recyclerView.setHasFixedSize(true);

        paymentCards = new ArrayList<>();
        paymentCards.add(new PaymentCard(R.color.blue,"CBE","2020","10000*********","bla bla"));
        paymentCards.add(new PaymentCard(R.color.colorPrimary,"Dashine","2021","541112*********","sdhjad"));
        paymentCards.add(new PaymentCard(R.color.daysLabelColor,"Awash","2023","234200*********","lkalsdl jd"));

        adaptor = new PaymentCardAdaptor(this,paymentCards);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManag);
        recyclerView.setAdapter(adaptor);
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View v = snapHelper.findSnapView(mLayoutManager);
                int pos = mLayoutManager.getPosition(v);
                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(pos);
                CardView linearLayout = holder.itemView.findViewById(R.id.cardBg);

                if(newState == RecyclerView.SCROLL_INDICATOR_LEFT){
                    linearLayout.setAlpha(.7f);
                    linearLayout.setPadding(30,5,30,5);
                    linearLayout.animate().setDuration(350).scaleX(0.8f).scaleY(0.8f).setInterpolator(new AccelerateInterpolator()).start();
                } else if(newState == RecyclerView.SCROLL_INDICATOR_RIGHT){
                    linearLayout.setAlpha(.7f);
                    linearLayout.setPadding(30,5,30,5);
                    linearLayout.animate().setDuration(350).scaleX(0.8f).scaleY(0.8f).setInterpolator(new AccelerateInterpolator()).start();
                } else {
                    linearLayout.animate().setDuration(350).scaleX(1f).scaleY(1f).setInterpolator(new AccelerateInterpolator()).start();
                    textView.setText(paymentCards.get(pos).getCardName());
                    linearLayout.setPadding(30,5,30,5);
                    linearLayout.setAlpha(1);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.pay){
            String acc = String.valueOf(aCode.getText());
            String unique = aUnicCode.getText().toString().trim();
            String CCE = String.valueOf(cceCode.getText());
            String Exp = String.valueOf(expire.getText());
            if(TextUtils.isEmpty(unique)){
                aUnicCodeLayout.setError("Error enter valid information");
                aUnicCodeLayout.setFocusable(true);
                return;
            }
            Map<String,Object> d = new HashMap<>();
            d.put("account number",acc);
            d.put("account unique code",unique);
            d.put("account expire date",Exp);
            d.put("account CCE Code",CCE);
            Log.d(TAG, String.valueOf(d));
        }
    }
}
