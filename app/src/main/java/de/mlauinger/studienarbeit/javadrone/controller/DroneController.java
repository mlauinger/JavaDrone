package de.mlauinger.studienarbeit.javadrone.controller;


import com.codeminders.ardrone.ARDrone;

import java.io.IOException;

public class DroneController {

    private static ARDrone drone;
    private static final long CONNECT_TIMEOUT = 3000;

    public ARDrone initializeConnection() {
        try {
            drone = new ARDrone();
            drone.connect();
            drone.clearEmergencySignal();

            // Wait until drone is ready
            drone.waitForReady(CONNECT_TIMEOUT);
        } catch (IOException e) {
            System.err.println("NO ARDRONE FOUND!!!");
            drone = null;
        }
        return drone;
    }

    public void performTakeOff() {
        try {
            drone.trim();
            drone.takeOff();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
