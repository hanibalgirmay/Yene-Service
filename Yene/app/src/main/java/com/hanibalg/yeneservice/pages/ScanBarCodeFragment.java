package com.hanibalg.yeneservice.pages;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

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
                            UserModel userModel = task.getResult().toObject(UserModel.class);
                            userModel.setUserId(task.getResult().getId());
                            Log.d(TAG,"+ "+task.getResult().getData());
                            ProviderInfo(text,userModel);
                        }
                    }).addOnFailureListener(e -> Log.d(TAG,e.getMessage()));

        }
    }

    private void ProviderInfo(String uId, UserModel userModel){
        showDialog(userModel);
        firebaseFirestore.collection("Service_Providers")
                .whereEqualTo("user_id",uId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (DocumentChange doc: task.getResult().getDocumentChanges()){
                            ProviderModel providerModel = doc.getDocument().toObject(ProviderModel.class);
                            Log.d(TAG,"provider_Info:"+doc.getDocument().getData());
                        }
                    }
                }).addOnFailureListener(e -> Log.d(TAG,e.getMessage()));
    }

    private void showDialog(UserModel userModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.scanned_popup, viewGroup,false);

        TextView pname = dialogView.findViewById(R.id.scan_name);
        TextView pservice = dialogView.findViewById(R.id.scan_service);
        ImageView pavatar = dialogView.findViewById(R.id.scan_img);
        Picasso.get().load(userModel.getImage()).placeholder(R.drawable.placeholder_profile).into(pavatar);
        pname.setText(userModel.getFirstName());

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
