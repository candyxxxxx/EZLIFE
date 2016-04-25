package com.ee5415.group2.ezlife;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shushu on 4/7/16.
 */
public class UserSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
    }

    // Fragment that displays the username preference
    public static class UserPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
// Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings);
        }
    }
}
