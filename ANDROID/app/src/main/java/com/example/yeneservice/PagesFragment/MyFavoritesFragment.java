package com.example.yeneservice.PagesFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.yeneservice.Adapters.AppointmentAdapter;
import com.example.yeneservice.Adapters.FavoriteAdapter;
import com.example.yeneservice.Adapters.NotificationRecyclerViewAdapter;
import com.example.yeneservice.HomeActivity;
import com.example.yeneservice.Models.AppointementModel;
import com.example.yeneservice.Models.NotificationModel;
import com.example.yeneservice.Models.ServicesProvider;
import com.example.yeneservice.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;


public class MyFavoritesFragment extends Fragment {
    private List<ServicesProvider> favList;
    FavoriteAdapter adapter;
    RecyclerView recyclerView;
    NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;
    FirebaseAuth auth;
    private String user_id;
    List<ServicesProvider> lstnot ;
    private static final String TAG = "MyActivity";
    FirebaseFirestore firebaseFirestore;
    LottieAnimationView lottieAnimationView;
    RecyclerView rv;
    View view;

    public MyFavoritesFragment() {
        // Required empty public constructor
    }

    public static MyFavoritesFragment newInstance(String param1, String param2) {
        MyFavoritesFragment fragment = new MyFavoritesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_my_favorites, container, false);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = auth.getCurrentUser().getUid();
//        view = rootview.findViewById(R.id.ty);
        lottieAnimationView = rootView.findViewById(R.id.animation_view);

        rv = rootView.findViewById(R.id.recycler_fav);
        rv.setHasFixedSize(true);

        showFav();

        return rootView;
    }

    private void showFav() {
            favList = new ArrayList<>();
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            if(user_id != null){
                db.collection("Users").document(auth.getUid())
                        .collection("Favorite").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                            if(doc.getType() == DocumentChange.Type.ADDED){
                                final String id = doc.getDocument().getId();
                                final String uuid = doc.getDocument().getString("providerID");

                                if (uuid != null) {
//                                    Toast.makeText(getContext(), ""+uuid, Toast.LENGTH_SHORT).show();
                                    loadUSer(id,uuid);
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

            adapter = new FavoriteAdapter(getContext(), favList);

            rv.setAdapter(adapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(mLayoutManager);
            rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
            rv.setAdapter(adapter);

    }

    private void loadUSer(final String d, final String uuidd) {
        firebaseFirestore.collection("Users").document(uuidd).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    final String fname = task.getResult().getString("firstName");
                    final String lname = task.getResult().getString("lastName");
                    final String image = task.getResult().getString("image");

                    firebaseFirestore.collection("Service_Providers").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                                String working = doc.getDocument().getString("working_area");
                                String about = doc.getDocument().getString("about_me");
                                String uID = doc.getDocument().getString("userID");

                                Float po = 3.3f;
                                if(uID.equals(uuidd)){
                                    favList.add(new ServicesProvider(d,uuidd,fname,lname,working,about,image,po));
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        // Set title bar
//        ((HomeActivity) getActivity())
//                .setActionBarTitle("Favorite");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
