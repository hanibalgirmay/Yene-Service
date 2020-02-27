package com.example.yeneservice.PagesFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yeneservice.Adapters.AppointmentAdapter;
import com.example.yeneservice.Models.AppointementModel;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AppointementFragment extends Fragment {
    private static final String TAG = "MyActivity";
    List<AppointementModel> lstBook ;
    AppointmentAdapter adapter;
    private FirebaseAuth auth;
    private String user_id;

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
//        auth = FirebaseAuth.getInstance();
//        user_id = auth.getCurrentUser().getUid();

        RecyclerView rv = rootview.findViewById(R.id.recycler_app);
        rv.setHasFixedSize(true);
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

                            if(user_id.equals(uid)){
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

                                                lstBook.add(new AppointementModel(fname,image,uid,serviceId,desc,date,time,id));
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

                            }

                        }
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "You are not registered", Toast.LENGTH_SHORT).show();
        }

        adapter = new AppointmentAdapter(getContext(), lstBook);

        rv.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        return rootview;
    }

}
