package com.example.yeneservice.PagesFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yeneservice.Adapters.HomeHorizontalServicesAdapter;
import com.example.yeneservice.Adapters.HomeServiceAdapter;
import com.example.yeneservice.Models.Service;
import com.example.yeneservice.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarFragment extends Fragment {
    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;
    List<Service> lstBook ;
    List<Service> lstHor ;
    HomeServiceAdapter serviceAdapter;
    HomeHorizontalServicesAdapter horizontalServicesAdapter;
    private static final String TAG = "MyActivity";

    public CarFragment() {
        // Required empty public constructor
    }

    public static CarFragment newInstance(String param1, String param2) {
        CarFragment fragment = new CarFragment();
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
        View root = inflater.inflate(R.layout.fragment_car, container, false);

        RecyclerView rv = root.findViewById(R.id.horizontal_hm);
        rv.setHasFixedSize(true);
        lstHor = new ArrayList<>();

        horizontalServicesAdapter = new HomeHorizontalServicesAdapter(getContext(), lstHor);
        rv.setAdapter(horizontalServicesAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Services_List").limit(6).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                assert queryDocumentSnapshots != null;
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String n = doc.getDocument().getString("category");
//                        String ty = doc.getDocument().getString("type").toLowerCase();
                        if(n.equals("car")){
                            String ty = doc.getDocument().getString("type").toLowerCase();
                            if(ty.equals("light")){
                                String service_title = doc.getDocument().getString("name");
                                String img = doc.getDocument().getString("image");

                                Log.d(TAG,"file name: "+ service_title);
                                lstHor.add(new Service(service_title,img));
                                horizontalServicesAdapter.notifyDataSetChanged();
                            }

                        }

                    }
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(horizontalServicesAdapter);

        //vertical main card
        RecyclerView vm = (RecyclerView) root.findViewById(R.id.vertical_vm);
        vm.setHasFixedSize(true);
        lstBook = new ArrayList<>();

        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection("Services_List").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG,"Error: "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String catagory = doc.getDocument().getString("category");

                        if(catagory.equals("car")){
                            String ty = doc.getDocument().getString("type");
//                            if(ty.equals("Basic")){
                                String firstename = doc.getDocument().getString("name");
                                String img = doc.getDocument().getString("image");

                                Log.d(TAG,"file name: "+ firstename);

                                lstBook.add(new Service(firstename,img));
                                serviceAdapter.notifyDataSetChanged();
//                            }

                        }

                    }
                }
            }
        });
        serviceAdapter = new HomeServiceAdapter(getActivity(),lstBook);
        vm.setLayoutManager(new GridLayoutManager(getActivity(),3));
        vm.setAdapter(serviceAdapter);

        return root;
    }


}
