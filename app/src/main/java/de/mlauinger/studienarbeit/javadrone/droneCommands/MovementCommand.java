package de.mlauinger.studienarbeit.javadrone.droneCommands;

import com.codeminders.ardrone.ARDrone;

import java.io.IOException;

public abstract class MovementCommand {
    static float SPEED_RATE = 0.01f;
    static float NEGATIVE_SPEED_RATE = -1 * SPEED_RATE;

    void move(ARDrone drone, float leftRight, float forwardBackward) {
        try {
            drone.move(leftRight, forwardBackward, 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
