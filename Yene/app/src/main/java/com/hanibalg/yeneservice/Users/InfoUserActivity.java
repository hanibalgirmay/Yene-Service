package com.hanibalg.yeneservice.Users;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hanibalg.yeneservice.DashBoardActivity;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.config.CustomSweetAlert;
import com.hanibalg.yeneservice.models.IDIdentification;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gun0912.tedbottompicker.TedBottomPicker;

public class InfoUserActivity extends AppCompatActivity {
    @BindView(R.id.selectIdCard)
    MaterialButton imgSelectBtn;
    @BindView(R.id.save)
    MaterialButton saveInfoBtn;
    @BindView(R.id.selectedImage)
    ImageView imageView;
    @BindView(R.id.wereda)
    TextInputEditText inputWereda;
    @BindView(R.id.kebele)
    TextInputEditText inputKebele;
    @BindView(R.id.public_id_number)
    TextInputEditText inputIdNumber;
    @BindView(R.id.kefele_ketema)
    AutoCompleteTextView inputKefeleKetema;
    @BindView(R.id.id_expireDate)
    TextInputEditText inputExpireDate;

//    private MaterialButton imgSelectBtn,saveInfoBtn;
//    private ImageView imageView;
    private Uri selectedUriList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private DocumentReference reference;
    private Uri imgUrl;
    private String downloadUrlImage;
    FirebaseStorage storage;
    StorageReference storageRef;


    private String TAG = InfoUserActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        ButterKnife.bind(this);
//        imgSelectBtn = findViewById(R.id.selectIdCard);
//        imageView = findViewById(R.id.selectedImage);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        reference = FirebaseFirestore.getInstance().collection("ID_Info").document(auth.getUid());

        String[] KEFELEKETEMA = new String[] {"Arada Sub City", "Akaki Kaliti Sub City", "Addis Ketema Sub City", "Bole Sub City",
        "Gullele Sub City","Kirkos Sub City","Kolfe Keranio Sub City","Lideta Sub City","Nefas Silk Sub City","Yeka Sub City"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                       this,
                        R.layout.text_drop_down_menu,
                        KEFELEKETEMA);
        inputKefeleKetema.setAdapter(adapter);

