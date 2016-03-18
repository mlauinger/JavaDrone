package de.mlauinger.studienarbeit.javadrone.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import de.mlauinger.studienarbeit.javadrone.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsImageFragment extends PreferenceFragment {

    public SettingsImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.image_recognition_settings);
    }
}
