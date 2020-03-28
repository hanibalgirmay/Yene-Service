package com.example.yeneservice.PagesFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Use the {@link HouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseFragment extends Fragment {
    List<Service> lstBook ;
    List<Service> lstHor ;
    HomeServiceAdapter serviceAdapter;
    HomeHorizontalServicesAdapter horizontalServicesAdapter;
    private static final String TAG = "MyActivity";
    public HouseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HouseFragment newInstance(String param1, String param2) {
        HouseFragment fragment = new HouseFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_house, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.horizontal_hm);
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
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
//                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String n = doc.getDocument().getString("category");
//                        String ty = doc.getDocument().getString("type").toLowerCase();
                        if(n.equals("home")){
                            String firstename = doc.getDocument().getString("name");
                            String img = doc.getDocument().getString("image");

                            Log.d(TAG,"file name: "+ firstename);
                            lstHor.add(new Service(firstename,img));
                            horizontalServicesAdapter.notifyDataSetChanged();
                        }

                        // stop animating Shimmer and hide the layout
//                        hShimmerViewContainer.stopShimmerAnimation();
//                        hShimmerViewContainer.setVisibility(View.GONE);
//                    }
                }
            }
        });
//        lstHor.add(new Service("one",R.drawable.businessman_profile_cartoon_removebg));
//        lstHor.add(new Service("two",R.drawable.businessman_profile_cartoon_removebg));
//        lstHor.add(new Service("three",R.drawable.businessman_profile_cartoon_removebg));
//        lstHor.add(new Service("one",R.drawable.businessman_profile_cartoon_removebg));
//        lstHor.add(new Service("six",R.drawable.businessman_profile_cartoon_removebg));
//        lstHor.add(new Service("five",R.drawable.businessman_profile_cartoon_removebg));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(horizontalServicesAdapter);

        //vertical main card
        RecyclerView vm = (RecyclerView) rootView.findViewById(R.id.vertical_vm);
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
                        String n = doc.getDocument().getString("category");
//                        String ty = doc.getDocument().getString("type").toLowerCase();
                        if(n.equals("home")){
                            String firstename = doc.getDocument().getString("name");
                            String img = doc.getDocument().getString("image");

                            Log.d(TAG,"file name: "+ firstename);

                            lstBook.add(new Service(firstename,img));
                            serviceAdapter.notifyDataSetChanged();
                        }

                        // stop animating Shimmer and hide the layout
//                        mShimmerViewContainer.stopShimmerAnimation();
//                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                }
            }
        });
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
//        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        serviceAdapter = new HomeServiceAdapter(getActivity(),lstBook);
        vm.setLayoutManager(new GridLayoutManager(getActivity(),3));
        vm.setAdapter(serviceAdapter);
        return rootView;
    }

}
