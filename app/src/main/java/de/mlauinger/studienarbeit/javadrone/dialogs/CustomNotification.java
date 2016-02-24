package de.mlauinger.studienarbeit.javadrone.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import de.mlauinger.studienarbeit.javadrone.R;

public class CustomNotification extends DialogFragment {
    public Dialog showNotification(Activity currentActivity, String notificationTitle, String notificationContent) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(currentActivity);
        alertBuilder.setMessage(notificationContent);
        alertBuilder.setTitle(notificationTitle);
        alertBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return alertBuilder.create();
    }

}