package com.example.yeneservice.PagesFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.yeneservice.Adapters.AppointmentAdapter;
import com.example.yeneservice.Models.AppointementModel;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class AppointementFragment extends Fragment {
    private static final String TAG = "MyActivity";
    List<AppointementModel> lstBook ;
    AppointmentAdapter adapter;
    private FirebaseAuth auth;
    private String user_id;
    private ImageView Sort;
    SharedPreferences pref;
    RecyclerView rv;
    View view;
    AppointementModel appointementModel;
    private Drawable icon;
    private ColorDrawable background;
    LottieAnimationView lottieAnimationView;

    public AppointementFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppointementFragment newInstance(String param1, String param2) {
        AppointementFragment fragment = new AppointementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_appointement, container, false);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        view = rootview.findViewById(R.id.ty);
        lottieAnimationView = rootview.findViewById(R.id.animation_view);
        pref = getActivity().getSharedPreferences("MY_Data", MODE_PRIVATE);

        Sort = rootview.findViewById(R.id.sort);
        Sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                getSortedData();
            }
        });
        rv = rootview.findViewById(R.id.recycler_app);
        rv.setHasFixedSize(true);

        showData();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                rv.setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));
                adapter.removeItem(viewHolder.getAdapterPosition());
//                icon = ContextCompat.getDrawable(adapter.getContext(),
//                        R.drawable.ic_delete_black_24dp);
                background = new ColorDrawable(Color.RED);
                showUndoSnackbar();
            }
        }).attachToRecyclerView(rv);

        return rootview;
    }

    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
//        snackbar.setAction(R.string.snack_bar_undo);
        snackbar.show();
    }

    private void showData() {
        lstBook = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(user_id != null){
            db.collection("Appointments").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e != null){
                        Log.d(TAG,"Error: "+ e.getMessage());
                    }
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            final String id = doc.getDocument().getId();
                            final String uid = doc.getDocument().getString("appointedID");
                            final String serviceId = doc.getDocument().getString("service_provider_id");
                            final String desc = doc.getDocument().getString("description");
                            final String date = doc.getDocument().getString("date");
                            final String time = doc.getDocument().getString("time");
                            final Timestamp timestamps = doc.getDocument().getTimestamp("timestamp");

                            if(user_id.equals(uid)){
                                lottieAnimationView.setVisibility(View.GONE);
                                Log.d(TAG,"appoint data of:  "+ serviceId);
                                FirebaseFirestore dba = FirebaseFirestore.getInstance();
                                if (serviceId != null) {
                                    assert serviceId != null;
                                    dba.collection("Users").document(serviceId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
//                                        String docid = task.;
                                                String fname = task.getResult().getString("firstName");
                                                String image = task.getResult().getString("image");

                                                lstBook.add(new AppointementModel(fname,image,uid,serviceId,desc,date,time,id,timestamps));
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getActivity(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                } else {
//                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Good jobError happen!")
//                                        .setContentText("try later! !")
//                                        .show();
                                }

                            } else {
                                lottieAnimationView.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }
            });
        } else {
            lottieAnimationView.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "You are not registered", Toast.LENGTH_SHORT).show();
        }
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2011","12:54","123123"));
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2012","12:54","123123"));
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2013","12:54","123123"));
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2014","12:54","123123"));
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2015","12:54","123123"));
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2016","12:54","123123"));
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2017","12:54","123123"));
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2018","12:54","123123"));
//        lstBook.add(new AppointementModel("full name",R.drawable.businessman_profile_cartoon_removebg,
//                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec",
//                "12/3/2019","12:54","123123"));

        String mSortSettings = pref.getString("Sort", "ascending");
        if(mSortSettings.equals("ascending")){
            Toast.makeText(getContext(), ""+AppointementModel.BY_DATE_ASCENDING, Toast.LENGTH_SHORT).show();
            Collections.sort(lstBook, AppointementModel.BY_DATE_ASCENDING);
        } else if(mSortSettings.equals("descending")){
            Collections.sort(lstBook, AppointementModel.BY_DATE_DESCENDING);
        }
        adapter = new AppointmentAdapter(getContext(), lstBook);

        rv.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
    }

    private void getSortedData() {
        String[] options = {"Ascending","Descending"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sort By");
        builder.setIcon(R.drawable.ic_sort_black_24dp);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    Toast.makeText(getContext(), "Ascending clicked", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Sort", "ascending");
                    editor.apply();
                    showData();
                } else {
                    Toast.makeText(getContext(), "Descending clicked", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Sort", "descending");
                    editor.apply();
                    showData();
                }
            }
        });
        builder.create().show();
    }

}
