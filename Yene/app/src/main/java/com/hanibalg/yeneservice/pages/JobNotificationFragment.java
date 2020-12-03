package com.hanibalg.yeneservice.pages;


import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.NotificationAdaptor;
import com.hanibalg.yeneservice.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobNotificationFragment extends Fragment {
    private List<NotificationModel>models;
    private NotificationAdaptor adaptor;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;

    public JobNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_job_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        models = new ArrayList<>();
        //init recyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyc);
        recyclerView.setHasFixedSize(true);

        if (auth.getUid() != null){
            firebaseFirestore.collection("Users")
                    .document(auth.getUid())
                    .collection("Notifications")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                            String document = doc.getDocument().getId();
                            NotificationModel notificationModel = doc.getDocument().toObject(NotificationModel.class);
                            notificationModel.setDocId(document);
                            models.add(notificationModel);
                            adaptor.notifyDataSetChanged();
                        }
                    });
            adaptor = new NotificationAdaptor(getContext(),models);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(adaptor);

        } else{
            //Notification will appear here
        }

    }
}
