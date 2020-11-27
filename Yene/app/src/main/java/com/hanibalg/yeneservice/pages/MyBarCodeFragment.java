package com.hanibalg.yeneservice.pages;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.UserModel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBarCodeFragment extends Fragment {
    private DocumentReference reference;
    private FirebaseAuth auth;
    private String TAG = MyBarCodeFragment.class.getName();

    public MyBarCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reference = FirebaseFirestore.getInstance().document(auth.getUid());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bar_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView barcodeImage = view.findViewById(R.id.bar_code);
        TextView textView = view.findViewById(R.id.qr_name);
        /**
         * Check if the user is provider
         */
        reference.addSnapshotListener((documentSnapshot, e) -> {
            if(documentSnapshot.exists()){
                UserModel u = documentSnapshot.toObject(UserModel.class);
                String id = documentSnapshot.getId();
                u.setUserID(id);
                if(u.getProvider()){
                    textView.setText(u.getFirstName());
                    String sample_data_n = u.getFirstName();
                    String sample_data_l = u.getLastName();
                    String sample_data_e = u.getEmail();
                    String sample_data_ID = u.getUserID();
                    String sample_data_img = u.getImage();
                    Map<String,Object> qr = new HashMap<>();
                    qr.put("fname",sample_data_n);
                    qr.put("lname",sample_data_l);
                    qr.put("email",sample_data_e);
                    qr.put("userID",sample_data_ID);
                    qr.put("avatar",sample_data_img);

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(qr), BarcodeFormat.QR_CODE,200,200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        barcodeImage.setImageBitmap(bitmap);
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }else{
                    Log.d(TAG,"user is not a provider");
                    return;
                }
            }else{
                Log.d(TAG,"error occure on: "+e.getMessage());
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
