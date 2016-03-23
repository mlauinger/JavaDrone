package de.mlauinger.studienarbeit.javadrone.droneCommands;

import com.codeminders.ardrone.ARDrone;

import java.io.IOException;

public class FlyRightCommand extends MovementCommand {
    public FlyRightCommand(ARDrone drone) {
        move(drone, SPEED_RATE, 0);
    }
}
