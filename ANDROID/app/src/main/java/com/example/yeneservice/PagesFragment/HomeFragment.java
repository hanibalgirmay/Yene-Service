package com.example.yeneservice.PagesFragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yeneservice.Adapters.HomeServiceAdapter;
import com.example.yeneservice.Adapters.ViewPageAdapter;
import com.example.yeneservice.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "Services";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    HomeServiceAdapter serviceAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View viewroot = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = viewroot.findViewById(R.id.tablayout);
        viewPager = viewroot.findViewById(R.id.viewpagerId);

//        tabLayout.getTabAt(0).
        HouseFragment frag1 = new HouseFragment();
        TransportationFragment frag2 = new TransportationFragment();
        CarFragment frag3 = new CarFragment();

        Bundle bundle1,bundle2;
        bundle1 = new Bundle();
//        bundle1.putString("provider",provider); //pass the data you want to pass

        bundle2 = new Bundle();
//        bundle2.putString("key",frag);

        frag1.setArguments(bundle1);//set arguments require a bundle
        frag2.setArguments(bundle2);//set arguments require a bundle

        ViewPageAdapter myadapter = new ViewPageAdapter(getChildFragmentManager());
//        add fragment
        myadapter.addFragment(frag1, "House");
        myadapter.addFragment(frag2, "Transportation");
        myadapter.addFragment(frag3, "Car");
//        setup adapter
        viewPager.setAdapter(myadapter);
        tabLayout.setupWithViewPager(viewPager);
        return viewroot;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

}
