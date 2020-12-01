package com.hanibalg.yeneservice.pages;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hanibalg.yeneservice.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanBarCodeFragment extends Fragment {
    private static final int CAMERA_PERMISSION_CODE=101;
    private static final int FILE_SHARE_PERMISSION = 102;
    private String TAG = ScanBarCodeFragment.class.getName();
    private CodeScanner mCodeScanner;
    private FirebaseFirestore firebaseFirestore;

    public ScanBarCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_bar_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        firebaseFirestore = FirebaseFirestore.getInstance();
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(result -> activity.runOnUiThread(() -> {
            Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
            String[] resultArray = result.getText().split("\n");
            //key and value
//            for (int r = 0; r< resultArray.length; r++){
//                String[] k = resultArray[r].split(":");
//            }
            Log.d(TAG, "_ "+resultArray[0]);
            Log.d(TAG, "_ "+resultArray[1]);
            Log.d(TAG, "_ "+resultArray[2]);
            Log.d(TAG, "_id "+resultArray[3]);
            String UiD = resultArray[3];
            getScannedProvider(UiD);
        }));
        scannerView.setOnClickListener(view1 -> mCodeScanner.startPreview());

        super.onViewCreated(view, savedInstanceState);
    }

    private void getScannedProvider(String text) {
        if(!text.isEmpty()){
            Log.d(TAG,"getProvider: "+text);
            firebaseFirestore.collection("Users")
                    .document(text.trim())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Log.d(TAG,"+ "+task.getResult().getData());
                            ProviderInfo(text);
                        }
                    }).addOnFailureListener(e -> Log.d(TAG,e.getMessage()));

        }
    }

    private void ProviderInfo(String uId){
        firebaseFirestore.collection("Service_Providers")
                .whereEqualTo("user_id",uId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (DocumentChange doc: task.getResult().getDocumentChanges()){
                            Log.d(TAG,"provider_Info+ "+doc.getDocument().getData());
                        }
                    }
                }).addOnFailureListener(e -> Log.d(TAG,e.getMessage()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
