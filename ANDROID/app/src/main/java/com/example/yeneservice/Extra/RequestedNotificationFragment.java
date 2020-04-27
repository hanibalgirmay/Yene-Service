package com.example.yeneservice.Extra;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yeneservice.Adapters.NotificationRecyclerViewAdapter;
import com.example.yeneservice.Models.NotificationModel;
import com.example.yeneservice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestedNotificationFragment extends Fragment {
    private List<NotificationModel> notificationList;
    RecyclerView recyclerView;
    NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;
    FirebaseAuth auth;
    String User_id;
    private static final String TAG = "NotificationActivity";
    FirebaseFirestore firebaseFirestore;

    public RequestedNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requested_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        User_id = auth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getActivity().getIntent();
        String id = intent.getExtras().getString("userID");
//        reference = FirebaseFirestore.getInstance().collection("Users").document(User_id);

        notificationList = new ArrayList<>();
        notificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter(getContext(),notificationList);
        RecyclerView myrv = view.findViewById(R.id.recycleView);
        myrv.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myrv.setLayoutManager(mLayoutManager);
        myrv.setAdapter(notificationRecyclerViewAdapter);

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String current_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Notifications").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    String id = doc.getDocument().getId();
                    NotificationModel notification = doc.getDocument().toObject(NotificationModel.class);
                    notification.setDocID(id);

                    if (notification.getFrom().equals(auth.getUid())){
                        notificationList.add(notification);
                        Log.d(TAG,"notfication: "+ notification.getDocID());
//                        lstnot.add(new Notification(id,firstename));
                        notificationRecyclerViewAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getContext(), "there is not notification for you", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}
