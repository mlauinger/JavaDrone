package de.mlauinger.studienarbeit.javadrone.controller;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.DroneVideoListener;

import java.io.IOException;

import de.mlauinger.studienarbeit.javadrone.R;

public class DroneController {

    private static ARDrone drone;

    //public void sendConfigurations(DroneConfigurationController configController) {
        //configController.sendAllPreferencesToDrone(drone);
    //}

    public void setDrone(ARDrone arDrone) {
        drone = arDrone;
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
                System.err.println(R.string.drone_not_reachable);
            }
        }
    }

    public void flyDrone(float forward_backward, float left_right) {
        if (null != drone) {
            try {
                drone.move(left_right, forward_backward, 0, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void performLanding() {
        if (null != drone) {
            try {
                drone.land();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getConfiguration() {
        return drone.readDroneConfiguration();
    }

    public void turnDrone(float left_right) {
        if (null != drone) {
            try {
                drone.move(0, 0, 0, left_right);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addImageListender(DroneVideoListener videoScreen) {
        drone.addImageListener(videoScreen);
    }
}
