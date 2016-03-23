package de.mlauinger.studienarbeit.javadrone.droneCommands;

import com.codeminders.ardrone.ARDrone;

import java.io.IOException;

public class FlyForwardCommand extends MovementCommand {
    public FlyForwardCommand(ARDrone drone) {
        move(drone, 0, NEGATIVE_SPEED_RATE);
    }
}
