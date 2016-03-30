package de.mlauinger.studienarbeit.javadrone.droneCommands;

import com.codeminders.ardrone.ARDrone;

public class TurnLeftCommand extends MovementCommand {
    public TurnLeftCommand(ARDrone arDrone) {
        turn(arDrone, NEGATIVE_SPEED_RATE);
    }
}
