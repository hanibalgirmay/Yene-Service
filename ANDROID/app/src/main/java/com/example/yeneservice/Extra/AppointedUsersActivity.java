package com.example.yeneservice.Extra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.yeneservice.Adapters.AppointmentUserAdapter;
import com.example.yeneservice.Models.AppointemntUserModel;
import com.example.yeneservice.R;

import java.util.ArrayList;
import java.util.List;

public class AppointedUsersActivity extends AppCompatActivity {
    List<AppointemntUserModel> lstUser;
    private static final String TAG = "MyActivity";
    AppointmentUserAdapter appointmentUserAdapter;
    RecyclerView mn;
    String user_id;
    private String img,ph,fname,lname,email,st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointed_users);

        Toolbar toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointed service provider");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(AppointementUserActivity.this,AppointementUserActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        mn = findViewById(R.id.usr_aa);

        lstUser = new ArrayList<>();
        appointmentUserAdapter = new AppointmentUserAdapter(this,lstUser,false);

        mn.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mn.setLayoutManager(mLayoutManager);

        mn.setAdapter(appointmentUserAdapter);

        lstUser.add(new AppointemntUserModel("Hanibal","Hanibal","3123123",R.drawable.businessman_profile_cartoon_removebg,
                "status",false));
        lstUser.add(new AppointemntUserModel("Hanibal","awet","3123123",R.drawable.businessman_profile_cartoon_removebg,
                "status",false));
        lstUser.add(new AppointemntUserModel("Hanibal","senaay","3123123",R.drawable.businessman_profile_cartoon_removebg,
                "status",false));
        lstUser.add(new AppointemntUserModel("Hanibal","tete","3123123",R.drawable.businessman_profile_cartoon_removebg,
                "status",false));
        lstUser.add(new AppointemntUserModel("Hanibal","feve","3123123",R.drawable.businessman_profile_cartoon_removebg,
                "status",false));


    }
}
