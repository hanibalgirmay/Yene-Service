package com.hanibalg.yeneservice.pages;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanibalg.yeneservice.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyNotificationFragment extends Fragment {


    public MyNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_notification, container, false);
    }

}
