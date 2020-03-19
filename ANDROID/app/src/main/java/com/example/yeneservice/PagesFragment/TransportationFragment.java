package com.example.yeneservice.PagesFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yeneservice.R;

public class TransportationFragment extends Fragment {

    public TransportationFragment() {
        // Required empty public constructor
    }

    public static TransportationFragment newInstance(String param1, String param2) {
        TransportationFragment fragment = new TransportationFragment();
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
        return inflater.inflate(R.layout.fragment_transportation, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
