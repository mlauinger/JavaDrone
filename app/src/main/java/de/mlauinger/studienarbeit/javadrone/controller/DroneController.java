package de.mlauinger.studienarbeit.javadrone.controller;


import com.codeminders.ardrone.ARDrone;

import java.io.IOException;
import java.net.InetAddress;

public class DroneController {

    private static ARDrone drone;
    private static final long CONNECT_TIMEOUT = 3000;

    public ARDrone initializeConnection() {
        try {
            //drone = new ARDrone(InetAddress.getByAddress(new byte[]{(byte)-64, (byte)-88, (byte)-78, (byte)-220}), 1000, 1000);
            drone = new ARDrone();
            //drone.connect();
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
        if (null != drone) {
            try {
                drone.clearEmergencySignal();
                drone.trim();
                drone.takeOff();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void emergencyShutdown() {
        if (null != drone) {
            try {
                drone.sendEmergencySignal();
            } catch (IOException e) {
                System.err.println("CANNOT REACH DRONE");
            }
        }
    }

    public void flyDrone(float forward_backward, float left_right) {
        if(null != drone) {
            try {
                drone.move(left_right, forward_backward, 0, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
