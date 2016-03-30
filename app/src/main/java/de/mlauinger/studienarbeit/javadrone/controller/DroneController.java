package de.mlauinger.studienarbeit.javadrone.controller;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.DroneVideoListener;

import java.io.IOException;

import de.mlauinger.studienarbeit.javadrone.R;
import de.mlauinger.studienarbeit.javadrone.droneCommands.FlyBackwardCommand;
import de.mlauinger.studienarbeit.javadrone.droneCommands.FlyForwardCommand;
import de.mlauinger.studienarbeit.javadrone.droneCommands.FlyLeftCommand;
import de.mlauinger.studienarbeit.javadrone.droneCommands.FlyRightCommand;
import de.mlauinger.studienarbeit.javadrone.droneCommands.TurnLeftCommand;
import de.mlauinger.studienarbeit.javadrone.droneCommands.TurnRightCommand;

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

    public void moveForward() {
        if (null != drone) {
            new FlyForwardCommand(drone);
        }
    }

    public void moveBackward() {
        if (null != drone) {
            new FlyBackwardCommand(drone);
        }
    }

    public void moveLeft() {
        if (null != drone) {
            new FlyLeftCommand(drone);
        }
    }

    public void moveRight() {
        if (null != drone) {
            new FlyRightCommand(drone);
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

    public void turnLeft() {
        if (null != drone) {
            new TurnLeftCommand(drone);
        }
    }

    public void turnRight() {
        if (null != drone) {
            new TurnRightCommand(drone);
        }
    }

    public static void addImageListender(DroneVideoListener videoScreen) {
        drone.addImageListener(videoScreen);
    }

    public void pause() {
        drone.pauseVideo();
        drone.pauseNavData();
    }

    public void resume() {
        drone.resumeVideo();
        drone.resumeNavData();
    }
}
