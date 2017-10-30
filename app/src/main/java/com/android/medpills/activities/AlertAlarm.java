package com.android.medpills.activities;




import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.WindowManager.LayoutParams;

import com.android.medpills.R;

public class AlertAlarm extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

      // Turn Screen On and Unlock the keypad when this alert dialog is displayed
        getActivity().getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON | LayoutParams.FLAG_DISMISS_KEYGUARD);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.myDialog));
        builder.setTitle("MedPills");
        setCancelable(false);

        final String pillName = getActivity().getIntent().getStringExtra("pill_name");
        builder.setMessage("Did you take your "+ pillName + " ?");

        builder.setPositiveButton("I took it", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pillAlertActivity act = (pillAlertActivity)getActivity();
                act.onPillTaken(pillName);
                getActivity().finish();
            }
        });

        builder.setNeutralButton("Snooze", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pillAlertActivity act = (pillAlertActivity)getActivity();
                act.onPillSnooze(pillName);
                getActivity().finish();
            }
        });

        builder.setNegativeButton("I won't take", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pillAlertActivity act = (pillAlertActivity)getActivity();
                act.onPillNotTaken();
                getActivity().finish();
            }
        });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }
}