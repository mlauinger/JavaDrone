package de.mlauinger.studienarbeit.javadrone.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import de.mlauinger.studienarbeit.javadrone.R;

public class CustomErrorDialog extends DialogFragment {
    public Dialog showConnectionError(Activity currentActivity) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(currentActivity);
        alertBuilder.setMessage(R.string.drone_not_reachable);
        alertBuilder.setTitle(R.string.connection_error);
        alertBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return alertBuilder.create();
    }

}
