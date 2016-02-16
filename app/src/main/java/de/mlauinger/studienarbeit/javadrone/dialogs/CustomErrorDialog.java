package de.mlauinger.studienarbeit.javadrone.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

public class CustomErrorDialog extends DialogFragment {
    public Dialog showConnectionError(Activity currentActivity) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(currentActivity);
        alertBuilder.setMessage("Could not reach the Drone");
        alertBuilder.setTitle("ConncetionError");
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return alertBuilder.create();
    }

}
