package com.hanibalg.yeneservice.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
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
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab0,fab1,fab2;
    private ImageView myProfile;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private int pos = 0;
    private DocumentReference reference;
    private Switch aSwitch;
    Uri selectedUriList = null;
    private TextInputLayout uInputLayout;
    private TextInputEditText uText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //firebase init
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String uId = auth.getCurrentUser().getUid();
        reference = FirebaseFirestore.getInstance().collection("Users").document(uId);
        //FLOATING ICONS!
        fab0 = findViewById(R.id.fab0);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        aSwitch = findViewById(R.id.receveNews);
        myProfile = findViewById(R.id.profile);
        TextView name = findViewById(R.id.fullname);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);

        LinearLayout po = findViewById(R.id.cr);
        uInputLayout = findViewById(R.id.userLayout);
        uText = findViewById(R.id.username);
        po.setOnClickListener(this);

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
                email.setText(userModel.getEmail());
                Picasso.get().load(userModel.getImage()).placeholder(R.drawable.placeholder_profile).into(myProfile);

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
                Intent edit = new Intent(this,EditProfileActivity.class);
                startActivity(edit);
                break;
            case R.id.fab2:
                Toast.makeText(this, "select image", Toast.LENGTH_SHORT).show();
                TedBottomPicker.with(this)
                        .setPeekHeight(350)
                        .showTitle(true)
                        .setCompleteButtonText("Done")
                        .setEmptySelectionText("No Select")
                        .setSelectedUri(selectedUriList)
                        .show(uri -> {
                            Log.d("profileImage",uri.toString());
                            updateUserProfile();
//                            Picasso.get().load(uri).placeholder(R.drawable.placeholder_profile).into(myProfile);
                        });
                break;
            case R.id.receveNews:
                updateNews();
                break;

            case R.id.cr:
                String va = uText.getText().toString();
                editMyProfile("Username",va);
                break;
            default:
                break;
        }
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
