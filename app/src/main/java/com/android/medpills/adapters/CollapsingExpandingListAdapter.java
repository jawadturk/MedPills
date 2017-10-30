package com.android.medpills.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.medpills.R;

import java.util.HashMap;
import java.util.List;


public class CollapsingExpandingListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listPillsTitles; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listSelectedDaysForAlarms;

    public CollapsingExpandingListAdapter(Context _context, List<String> listDataHeader,
                                          HashMap<String, List<String>> listChildData) {
        this.context = _context;
        this.listPillsTitles = listDataHeader;
        this.listSelectedDaysForAlarms = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listSelectedDaysForAlarms.get(this.listPillsTitles.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View AlarmsItemView, ViewGroup parent)
    {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (AlarmsItemView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            AlarmsItemView = infalInflater.inflate(R.layout.list_item, null);
        }

        // childText should be a string that we can split into two parts
        String[] parts = childText.split("#");
        // The first part is the time
        String time = parts[0];
        // The second part is a string of 0 and 1 that works as a boolean list
        String daysOfWeek = parts[1];

        TextView textViewAlarmTime = (TextView) AlarmsItemView.findViewById(R.id.pill_box_time);
        textViewAlarmTime.setText(time);
        // Get all the textview objects from the xml file
        TextView textView_monday = (TextView) AlarmsItemView.findViewById(R.id.pill_box_monday);
        TextView textView_tuesday = (TextView) AlarmsItemView.findViewById(R.id.pill_box_tuesday);
        TextView textView_wednesDay = (TextView) AlarmsItemView.findViewById(R.id.pill_box_wednesday);
        TextView textView_thursday = (TextView) AlarmsItemView.findViewById(R.id.pill_box_thursday);
        TextView textView_friday = (TextView) AlarmsItemView.findViewById(R.id.pill_box_friday);
        TextView textView_saturday = (TextView) AlarmsItemView.findViewById(R.id.pill_box_saturday);
        TextView textView_sunday = (TextView) AlarmsItemView.findViewById(R.id.pill_box_sunday);

        // The color indicates the days of week when the alarm goes off
        int colorSelected = context.getResources().getColor(R.color.blue600);
        // The colors indicates the days of week when the alarm doesn't go off
        int colorNotSelected = Color.parseColor("#f4f4f4");

        // Use dayOfWeek as a boolean list to change the colors of the textviews
        for (int i = 0; i < 7; i++){
            if (i==0) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    textView_sunday.setTextColor(colorSelected);
                } else {
                    textView_sunday.setTextColor(colorNotSelected);
                }
            } else if (i==1) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    textView_monday.setTextColor(colorSelected);
                } else {
                    textView_monday.setTextColor(colorNotSelected);
                }
            } else if (i==2) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    textView_tuesday.setTextColor(colorSelected);
                } else {
                    textView_tuesday.setTextColor(colorNotSelected);
                }
            } else if (i==3) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    textView_wednesDay.setTextColor(colorSelected);
                } else {
                    textView_wednesDay.setTextColor(colorNotSelected);
                }
            } else if (i==4) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    textView_thursday.setTextColor(colorSelected);
                } else {
                    textView_thursday.setTextColor(colorNotSelected);
                }
            } else if (i==5) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    textView_friday.setTextColor(colorSelected);
                } else {
                    textView_friday.setTextColor(colorNotSelected);
                }
            } else if (i==6) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    textView_saturday.setTextColor(colorSelected);
                } else {
                    textView_saturday.setTextColor(colorNotSelected);
                }
            }
        }

        return AlarmsItemView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listSelectedDaysForAlarms.get(this.listPillsTitles.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listPillsTitles.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listPillsTitles.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}