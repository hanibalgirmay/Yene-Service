package com.hanibalg.yeneservice.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab0,fab1,fab2,cameraFab;
    private ImageView myProfile;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private int pos = 0;
    private DocumentReference reference;
    private Switch aSwitch;
    private TextInputLayout uInputLayout;
    Uri selectedUri = null;
    List<Uri> selectedUriList = null;
    private TextInputEditText uText;
    private LinearLayout uPhone,uEmail,uCity;
    private int CAMERA_PERMSSION_CODE = 1;
    private int GALLERY_PERMSSION_CODE = 1;
    ArrayList<Uri> imgUrl = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //firebase init
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String uId = auth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        reference = FirebaseFirestore.getInstance().collection("Users").document(uId);
        //FLOATING ICONS!
        fab0 = findViewById(R.id.fab0);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        cameraFab = findViewById(R.id.camera);

        aSwitch = findViewById(R.id.receveNews);
        myProfile = findViewById(R.id.profile);

        LinearLayout pAbout = findViewById(R.id.provider_about);
        LinearLayout pInfo = findViewById(R.id.card_provider_info);

        TextView name = findViewById(R.id.fullname);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView em = findViewById(R.id.profile_email);
        TextView fn = findViewById(R.id.profile_username);
        TextView ph = findViewById(R.id.profile_phone);
        TextView ct = findViewById(R.id.profile_city);
        TextView abtme = findViewById(R.id.profile_aboutMe);


//        uInputLayout = findViewById(R.id.userLayout);
//        uText = findViewById(R.id.username);
        LinearLayout po = findViewById(R.id.cr);
        uPhone = findViewById(R.id.crr);
        uEmail = findViewById(R.id.crrr);
        uCity = findViewById(R.id.crrrr);
        po.setOnClickListener(this);
        uPhone.setOnClickListener(this);
        uEmail.setOnClickListener(this);
        uCity.setOnClickListener(this);

        //animation
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab0.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        aSwitch.setOnClickListener(this);

        reference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                String full_name = userModel.getFirstName() + " " + userModel.getLastName();
                name.setText(full_name);
                em.setText(userModel.getEmail());
                ph.setText(userModel.getPhone());
                fn.setText(full_name);
                email.setText(userModel.getEmail());
                ct.setText(userModel.getCity());
                aSwitch.setChecked(userModel.getReceiveNews());
                Picasso.get().load(userModel.getImage()).placeholder(R.drawable.placeholder_profile).into(myProfile);
                boolean providerCheck = userModel.getProvider();
                Toast.makeText(this, "isProvider: "+providerCheck, Toast.LENGTH_SHORT).show();
                if(providerCheck){
                    pAbout.setVisibility(View.VISIBLE);
                    pInfo.setVisibility(View.VISIBLE);

                }
//                else{
//                    pAbout.setVisibility(View.GONE);
//                    pInfo.setVisibility(View.GONE);
//                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab0:
                animateFAB();
                break;
            case R.id.fab1:
//                Intent edit = new Intent(this,EditProfileActivity.class);
//                startActivity(edit);
                Toast.makeText(this, "showcase add", Toast.LENGTH_SHORT).show();
                showcase();
                break;
            case R.id.fab2:
                Toast.makeText(this, "education certificates", Toast.LENGTH_SHORT).show();
//                TedBottomPicker.with(this)
//                        .setPeekHeight(350)
//                        .showTitle(true)
//                        .setCompleteButtonText("Done")
//                        .setEmptySelectionText("No Select")
//                        .setSelectedUri(selectedUriList)
//                        .show(uri -> {
//                            Log.d("profileImage",uri.toString());
//                            updateUserProfile();
////                            Picasso.get().load(uri).placeholder(R.drawable.placeholder_profile).into(myProfile);
//                        });
                break;
            case R.id.camera:
                openCamera();
                break;
            case R.id.receveNews:
                updateNews();
                break;

            case R.id.cr:
                TextView user = findViewById(R.id.profile_username);
                String u = user.getText().toString();
                editMyProfile("Full Name",u);
                break;

            case R.id.crr:
                TextView ph = findViewById(R.id.profile_phone);
                String p = ph.getText().toString();
                editMyProfile("phone",p);
                break;
            case R.id.crrr:
                TextView em = findViewById(R.id.profile_email);
                String e = em.getText().toString();
                editMyProfile("Email",e);
                break;
             case R.id.crrrr:
                TextView ct = findViewById(R.id.profile_city);
                String c = ct.getText().toString();
                editMyProfile("city",c);
                break;
            default:
                break;
        }
    }

    private void showcase(){
        TedBottomPicker.with(this)
            .setPeekHeight(400)
            .showTitle(false)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Select")
            .setSelectedUriList(selectedUriList)
            .showMultiImage(uriList -> {
                imgUrl.addAll(uriList);

                final StorageReference ref = storageRef.child("providers");
                int i=0;
                for (i =0; i < imgUrl.size(); i++){
                    final Uri individualImage = imgUrl.get(i);
                    final StorageReference imgU = ref.child("showcases/" + UUID.randomUUID().toString());
                    imgU.putFile(individualImage).addOnSuccessListener(taskSnapshot -> {
                        imgU.getDownloadUrl().addOnSuccessListener(uri -> {
                            String link = String.valueOf(uri);
                            Toast.makeText(MyProfileActivity.this, "Show-case Uploaded!!", Toast.LENGTH_SHORT).show();
                        });
                        Log.d("ddd","error");
                    });
                }
            });

    }

    private void openCamera() {
        if(ContextCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "you have already granted this permission", Toast.LENGTH_SHORT).show();
        } else{
            requestCameraPermissions();
        }
    }

    private void requestCameraPermissions() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            //alert dialog
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permsiion is needed because of camera access now")
                    .setPositiveButton("ok", (dialog, which) -> {
                        ActivityCompat.requestPermissions(MyProfileActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMSSION_CODE);
                    })
                    .setNegativeButton("cancel",(dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERMSSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERMSSION_CODE){
            if(grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "this permssion is granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "permission is denied", Toast.LENGTH_SHORT).show();
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void editMyProfile(String name, String data) {
        Intent edit = new Intent(MyProfileActivity.this, EditProfileActivity.class);
        edit.putExtra("name", name);
        edit.putExtra("value", data);
        startActivity(edit);
    }

    private void animateFAB() {
        if (isFabOpen){
            fab0.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);

            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        }else {
            fab0.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);

            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    private void updateUserProfile(){
        if (selectedUriList != null) {
            firebaseFirestore.collection("Users")
                .document(auth.getUid())
                .update("image",selectedUriList)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(MyProfileActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
    private void updateNews(){
        boolean news = aSwitch.isChecked();
        reference.update("receiveNews",news)
                .addOnCompleteListener(task -> Toast.makeText(MyProfileActivity.this, "updated", Toast.LENGTH_SHORT).show());
    }
}
