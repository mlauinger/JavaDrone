package de.mlauinger.studienarbeit.javadrone.droneCommands;

import com.codeminders.ardrone.ARDrone;

import java.io.IOException;

public class FlyBackwardCommand extends MovementCommand {
    public FlyBackwardCommand(ARDrone drone) {
        move(drone, 0, SPEED_RATE);
    }
}
