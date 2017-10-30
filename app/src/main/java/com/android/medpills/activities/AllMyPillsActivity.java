package com.android.medpills.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.android.medpills.Model.PillAlarm;
import com.android.medpills.Model.Pill;
import com.android.medpills.Model.PillsContainer;
import com.android.medpills.Model.PillComparator;
import com.android.medpills.R;
import com.android.medpills.adapters.CollapsingExpandingListAdapter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This activity handles the view and controller of the pillbox page, where
 * the user can view alarms by pills and edit or delete an alarm
 */
public class AllMyPillsActivity extends ActionBarActivity {
    CollapsingExpandingListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listOfPills;
    HashMap<String, List<String>> pillsAlarmsDays;
    List<List<List<Long>>> pillsAlarmsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pill_container);

        setupToolbar();
        setUpViews();

    }

    private void setUpViews() {
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        try {
            prepareListData();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        listAdapter = new CollapsingExpandingListAdapter(this, listOfPills, pillsAlarmsDays);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listOfPills.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listOfPills.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                PillsContainer pillsContainer = new PillsContainer();
                pillsContainer.setTempIds(pillsAlarmsIds.get(groupPosition).get(childPosition));

                Intent intent = new Intent(getApplicationContext(), EditPillActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    private void setupToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pill Box");
    }

    /** Preparing the list data */
    private void prepareListData() throws URISyntaxException {
        listOfPills = new ArrayList<String>();
        pillsAlarmsDays = new HashMap<String, List<String>>();
        pillsAlarmsIds = new ArrayList<List<List<Long>>>();

        PillsContainer pillbox = new PillsContainer();
        List<Pill> pills = pillbox.getPills(this);
        Collections.sort(pills, new PillComparator());

        for (Pill pill: pills){
            String name = pill.getPillName();
            listOfPills.add(name);
            List<String> times = new ArrayList<String>();
            List<PillAlarm> pillAlarms = pillbox.getAlarmByPill(this.getBaseContext(), name);
            List<List<Long>> ids = new ArrayList<List<Long>>();

            for (PillAlarm pillAlarm : pillAlarms){
                String time = pillAlarm.getStringTime() + daysList(pillAlarm);
                times.add(time);
                ids.add(pillAlarm.getIds());
            }
            pillsAlarmsIds.add(ids);
            pillsAlarmsDays.put(name, times);
        }
    }

    /**
     * Helper function to obtain a string of the days of the week
     * that can be used as a boolean list
     */
    private String daysList(PillAlarm pillAlarm){
        String days = "#";
        for(int i=0; i<7; i++){
            if (pillAlarm.getDayOfWeek()[i])
                days += "1";
            else
                days += "0";
        }
        return days;
    }

    @Override
    public void onBackPressed() {
        Intent returnHome = new Intent(getBaseContext(), MainActivity.class);
        startActivity(returnHome);
        finish();
    }
}