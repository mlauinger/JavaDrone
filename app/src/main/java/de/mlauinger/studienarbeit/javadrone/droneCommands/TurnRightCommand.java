package de.mlauinger.studienarbeit.javadrone.droneCommands;

import com.codeminders.ardrone.ARDrone;

public class TurnRightCommand extends MovementCommand {
    public TurnRightCommand(ARDrone arDrone) {
        turn(arDrone, SPEED_RATE);
    }
}