        //click handler
//        imgSelectBtn.setOnClickListener(this);
    }

    @OnClick(R.id.save)
    public void onSaveInfoClick(){
        String ID_Number = inputIdNumber.getText().toString();
        String Kebele =  inputKebele.getText().toString();
        String Wereda =  inputWereda.getText().toString();
        String SubCity = inputKefeleKetema.getText().toString();
        String ExpireDate = inputExpireDate.getText().toString();

        if (ID_Number.isEmpty()){
            inputIdNumber.setError("Enter your ID number");
            inputIdNumber.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(Kebele.toString())){
            inputKebele.setError("your kebele is required! ");
            inputKebele.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(Wereda.toString())){
            inputWereda.setError("your wereda is required! ");
            inputWereda.setFocusable(true);
            return;
        }
        if (SubCity.isEmpty()){
            inputKefeleKetema.setError("subCity is required! ");
            inputKefeleKetema.setFocusable(true);
            return;
        }
        if (ExpireDate.isEmpty()){
            inputExpireDate.setError("your ID expired date required! ");
            inputExpireDate.setFocusable(true);
            return;
        }
        if (selectedUriList == null){
            Toast.makeText(this, "Your ID card image is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (selectedUriList != null){
                //show progress
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Uploading...");
                pDialog.setCancelable(false);
                pDialog.show();
//                Uri uploadUri = Uri.fromFile(new File(selectedUriList.toString()));
                Log.d(TAG,"_________"+selectedUriList);
                StorageReference ref = storageRef.child("ID_cards/"+ UUID.randomUUID().toString());
                ref.putFile(selectedUriList)
                        .addOnSuccessListener(taskSnapshot -> {
                            //dismiss dialog
                            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                                Log.d("uploaded_image_info","_________"+uri);
                                saveData(uri,ID_Number,Wereda,Kebele,ExpireDate,SubCity);
                                pDialog.dismiss();
                                Toast.makeText(InfoUserActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                            });

                        }).addOnFailureListener(e -> {
                            e.printStackTrace();
                            Log.d(TAG,"onFailed"+e);
                            pDialog.dismiss();
                            Toast.makeText(InfoUserActivity.this, "upload error,try again", Toast.LENGTH_SHORT).show();
                        }).addOnProgressListener(snapshot -> {
                            double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            pDialog.setTitleText("Uploaded "+(int)progress+"%");
                        });
            }
            else{
                Toast.makeText(this, "Your ID card image is required!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void saveData(Uri downloadUrl, String ID_Number, String wereda, String kebele, String expireDate, String subCity) {
        if (downloadUrl != null){
            Map<String,Object> infoMap = new HashMap<>();
            infoMap.put("IDCardNumber",ID_Number);
            infoMap.put("Kebele",Integer.parseInt(kebele));
            infoMap.put("Wereda",Integer.parseInt(wereda));
            infoMap.put("SubCity",subCity);
            infoMap.put("IDExpireDate",expireDate);
            infoMap.put("IDScannedImage",downloadUrl.toString());
            infoMap.put("timestamp", Timestamp.now());

            Log.d("uploaded_image_info","_+_+_+"+infoMap);
            firebaseFirestore.collection("ID_Info")
                    .document(auth.getUid())
                    .set(infoMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(InfoUserActivity.this, "info updated", Toast.LENGTH_SHORT).show();
                        launchHome();
                    }).addOnFailureListener(e -> Toast.makeText(InfoUserActivity.this, "error happen", Toast.LENGTH_SHORT).show());
//            boolean r = reference.get().getResult().exists();
//            if (r){
//                firebaseFirestore.collection("ID_Info")
//                        .document(auth.getUid())
//                        .update(infoMap)
//                        .addOnSuccessListener(aVoid -> Toast.makeText(InfoUserActivity.this, "info updated", Toast.LENGTH_SHORT).show());
//
//            } else{
//                reference.set(infoMap).addOnCompleteListener(task -> {
//                    if (task.isSuccessful()){
//                        Toast.makeText(InfoUserActivity.this, "Document successfully saved", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(e -> {
//                    Toast.makeText(InfoUserActivity.this, "oppss, try again", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG,"onFailed"+e.getMessage());
//                });
//                //clear input
//                inputExpireDate.setText("");
//                inputKefeleKetema.setText("");
//                inputWereda.setText("");
//                inputIdNumber.setText("");
//                inputKebele.setText("");
//            }


        }
    }

    private void loadinfo(){
        firebaseFirestore.collection("ID_Info").document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isComplete()){
                        IDIdentification idIdentification = task.getResult().toObject(IDIdentification.class);
                        idIdentification.setUserId(auth.getUid());
                        inputKebele.setText(String.valueOf(idIdentification.getKebele()));
                        inputWereda.setText(String.valueOf(idIdentification.getWereda()));
                        inputIdNumber.setText(idIdentification.getIDCardNumber());
                        inputExpireDate.setText(idIdentification.getIDExpireDate());
                        inputKefeleKetema.setText(idIdentification.getSubCity());
                        try {
                            Picasso.get().load(idIdentification.getIDScannedImage()).placeholder(R.drawable.background).into(imageView);
                        } catch (Exception w){
                            w.printStackTrace();
                        }
                    }
                }).addOnFailureListener(e -> e.printStackTrace());
    }

    @OnClick(R.id.selectIdCard)
    public void chooseImg() {
        TedBottomPicker.with(this)
            .setPeekHeight(350)
            .showTitle(true)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Select")
            .show(uri -> {
                Log.d("user_information",uri.toString());
                try {
                    selectedUriList = uri;
                    Log.d(TAG,"_image: "+uri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUriList);
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                } catch (Exception r){
                    r.printStackTrace();
                }
            });
    }

    @Override
    public void onBackPressed() {
        new GuardandoContactoHilo().execute();
    }

    private class GuardandoContactoHilo extends AsyncTask< Void, Void, Void> {
        private CustomSweetAlert progressAlert;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressAlert.alertLoading("Saving Info...");
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            progressAlert.stopAlert();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference r = FirebaseFirestore.getInstance().collection("ID_Info").document(auth.getUid());
        r.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                //data exist
                IDIdentification idIdentification = documentSnapshot.toObject(IDIdentification.class);
                if (idIdentification.getIDScannedImage() == null || idIdentification.getIDCardNumber().isEmpty()){
                    Toast.makeText(this, "Upload or finish information", Toast.LENGTH_SHORT).show();
                    loadinfo();
                    return;
                }else{
                    launchHome();
                    return;
                }

            }else{
                //data do not exists
//                    launchHome();
//                    return;
            }
        }).addOnFailureListener(e -> Log.d(TAG,"Failed"+e));

    }

    private void launchHome() {
        Intent home = new Intent(this, DashBoardActivity.class);
        startActivity(home);
    }
}