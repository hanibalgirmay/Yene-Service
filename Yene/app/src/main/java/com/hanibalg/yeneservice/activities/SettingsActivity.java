package com.hanibalg.yeneservice.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.hanibalg.yeneservice.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            //init
            String locale = null;
            PreferenceManager pm = getPreferenceManager();
            ListPreference lang = (ListPreference) pm.findPreference("lang");
            if(lang.getValue().equals("english")) {
                locale = "en_US";
                Toast.makeText(getActivity(), "english", Toast.LENGTH_SHORT).show();
            } else if(lang.getValue().equals("amharic")){
                locale = "ET";
                Toast.makeText(getActivity(), "amharic", Toast.LENGTH_SHORT).show();
            }else{

            }
        }
    }
}