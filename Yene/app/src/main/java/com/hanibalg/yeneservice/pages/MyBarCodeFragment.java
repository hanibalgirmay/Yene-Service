package com.hanibalg.yeneservice.pages;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.UserModel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBarCodeFragment extends Fragment {
    private DocumentReference reference;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private String TAG = MyBarCodeFragment.class.getName();

    public MyBarCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        reference = FirebaseFirestore.getInstance().document(auth.getUid());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bar_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView barcodeImage = view.findViewById(R.id.bar_code);
        TextView textView = view.findViewById(R.id.qr_name);
        LinearLayout errorLayout = view.findViewById(R.id.errLayout);
        LinearLayout mainL = view.findViewById(R.id.mainL);

        /**
         * Check if the user is provider
         */
        String uId = auth.getUid();
        reference = FirebaseFirestore.getInstance().collection("Users").document(uId.trim());

        try {
            reference.addSnapshotListener((documentSnapshot, e) -> {
                if(documentSnapshot.getData().isEmpty()){
                    mainL.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }
                if(documentSnapshot.exists()){
                    UserModel u = documentSnapshot.toObject(UserModel.class);
                    String id = documentSnapshot.getId();
                    Log.d(TAG,"_isUser: "+u.toString());
                    Log.d(TAG,"_isUserProvider: "+u.getProvider());
                    Log.d(TAG,"_news: "+u.getReceiveNews());
                    Log.d(TAG,"_ID "+auth.getUid());
                    if(u.getProvider()){
                        String sample_data_n = u.getFirstName();
                        String sample_data_l = u.getLastName();
                        String sample_data_e = u.getEmail();
                        String sample_data_ID = auth.getUid();
                        String sample_data_img = u.getImage();
                        String fullName = sample_data_n + " " +sample_data_l;

                        String qr = "name: "+fullName +"\nEmail: "+sample_data_e + "\navatar: "+sample_data_img+"\n"+sample_data_ID+'\n';
                        Log.d(TAG,"data_QR_CODE: "+qr);
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(qr, BarcodeFormat.QR_CODE,200,200);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            barcodeImage.setImageBitmap(bitmap);
                            textView.setText(fullName);
                        }catch (Exception ee){
                            ee.printStackTrace();
                        }
                    }else{
                        Log.d(TAG,"user is not a provider");
//                    mainL.setVisibility(View.GONE);
//                    errorLayout.setVisibility(View.VISIBLE);
                        return;
                    }
                }else{
                    //user not found
//                errorLayout.setVisibility(View.VISIBLE);
//                mainL.setVisibility(View.GONE);
                    Log.d(TAG,"error occure on: "+e.getMessage());
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        super.onViewCreated(view, savedInstanceState);
    }
}
