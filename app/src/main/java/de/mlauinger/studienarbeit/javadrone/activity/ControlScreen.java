package de.mlauinger.studienarbeit.javadrone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import de.mlauinger.studienarbeit.javadrone.R;
import de.mlauinger.studienarbeit.javadrone.controller.DroneConfigurationController;
import de.mlauinger.studienarbeit.javadrone.controller.DroneController;

public class ControlScreen extends AppCompatActivity {

    DroneController droneController;
    Button landing;
    Button takeoff;
    Button emergency;
    Button flyLeft;
    Button flyRight;
    Button flyForward;
    Button flyBackward;
    DroneConfigurationController configController = new DroneConfigurationController(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen);
        droneController = new DroneController();
        droneController.sendConfigurations(configController);
        initializeViewElements();

    }

    private void initializeViewElements() {
        landing = (Button) findViewById(R.id.landing);
        takeoff = (Button) findViewById(R.id.takeoff);
        emergency = (Button) findViewById(R.id.emergency);
        flyLeft = (Button) findViewById(R.id.flyleft);
        flyRight = (Button) findViewById(R.id.flyright);
        flyForward = (Button) findViewById(R.id.flyforward);
        flyBackward = (Button) findViewById(R.id.flybackward);
    }

    public void doTakeOff(View view) {
        droneController.performTakeOff();
        takeoff.setEnabled(false);
        landing.setEnabled(true);
        flyLeft.setEnabled(true);
        flyRight.setEnabled(true);
        flyForward.setEnabled(true);
        flyBackward.setEnabled(true);
    }

    public void doEmergency(View view) {
        droneController.emergencyShutdown();
    }

    public void flyForward(View view) {
        droneController.flyDrone(0.1f, 0);
    }

    public void flyBackward(View view) {
        droneController.flyDrone(-0.1f, 0);
    }

    public void flyLeft(View view) {
        droneController.flyDrone(0, 0.1f);
    }

    public void flyRight(View view) {
        droneController.flyDrone(0, 0.1f);
    }

    public void doLanding(View view) {
        droneController.performLanding();
        takeoff.setEnabled(true);
        landing.setEnabled(false);
        flyLeft.setEnabled(false);
        flyRight.setEnabled(false);
        flyForward.setEnabled(false);
        flyBackward.setEnabled(false);
    }
}
