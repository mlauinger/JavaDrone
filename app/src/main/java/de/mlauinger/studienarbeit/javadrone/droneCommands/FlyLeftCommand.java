package de.mlauinger.studienarbeit.javadrone.droneCommands;

import com.codeminders.ardrone.ARDrone;

import java.io.IOException;

public class FlyLeftCommand extends MovementCommand {
    public FlyLeftCommand(ARDrone drone) {
        move(drone, NEGATIVE_SPEED_RATE, 0);
    }
}
