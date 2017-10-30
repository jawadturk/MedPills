package com.android.medpills.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class pillAlertActivity extends FragmentActivity {

    private AlarmManager alarmManager;
    private PendingIntent operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertAlarm alert = new AlertAlarm();
        alert.show(getSupportFragmentManager(), "AlertAlarm");
    }

    // Snooze
    public void onPillSnooze(String pillName){
        final int _id = (int) System.currentTimeMillis();
        final long minute = 60000;
        long snoozeLength = 10;
        long currTime = System.currentTimeMillis();
        long min = currTime + minute * snoozeLength;

        Intent intent = new Intent(getBaseContext(), pillAlertActivity.class);
        intent.putExtra("pill_name", pillName);

        operation = PendingIntent.getActivity(getBaseContext(), _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /** Getting a reference to the System Service ALARM_SERVICE */
        alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, min, operation);
        Toast.makeText(getBaseContext(), "Alarm for " + pillName + " was snoozed for 10 minutes", Toast.LENGTH_SHORT).show();

        finish();

    }

    // I took it
    public void onPillTaken(String pillName){
        Calendar takeTime = Calendar.getInstance();
        int hour = takeTime.get(Calendar.HOUR_OF_DAY);
        int minute = takeTime.get(Calendar.MINUTE);
        String am_pm = (hour < 12) ? "am" : "pm";


        String mintuesTime;
        if (minute < 10)
            mintuesTime = "0" + minute;
        else
            mintuesTime = "" + minute;

        int hoursTime = hour % 12;
        if (hoursTime == 0)
            hoursTime = 12;

        Toast.makeText(getBaseContext(),  pillName + " was taken at "+ hoursTime + ":" + mintuesTime + " " + am_pm + ".", Toast.LENGTH_SHORT).show();

        Intent returnHistory = new Intent(getBaseContext(), MainActivity.class);
        startActivity(returnHistory);
        finish();
    }

    // I won't take it
    public void onPillNotTaken(){
        finish();
    }
}