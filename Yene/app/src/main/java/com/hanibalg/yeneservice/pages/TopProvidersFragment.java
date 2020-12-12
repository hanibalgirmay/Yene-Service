package com.hanibalg.yeneservice.pages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ProvidersAdaptor;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopProvidersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopProvidersFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private ProvidersAdaptor adaptor;
    private List<ProviderModel> providerModels;
    private List<UserModel> mUser;

    private String TAG = TopProvidersFragment.class.getName();

    public TopProvidersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TopProvidersFragment newInstance(String param1, String param2) {
        TopProvidersFragment fragment = new TopProvidersFragment();
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
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_top_providers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.topProviders);
        recyclerView.setHasFixedSize(true);
        providerModels = new ArrayList<>();
        mUser = new ArrayList<>();
        recyclerView.setAdapter(adaptor);
        topProviders();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        adaptor = new ProvidersAdaptor(getActivity(),providerModels,mUser);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptor);
    }

    private void topProviders() {
        firebaseFirestore.collection("Service_Providers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                                ProviderModel topProvider = doc.getDocument().toObject(ProviderModel.class);
                                //filter out this user if he/she is provider
                                if(!topProvider.getUser_id().equals(auth.getUid())){
                                    //get user info
                                    firebaseFirestore.collection("Users")
                                            .document(topProvider.getUser_id())
                                            .get()
                                            .addOnCompleteListener(task -> {
                                                if(task.isSuccessful()){
                                                    UserModel userMode = task.getResult().toObject(UserModel.class);
                                                    String id = task.getResult().getId();
                                                    userMode.setUserId(id);
                                                    Log.d("search-item",id);
                                                    providerModels.add(topProvider);
                                                    mUser.add(userMode);
                                                    adaptor.notifyDataSetChanged();
                                                }
                                            });
                                }
                                providerModels.add(topProvider);
                            }
                        }
                    }
                }).addOnFailureListener(e -> {
                    e.getStackTrace();
                    Log.d(TAG,"onError"+e);
                });
    }
}