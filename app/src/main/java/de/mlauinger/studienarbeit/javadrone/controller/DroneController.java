/**
 * Created by marco on 12.11.15.
 */

package de.mlauinger.studienarbeit.javadrone.controller;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.codeminders.ardrone.ARDrone;

import java.io.IOException;

public class DroneController {

    public ARDrone initializeConnection() {
        ARDrone drone;
        try {
            drone = new ARDrone();
            drone.connect();
        } catch (IOException e) {
            System.err.println("NO ARDRONE FOUND!!!");
            drone = null;
        }
        return drone;
    }


}
