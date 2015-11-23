package de.mlauinger.studienarbeit.javadrone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import de.mlauinger.studienarbeit.javadrone.R;
import de.mlauinger.studienarbeit.javadrone.controller.DroneController;

public class ControlScreen extends AppCompatActivity {

    DroneController droneController;
    Button landing;
    Button takeoff;
    Button emergency;
    Button flyleft;
    Button flyright;
    Button flyrforward;
    Button flyrbackward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen);
        droneController = new DroneController();
        landing = (Button) findViewById(R.id.landing);
        takeoff = (Button) findViewById(R.id.takeoff);
        emergency = (Button) findViewById(R.id.emergency);
        flyleft = (Button) findViewById(R.id.flyleft);
        flyright = (Button) findViewById(R.id.flyright);
        flyrforward = (Button) findViewById(R.id.flyforward);
        flyrbackward = (Button) findViewById(R.id.flybackward);
    }

    public void doTakeOff(View view) {
        droneController.performTakeOff();
        takeoff.setEnabled(false);
        landing.setEnabled(true);
        flyleft.setEnabled(true);
        flyright.setEnabled(true);
        flyrforward.setEnabled(true);
        flyrbackward.setEnabled(true);
    }

    public void doEmergency(View view) {
        droneController.emergencyShutdown();
    }

    public void flyForward(View view) {
        droneController.flyDrone(1.0f, 0);
    }

    public void flyBackward(View view) {
        droneController.flyDrone(-1.0f, 0);
    }

    public void flyLeft(View view) {
        droneController.flyDrone(0, 1.0f);
    }

    public void flyRight(View view) {
        droneController.flyDrone(0, -1.0f);
    }
}
