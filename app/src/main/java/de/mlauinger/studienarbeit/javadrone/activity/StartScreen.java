package de.mlauinger.studienarbeit.javadrone.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codeminders.ardrone.ARDrone;

import de.mlauinger.studienarbeit.javadrone.R;
import de.mlauinger.studienarbeit.javadrone.controller.DroneController;

public class StartScreen extends AppCompatActivity {

    WifiManager connManager;
    DroneController dronCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        connManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        dronCon = new DroneController();
    }

    private Builder turnOnWiFiDialog;

    public void doConnect(View view) {
        if(connManager.isWifiEnabled()) {
            dronCon.initializeConnection();
            Intent startControls = new Intent(this, ControlScreen.class);
            startActivity(startControls);
        } else {
            turnOnWiFiDialog.show();
        }

    }
}
