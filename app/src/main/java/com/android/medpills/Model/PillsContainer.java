package com.android.medpills.Model;

import android.content.Context;

import com.android.medpills.helper.DataBaseHelper;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;


public class PillsContainer {
    private DataBaseHelper db;
    private static List<Long> tempIds; // Ids of the alarms to be deleted or edited
    private static String tempName; // Ids of the alarms to be deleted or edited
    private static String pillImage;
    public  String getPillImage() {
        return pillImage;
    }

    public  void setPillImage(String pillImage) {
        PillsContainer.pillImage = pillImage;
    }



    public List<Long> getTempIds() { return Collections.unmodifiableList(tempIds); }

    public void setTempIds(List<Long> tempIds) { this.tempIds = tempIds; }

    public String getTempName() { return tempName; }

    public void setTempName(String tempName) { this.tempName = tempName; }

    public List<Pill> getPills(Context c) {
        db = new DataBaseHelper(c);
        List<Pill> allPills = db.getAllPills();
        db.close();
        return allPills;
    }

    public long addNewPill(Context c, Pill pill) {
        db = new DataBaseHelper(c);
        long pillId = db.createPill(pill);
        pill.setPillId(pillId);
        db.close();
        return pillId;
    }

    public Pill getPillByName(Context c, String pillName){
        db = new DataBaseHelper(c);
        Pill wantedPill = db.getPillByName(pillName);
        db.close();
        return wantedPill;
    }

    public void addNewAlarm(Context c, PillAlarm pillAlarm, Pill pill){
        db = new DataBaseHelper(c);
        db.createAlarm(pillAlarm, pill.getPillId());
        db.close();
    }

    public List<PillAlarm> gellAllTheAlarms(Context c, int dayOfWeek) throws URISyntaxException {
        db = new DataBaseHelper(c);
        List<PillAlarm> daysPillAlarms = db.getAlarmsByDay(dayOfWeek);
        db.close();
        Collections.sort(daysPillAlarms);
        return daysPillAlarms;
    }

    public List<PillAlarm> getAlarmByPill (Context c, String pillName) throws URISyntaxException {
        db = new DataBaseHelper(c);
        List<PillAlarm> pillsPillAlarms = db.getAllAlarmsByPill(pillName);
        db.close();
        return pillsPillAlarms;
    }

    public boolean pillAlreadyExist(Context c, String pillName) {
        db = new DataBaseHelper(c);
        for(Pill pill: this.getPills(c)) {
            if(pill.getPillName().equals(pillName))
                return true;
        }
        return false;
    }

    public void deletePill(Context c, String pillName) throws URISyntaxException {
        db = new DataBaseHelper(c);
        db.deletePill(pillName);
        db.close();
    }

    public void deletAlarmById(Context c, long alarmId) {
        db = new DataBaseHelper(c);
        db.deleteAlarmById(alarmId);
        db.close();
    }



    public PillAlarm getAlarmById(Context c, long alarm_id) throws URISyntaxException {
        db = new DataBaseHelper(c);
        PillAlarm pillAlarm = db.getAlarmById(alarm_id);
        db.close();
        return pillAlarm;
    }

    public int getDayOfWeek(Context c, long alarm_id) throws URISyntaxException {
        db = new DataBaseHelper(c);
        int getDayOfWeek = db.getDayOfWeek(alarm_id);
        db.close();
        return getDayOfWeek;
    }
}
