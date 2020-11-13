package com.hanibalg.yeneservice.pages;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.ProvidersAdaptor;
import com.hanibalg.yeneservice.models.ProviderModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
//    private EditText mSearchField;
    private SearchView searchView;
    private ImageView mSearchBtn,btn_filter;
    private RecyclerView mResultList;
    private static final String TAG = "MyActivity";
    ProvidersAdaptor searchAdapter;
    List<ProviderModel> lstProvider ;
    List<UserModel> mUser ;
    private FirebaseFirestore db;
    private BottomSheetBehavior bottomSheetBehavior;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        mSearchField = view.findViewById(R.id.search);
        searchView = view.findViewById(R.id.search);
        btn_filter = view.findViewById(R.id.btnFilter);

        //init
        mResultList = view.findViewById(R.id.search_recycler);
        mResultList.setHasFixedSize(true);
        lstProvider = new ArrayList<>();
        mUser = new ArrayList<>();
        searchAdapter = new ProvidersAdaptor(getContext(), lstProvider,mUser);
        mResultList.setAdapter(searchAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProvider(query);
                mUser.clear();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProvider(newText);
                mUser.clear();
                return false;
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mResultList.setLayoutManager(mLayoutManager);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        mResultList.setAdapter(searchAdapter);

        //filter
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void searchProvider(String newText) {
        db.collection("Users")
                .orderBy("firstName")
                .whereEqualTo("isProvider",true)
                .startAt(newText).endAt(newText + "\uf8ff")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(getActivity(), "no provider found!", Toast.LENGTH_SHORT).show();
                        }else {
                            for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                                final UserModel userModel = doc.getDocument().toObject(UserModel.class);
                                //adaptor
                                mUser.add(userModel);
//                                lstProvider.add(providerModel);
                                searchAdapter.notifyDataSetChanged();
//                                DocumentReference userRef = providerModel.getUserInfo();
//                                userRef.get()
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                UserModel userModel = documentSnapshot.toObject(UserModel.class);
//
//                                                //adaptor
//                                                mUser.add(userModel);
//                                                lstProvider.add(providerModel);
//                                                searchAdapter.notifyDataSetChanged();
//                                            }
//                                        });
                            }
                        }
                    }
                });
    }

}
