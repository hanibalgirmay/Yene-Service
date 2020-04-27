package com.example.yeneservice.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.example.yeneservice.Adapters.QRCodeHelper;
import com.example.yeneservice.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ShareServiceProviderProfileActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_service_provider_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        constraintLayout = findViewById(R.id.man);
//        Glide.with(this)
//                .load(R.drawable.businessman_profile_cartoon_removebg)
//                .transform(new BlurTransformation())
//                .into(constraintLayout);
//        imageView = findViewById(R.id.qrCodeImageView);

//        getCode();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.provider_share, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    //    private void getCode() {
//        try {
//            barcode();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//    private void barcode() throws WriterException {
//        Intent intent = getIntent();
//        String idd = intent.getExtras().getString("user");
//        String Docid = intent.getExtras().getString("doc");
//
//        String o = "hello hanibal";
//        BitMatrix bitMatrix = multiFormatWriter.encode(idd, BarcodeFormat.QR_CODE,350,350);
//        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//        imageView.setImageBitmap(bitmap);
//    }
}
