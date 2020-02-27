package com.example.yeneservice.PagesFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yeneservice.Adapters.HomeHorizontalServicesAdapter;
import com.example.yeneservice.Adapters.HomeServiceAdapter;
import com.example.yeneservice.Models.Service;
import com.example.yeneservice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    List<Service> lstBook ;
    List<Service> lstHor ;
    HomeServiceAdapter serviceAdapter;
    HomeHorizontalServicesAdapter horizontalServicesAdapter;
    private static final String TAG = "MyActivity";
    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.horizontal_hm);
        rv.setHasFixedSize(true);
        lstHor = new ArrayList<>();

        horizontalServicesAdapter = new HomeHorizontalServicesAdapter(getContext(), lstHor);
        rv.setAdapter(horizontalServicesAdapter);

        lstHor.add(new Service("one",R.drawable.businessman_profile_cartoon_removebg));
        lstHor.add(new Service("two",R.drawable.businessman_profile_cartoon_removebg));
        lstHor.add(new Service("three",R.drawable.businessman_profile_cartoon_removebg));
        lstHor.add(new Service("one",R.drawable.businessman_profile_cartoon_removebg));
        lstHor.add(new Service("six",R.drawable.businessman_profile_cartoon_removebg));
        lstHor.add(new Service("five",R.drawable.businessman_profile_cartoon_removebg));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(horizontalServicesAdapter);

        //vertical main card
        RecyclerView vm = (RecyclerView) rootView.findViewById(R.id.vertical_vm);
        vm.setHasFixedSize(true);
        lstBook = new ArrayList<>();

        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        lstBook.add(new Service("title",R.drawable.businessman_profile_cartoon_removebg));
        serviceAdapter = new HomeServiceAdapter(getActivity(),lstBook);
        vm.setLayoutManager(new GridLayoutManager(getActivity(),3));
        vm.setAdapter(serviceAdapter);
        return rootView;
    }

}
