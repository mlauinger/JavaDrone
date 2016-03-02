package de.mlauinger.studienarbeit.javadrone.activity;

import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.mlauinger.studienarbeit.javadrone.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsDroneFragment extends PreferenceFragment {

    public SettingsDroneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.drone_settings);
    }
}
