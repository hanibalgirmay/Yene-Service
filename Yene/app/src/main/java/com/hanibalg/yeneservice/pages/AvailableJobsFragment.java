package com.hanibalg.yeneservice.pages;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.JobAvailableOnMapActivity;
import com.hanibalg.yeneservice.adaptors.PublicJobListAdaptor;
import com.hanibalg.yeneservice.models.JobListPublic;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableJobsFragment extends Fragment implements View.OnClickListener {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private PublicJobListAdaptor adaptor;
    private RecyclerView recyclerView;
    private List<JobListPublic> model;
    private List<UserModel> user;

    public AvailableJobsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available_jobs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init fireBase
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //var
        FloatingActionButton fab = view.findViewById(R.id.mapAvailableJob);
        fab.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.job_recyclerView);
        recyclerView.setHasFixedSize(true);
        model = new ArrayList<>();
        user = new ArrayList<>();
        getJob();
    }

    private void getJob() {
        firebaseFirestore.collection("Jobs")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                        JobListPublic jobListPublic = doc.getDocument().toObject(JobListPublic.class);
                        String docId = doc.getDocument().getId();
                        jobListPublic.setJobId(docId);
                        getPostedUserdata(jobListPublic);
//                        model.add(jobListPublic);
//                        adaptor.notifyDataSetChanged();
                    }
                });
        adaptor = new PublicJobListAdaptor(model,user,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adaptor);
    }

    private void getPostedUserdata(JobListPublic jobListPublic) {
        firebaseFirestore.collection("Users")
                .document(jobListPublic.getUserID())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isComplete()){
                        UserModel userModel = task.getResult().toObject(UserModel.class);
                        user.add(userModel);
                        model.add(jobListPublic);
                        adaptor.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "error, something is not right!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.mapAvailableJob){
            Intent mp = new Intent(getActivity(), JobAvailableOnMapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mp);
        }
    }
}
