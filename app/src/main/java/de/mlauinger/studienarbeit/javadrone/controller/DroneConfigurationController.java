package de.mlauinger.studienarbeit.javadrone.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.codeminders.ardrone.ARDrone;

import java.io.IOException;

public final class DroneConfigurationController implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences storedPrefs;

    private static float verticalSpeedPrefValue;
    private static int maxAltitudePrefValue;
    private static float tiltPrefValue;
    private static float rotationSpeedPrefValue;

    public static String DRONE_MAX_YAW_PARAM_NAME = "control:control_yaw";
    public static String DRONE_MAX_VERT_SPEED_PARAM_NAME = "control:control_vz_max";
    public static String DRONE_MAX_EULA_ANGLE = "control:euler_angle_max";
    public static String DRONE_MAX_ALTITUDE = "control:altitude_max";

    public DroneConfigurationController(Context context) {
        //storedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //storedPrefs.registerOnSharedPreferenceChangeListener(this);
        System.out.println("Controller created");
        //loadAllPreferences();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("vertical_speed")) {
            loadVerticalSpeedValue();
        } else if (key.equals("maximal_altitude")) {
            loadMaxAltitudeValue();
        } else if (key.equals("tilt")) {
            loadTiltValue();
        } else if (key.equals("rotation_speed")) {
            loadRotationSpeedValue();
        }
    }

    public void sendAllPreferencesToDrone(ARDrone drone) {
        try {
            drone.setConfigOption(DRONE_MAX_ALTITUDE, String.valueOf(maxAltitudePrefValue));
            drone.setConfigOption(DRONE_MAX_VERT_SPEED_PARAM_NAME, String.valueOf(verticalSpeedPrefValue));
            drone.setConfigOption(DRONE_MAX_EULA_ANGLE, String.valueOf(tiltPrefValue));
            drone.setConfigOption(DRONE_MAX_YAW_PARAM_NAME, String.valueOf(rotationSpeedPrefValue));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllPreferences() {
        loadVerticalSpeedValue();
        loadMaxAltitudeValue();
        loadTiltValue();
        loadRotationSpeedValue();
    }

    private void loadVerticalSpeedValue() {
        verticalSpeedPrefValue = (float) storedPrefs.getInt("vertical_speed", 1);
    }

    private void loadMaxAltitudeValue() {
        maxAltitudePrefValue = storedPrefs.getInt("maximal_altitude", 3);
    }

    private void loadTiltValue() {
        tiltPrefValue = (float) storedPrefs.getInt("tilt", 5);
    }

    private void loadRotationSpeedValue() {
        rotationSpeedPrefValue = (float) storedPrefs.getInt("rotation_speed", 50);
    }
}
