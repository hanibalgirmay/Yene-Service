package com.hanibalg.yeneservice.pages;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.adaptors.FavoriteAdaptor;
import com.hanibalg.yeneservice.models.LocationsModel;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment {

    private List<UserModel> favList;
    private List<UserModel> UList;
    private List<ServiceListModel> SList;
    UserModel userModel;
    ServiceListModel serviceListModel;
    LocationsModel locationModel;
    DocumentReference reference,reference2;
    FavoriteAdaptor adapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;

    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Get Firebase auth instance
        reference = FirebaseFirestore.getInstance().collection("Users").document("asdasd a");
        reference2 = FirebaseFirestore.getInstance().collection("Locations").document("asdasd a");
        /*
        Lotti animation to be addded
         */
        //lottieAnimationView = rootView.findViewById(R.id.animation_view);

        recyclerView = view.findViewById(R.id.recycler_fav);
        recyclerView.setHasFixedSize(true);

        showFav();

        super.onViewCreated(view, savedInstanceState);
    }

    private void showFav() {
        favList = new ArrayList<>();
        UList = new ArrayList<>();

        firebaseFirestore.collection("Users").document(auth.getUid())
                .collection("Favorite")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                        String provider = doc.getDocument().getString("documentID");
                        getProfile(provider);
                    }
                });
//        favList.add(new ProviderModel("1",reference,t,"asdasd","dfgdfg","male",4,"42654654",reference2,"rteret","inidividaul","city","iducation",t));

        adapter = new FavoriteAdaptor(getContext(), favList);

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(adapter);
    }

    private void getProfile(String provider) {
        firebaseFirestore.collection("Users").document(provider)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    favList.add(userModel);
                    adapter.notifyDataSetChanged();
                });
    }
}