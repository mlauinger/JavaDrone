package de.mlauinger.studienarbeit.javadrone.activity;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codeminders.ardrone.ARDrone;

import java.io.IOException;
import java.net.UnknownHostException;

import de.mlauinger.studienarbeit.javadrone.R;
import de.mlauinger.studienarbeit.javadrone.controller.DroneController;
import de.mlauinger.studienarbeit.javadrone.dialogs.CustomErrorDialog;

public class StartScreen extends AppCompatActivity {

    WifiManager connManager;
    DroneController droneCon;
    Dialog errorDialog;

    public StartScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        connManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        droneCon = new DroneController();
        errorDialog = new CustomErrorDialog().showConnectionError(this);
    }

    private Builder turnOnWiFiDialog;

    public void doConnect(View view) {
        if (connManager.isWifiEnabled()) {
            try {
                (new DroneConnector()).execute(new ARDrone());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            turnOnWiFiDialog.show();
        }
    }

    private void processToControl() {
        Intent startControls = new Intent(this, ControlScreen.class);
        startActivity(startControls);
    }

    public void openSettings(View view) {
        Intent openSettings = new Intent(this, SettingsScreen.class);
        startActivity(openSettings);
    }

    private class DroneConnector extends AsyncTask<ARDrone, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(ARDrone... params) {
            ARDrone drone = params[0];
            try {
                drone.connect();
                drone.waitForReady(3000);
                drone.playLED(1, 10, 4);
                drone.clearEmergencySignal();
                droneCon.setDrone(drone);
                return true;
            } catch (IOException e) {
                System.err.println(R.string.drone_not_in_reach);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            if (isConnected) {
                processToControl();
                return;
            }
            System.err.println(R.string.drone_not_reachable);
            errorDialog.show();
        }
    }
}
