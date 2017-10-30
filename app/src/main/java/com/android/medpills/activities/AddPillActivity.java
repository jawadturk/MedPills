package com.android.medpills.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.medpills.Model.PillAlarm;
import com.android.medpills.Model.Pill;
import com.android.medpills.Model.PillsContainer;
import com.android.medpills.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.liuguangqiang.ipicker.IPicker;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;



public class AddPillActivity extends AppCompatActivity {
    private AlarmManager alarmManager;
    private PendingIntent operation;
    private boolean weekDaysList[] = new boolean[7];
    private Button button_cancelAlarm;
    private Button button_setAlarm;
    private EditText editText_pillTitle;
    private ImageView imageView_pillImage;
    private int hour, minute;
    private TextView textView_pillTiming;
    private String imageName = "";
    private PillsContainer pillsContainer = new PillsContainer();
    private List<Long> ids = new LinkedList<Long>();

    private TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour)
        {
            hour = hourOfDay;
            minute = minuteOfHour;
            textView_pillTiming.setText(timeTransformer(hour, minute));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        setUpViews();
        setUpToolBar();


        button_setAlarm = (Button) findViewById(R.id.btn_set_alarm);
        button_setAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkBoxCounter = 0;
                String pill_name = editText_pillTitle.getText().toString();

                PillAlarm pillAlarm = new PillAlarm();
                if (!atLeastDayChecked() || TextUtils.isEmpty(pill_name)) {
                    Toast.makeText(getBaseContext(), "Please enter title and check at least one day!", Toast.LENGTH_SHORT).show();

                } else {

                    /** If Pill does not already exist */
                    if (!pillsContainer.pillAlreadyExist(getApplicationContext(), pill_name)) {
                        Pill pill = new Pill();
                        pill.setPillName(pill_name);
                        pillAlarm.setHour(hour);
                        pillAlarm.setMinute(minute);
                        pillAlarm.setPillName(pill_name);
                        pillAlarm.setPillPhotoPath(imageName);
                        pillAlarm.setDayOfWeek(weekDaysList);
                        pill.addAlarm(pillAlarm);
                        long pillId = pillsContainer.addNewPill(getApplicationContext(), pill);
                        pill.setPillId(pillId);
                        pillsContainer.addNewAlarm(getApplicationContext(), pillAlarm, pill);
                    } else { // If Pill already exists
                        Pill pill = pillsContainer.getPillByName(getApplicationContext(), pill_name);
                        pillAlarm.setHour(hour);
                        pillAlarm.setMinute(minute);
                        pillAlarm.setPillName(pill_name);
                        pillAlarm.setPillPhotoPath(imageName);
                        pillAlarm.setDayOfWeek(weekDaysList);
                        pill.addAlarm(pillAlarm);
                        pillsContainer.addNewAlarm(getApplicationContext(), pillAlarm, pill);
                    }


                    try {
                        List<PillAlarm> pillAlarms = pillsContainer.getAlarmByPill(getApplicationContext(), pill_name);
                        for (PillAlarm tempPillAlarm : pillAlarms) {
                            if (tempPillAlarm.getHour() == hour && tempPillAlarm.getMinute() == minute) {
                                ids = tempPillAlarm.getIds();
                                break;
                            }
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }


                    for (int i = 0; i < weekDaysList.length; i++) {
                        if (weekDaysList[i]) {

                            int dayOfWeek = i + 1;

                            long _id = ids.get(checkBoxCounter);
                            int id = (int) _id;
                            checkBoxCounter++;

                            /** This intent invokes the activity AlertActivity, which in turn opens the AlertAlarm window */
                            Intent intent = new Intent(getBaseContext(), pillAlertActivity.class);
                            intent.putExtra("pill_name", pill_name);

                            operation = PendingIntent.getActivity(getBaseContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            /** Getting a reference to the System Service ALARM_SERVICE */
                            alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

                            /** Creating a calendar object corresponding to the date and time set by the user */
                            Calendar calendar = Calendar.getInstance();

                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.MINUTE, minute);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);
                            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                            /** Converting the date and time in to milliseconds elapsed since epoch */
                            long alarm_time = calendar.getTimeInMillis();

                            if (calendar.before(Calendar.getInstance()))
                                alarm_time += AlarmManager.INTERVAL_DAY * 7;

                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_time,
                                    alarmManager.INTERVAL_DAY * 7, operation);
                        }
                    }

                    Toast.makeText(getBaseContext(), "Alarm for " + pill_name + " is set successfully", Toast.LENGTH_SHORT).show();
                    Intent returnHome = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(returnHome);
                    finish();
                }


            }
        });

        button_cancelAlarm = (Button) findViewById(R.id.btn_cancel_alarm);
        button_cancelAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnHome = new Intent(getBaseContext(), MainActivity.class);
                startActivity(returnHome);
                finish();
            }
        });
    }

    private void setUpToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add an Alarm");
    }

    private void setUpViews() {
        editText_pillTitle = (EditText) findViewById(R.id.pill_name);

        imageView_pillImage = (ImageView) findViewById(R.id.imageView_pillImage);
        imageView_pillImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IPicker.open(getApplicationContext());
            }
        });
        IPicker.setLimit(1);
        IPicker.setCropEnable(false);

        IPicker.setOnSelectedListener(new IPicker.OnSelectedListener() {
            @Override
            public void onSelected(final List<String> paths) {
                imageName = paths.get(0);
                Glide
                        .with(imageView_pillImage.getContext())
                        .load(paths.get(0))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(80, 80) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                imageView_pillImage.setImageBitmap(resource);

                            }
                        });
            }
        });
        // Set up the time string on the page
        textView_pillTiming = (TextView) findViewById(R.id.reminder_time);
        Typeface lightFont = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        textView_pillTiming.setTypeface(lightFont);


        // Get the time right now and set it to be the time string
        Calendar rightNow = Calendar.getInstance();
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);
        textView_pillTiming.setText(timeTransformer(hour, minute));

        textView_pillTiming.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(AddPillActivity.this,
                        //R.style.Theme_AppCompat_Dialog,
                        t,
                        hour,
                        minute,
                        false).show();
            }
        });
        textView_pillTiming.setText(timeTransformer(hour, minute));
    }



    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        /** Checking which checkbox was clicked */
        switch (view.getId()) {
            case R.id.checkbox_monday:
                if (checked)
                    weekDaysList[1] = true;
                else
                    weekDaysList[1] = false;
                break;
            case R.id.checkbox_tuesday:
                if (checked)
                    weekDaysList[2] = true;
                else
                    weekDaysList[2] = false;
                break;
            case R.id.checkbox_wednesday:
                if (checked)
                    weekDaysList[3] = true;
                else
                    weekDaysList[3] = false;
                break;
            case R.id.checkbox_thursday:
                if (checked)
                    weekDaysList[4] = true;
                else
                    weekDaysList[4] = false;
                break;
            case R.id.checkbox_friday:
                if (checked)
                    weekDaysList[5] = true;
                else
                    weekDaysList[5] = false;
                break;
            case R.id.checkbox_saturday:
                if (checked)
                    weekDaysList[6] = true;
                else
                    weekDaysList[6] = false;
                break;
            case R.id.checkbox_sunday:
                if (checked)
                    weekDaysList[0] = true;
                else
                    weekDaysList[0] = false;
                break;
            case R.id.every_day:
                LinearLayout ll = (LinearLayout) findViewById(R.id.checkbox_layout);
                for (int i = 0; i < ll.getChildCount(); i++) {
                    View v = ll.getChildAt(i);
                    ((CheckBox) v).setChecked(checked);
                    onCheckboxClicked(v);
                }
                break;
        }
    }



    /**
     * This method takes hours and minute as input and returns
     * a string that is like "12:01pm"
     */
    public String timeTransformer(int hour, int minute) {
        String am_pm = (hour < 12) ? "am" : "pm";
        int hoursTime = hour % 12;
        if (hoursTime == 0)
            hoursTime = 12;
        String minuteWithZero;
        if (minute < 10)
            minuteWithZero = "0" + minute;
        else
            minuteWithZero = "" + minute;
        return hoursTime + ":" + minuteWithZero + am_pm;
    }

    @Override
    public void onBackPressed() {
        Intent returnHome = new Intent(getBaseContext(), MainActivity.class);
        startActivity(returnHome);
        finish();
    }

    private boolean atLeastDayChecked() {
        boolean checked = false;
        for (int i = 0; i < weekDaysList.length; i++) {
            if (weekDaysList[i] == true) {
                checked = true;
            }
        }
        return checked;
    }
}