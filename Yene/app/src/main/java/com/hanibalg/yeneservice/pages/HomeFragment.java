package com.hanibalg.yeneservice.pages;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.activities.AllCatagoriesActivity;
import com.hanibalg.yeneservice.adaptors.ServiceListAdaptor;
import com.hanibalg.yeneservice.adaptors.ServicesAdaptors;
import com.hanibalg.yeneservice.models.ServiceListModel;
import com.hanibalg.yeneservice.models.servicesModel;
import com.hanibalg.yeneservice.patterns.interfaces.OnDataAdded;
import com.hanibalg.yeneservice.patterns.viewModels.ServiceListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnDataAdded {
    private RecyclerView homeServiceList;
    private RecyclerView services;
    private List<ServiceListModel> slist;
    private List<servicesModel> mService;
    private ServicesAdaptors servicesAdaptors;
    private ServiceListAdaptor myAdaptor;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private ServiceListViewModel serviceListViewModel;
    EditText inputSearch;
    NestedScrollView linearLayout;
    LinearLayout RecommendedcardView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        serviceListViewModel = ViewModelProviders.of(requireActivity()).get(ServiceListViewModel.class);
        serviceListViewModel.init(getActivity());

        //initialization
        RecommendedcardView = view.findViewById(R.id.recommendedId);
        homeServiceList = view.findViewById(R.id.listHome);
        services = view.findViewById(R.id.serviceHor);
        linearLayout = view.findViewById(R.id.mainLayout);
        inputSearch = view.findViewById(R.id.search);
        TextView viewAll = view.findViewById(R.id.viewAll);
        viewAll.setOnClickListener(v -> {
            Intent viewAll1 = new Intent(getActivity(), AllCatagoriesActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(viewAll1);
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            linearLayout.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY != oldScrollY){
//                        inputSearch.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
//                        inputSearch.setBackgroundColor(Color.rgb(255,255,255));
                }
            });
        }

        RecommendedcardView.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Recommended clicked", Toast.LENGTH_SHORT).show();
            FrameLayout frameLayout = getActivity().findViewById(R.id.cont);
            frameLayout.setVisibility(View.VISIBLE);
            TopProvidersFragment topProvidersFragment = new TopProvidersFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cont,topProvidersFragment,"test fragment")
                    .addToBackStack(null)
                    .commit();
        });

        Services();

        //adaptor bind
        homeServiceList.setHasFixedSize(true);
        slist = new ArrayList<>();
        firebaseFirestore.collection("Services_List")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DocumentChange doc: task.getResult().getDocumentChanges()){
                            ServiceListModel serviceListModel = doc.getDocument().toObject(ServiceListModel.class);
                            slist.add(serviceListModel);
                            myAdaptor.notifyDataSetChanged();
                        }
                    }
                });
        myAdaptor = new ServiceListAdaptor(getActivity(),slist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        homeServiceList.setLayoutManager(mLayoutManager);
        homeServiceList.setLayoutManager(new GridLayoutManager(getActivity(),3));
        homeServiceList.setAdapter(myAdaptor);
        super.onViewCreated(view, savedInstanceState);
    }

    private void Services() {
        services.setHasFixedSize(true);
        mService = new ArrayList<>();

        firebaseFirestore.collection("Services")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (DocumentChange doc: task.getResult().getDocumentChanges()){
                            String DocID = doc.getDocument().getId();
                            servicesModel model = doc.getDocument().toObject(servicesModel.class);
                            model.setServiceId(DocID);
                            mService.add(model);
                            servicesAdaptors.notifyDataSetChanged();
                        }
                    }
                });
        servicesAdaptors = new ServicesAdaptors(mService,getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        services.setLayoutManager(mLayoutManager);
//        services.setLayoutManager(new LinearLayoutManager.HORIZONTAL,(getActivity(),3));
        services.setAdapter(servicesAdaptors);
    }

    @Override
    public void added() {
        myAdaptor.notifyDataSetChanged();
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if(id == R.id.logout){
//            signOut();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    //sign out method
//    private void signOut() {
//        mAuth.signOut();
//        Intent lg = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(lg);
//    }
}
