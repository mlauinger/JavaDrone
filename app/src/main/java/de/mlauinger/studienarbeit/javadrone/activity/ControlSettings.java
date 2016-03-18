package de.mlauinger.studienarbeit.javadrone.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import de.mlauinger.studienarbeit.javadrone.R;

public class ControlSettings extends AppCompatActivity {

    private FrameLayout settingsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_control_settings);
        getFragmentManager().beginTransaction()
                .add(R.id.settings_fragment, new SettingsImageFragment())
                .commit();
        settingsContainer = (FrameLayout) findViewById(R.id.settings_fragment);
    }

    public void switchScreen(View view) {
        Intent manualControl = new Intent(this, ControlScreen.class);
        startActivity(manualControl);
    }

    public void showSettings(View view) {
        if (settingsContainer.isShown()) {
            settingsContainer.setVisibility(View.INVISIBLE);
            return;
        }
        settingsContainer.setVisibility(View.VISIBLE);

    }

    public void doEmergency(View view) {
    }

    public void showDroneConfiguration(View view) {
    }
}
