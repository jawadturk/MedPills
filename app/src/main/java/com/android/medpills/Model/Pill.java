package com.android.medpills.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Pill {
    private String pillName;
    private long pillId;
    private List<PillAlarm> pillAlarms = new LinkedList<PillAlarm>();
    public String getPillName() { return pillName; }
    public void setPillName(String pillName) { this.pillName = pillName; }
    public long getPillId() {
        return pillId;
    }

    public void setPillId(long pillID) {
        this.pillId = pillID;
    }
    /**
     *
     * @param pillAlarm
     * allows a new alarm sto be added to a preexisting alarm
     */
    public void addAlarm(PillAlarm pillAlarm) {
        pillAlarms.add(pillAlarm);
        Collections.sort(pillAlarms);
    }


}
