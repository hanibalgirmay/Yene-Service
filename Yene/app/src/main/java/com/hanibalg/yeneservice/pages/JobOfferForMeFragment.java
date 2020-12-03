<<<<<<< HEAD
package com.hanibalg.yeneservice.pages;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.AppointedAdaptor;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobOfferForMeFragment extends Fragment {

    private List<AppointmentJobModel> model;
    private List<UserModel> mUser;
    private AppointedAdaptor adaptor;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private LottieAnimationView lottieAnimationView;
    private LinearLayout cardView;

    public JobOfferForMeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_job_offer_for_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lottieAnimationView = view.findViewById(R.id.animationView);
        cardView = view.findViewById(R.id.card_not_job);

        RecyclerView recyclerV = view.findViewById(R.id.my_recycler_view);
        recyclerV.setHasFixedSize(true);
        model = new ArrayList<>();
        mUser = new ArrayList<>();
        getMyJob(recyclerV);
    }

    private void getMyJob(RecyclerView recyclerV) {
        firebaseFirestore.collection("JobsRequests")
                .whereEqualTo("accepted",false)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                        AppointmentJobModel appointmentJobModel = doc.getDocument().toObject(AppointmentJobModel.class);
                        String id = doc.getDocument().getId();
                        appointmentJobModel.setDocID(id);
                        if (appointmentJobModel.getService_provider_id().equals(auth.getUid())){
                            cardView.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            getProviderDate(appointmentJobModel);
                            Log.d("requests",doc.getDocument().getData().toString());
                        }else{
                            //show cardView not job
                            cardView.setVisibility(View.VISIBLE);
                            lottieAnimationView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        adaptor = new AppointedAdaptor(getActivity(),model,mUser);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerV.setLayoutManager(mLayoutManager);
        recyclerV.setAdapter(adaptor);
    }

    private void getProviderDate(AppointmentJobModel appointmentJobModel) {
        firebaseFirestore.collection("Users").document(appointmentJobModel.getJobAppointedUserID())
                .get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                model.add(appointmentJobModel);
                mUser.add(userModel);
                Log.d("Information-provider",documentSnapshot.getData().toString());
                adaptor.notifyDataSetChanged();
            }
        });
    }
}
=======
package com.hanibalg.yeneservice.pages;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.AppointedAdaptor;
import com.hanibalg.yeneservice.models.AppointmentJobModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobOfferForMeFragment extends Fragment {

    private List<AppointmentJobModel> model;
    private List<UserModel> mUser;
    private AppointedAdaptor adaptor;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private LottieAnimationView lottieAnimationView;
    private LinearLayout cardView;

    public JobOfferForMeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return inflater.inflate(R.layout.fragment_job_offer_for_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lottieAnimationView = view.findViewById(R.id.animationView);
        cardView = view.findViewById(R.id.card_not_job);

        RecyclerView recyclerV = view.findViewById(R.id.my_recycler_view);
        recyclerV.setHasFixedSize(true);
        model = new ArrayList<>();
        mUser = new ArrayList<>();
        getMyJob(recyclerV);
    }

    private void getMyJob(RecyclerView recyclerV) {
        firebaseFirestore.collection("JobsRequests")
                .whereEqualTo("accepted",false)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                        AppointmentJobModel appointmentJobModel = doc.getDocument().toObject(AppointmentJobModel.class);
                        String id = doc.getDocument().getId();
                        appointmentJobModel.setDocID(id);
                        if (appointmentJobModel.getService_provider_id().equals(auth.getUid())){
                            cardView.setVisibility(View.GONE);
                            lottieAnimationView.setVisibility(View.GONE);
                            getProviderDate(appointmentJobModel);
                            Log.d("requests",doc.getDocument().getData().toString());
                        }else{
                            //show cardView not job
                            cardView.setVisibility(View.VISIBLE);
                            lottieAnimationView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        adaptor = new AppointedAdaptor(getActivity(),model,mUser);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerV.setLayoutManager(mLayoutManager);
        recyclerV.setAdapter(adaptor);
    }

    private void getProviderDate(AppointmentJobModel appointmentJobModel) {
        firebaseFirestore.collection("Users").document(appointmentJobModel.getJobAppointedUserID())
                .get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                model.add(appointmentJobModel);
                mUser.add(userModel);
                Log.d("Information-provider",documentSnapshot.getData().toString());
                adaptor.notifyDataSetChanged();
            }
        });
    }
}
>>>>>>> 38ad388af22ba498a9929d1337ddd1faf64803bc
