package de.mlauinger.studienarbeit.javadrone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.mlauinger.studienarbeit.javadrone.R;

public class ControlSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_control_settings);
    }

    public void switchScreen(View view) {
    }

    public void showSettings(View view) {
        getFragmentManager().beginTransaction()
                .add(R.id.settings_fragment, new SettingsImageFragment())
                .commit();
    }
}
