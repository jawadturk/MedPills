package com.android.medpills.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.medpills.Model.PillAlarm;
import com.android.medpills.Model.Pill;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {

    /** Database name */
    private static final String DATABASE_NAME = "pill_model_database";

    /** Database version */
    private static final int DATABASE_VERSION =4;

    public static final class TABLE_PILLS {
        public static final String TABLE_NAME = "pills";
        public static final String COL_ID = "id";
        public static final String COL_PILL_NAME = "pillName";

        /** Pill Table: create statement */
        private static final String CREATE_TABLE =
                "create table " + TABLE_PILLS.TABLE_NAME + "("
                        + TABLE_PILLS.COL_ID + " integer primary key not null,"
                        + TABLE_PILLS.COL_PILL_NAME + " text not null"
                        + ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }



    public static final class TABLE_PILLS_ALARMS {
        public static final String TABLE_NAME = "alarms";
        public static final String COL_ID = "id";
        public static final String COL_INTENT = "intent";
        public static final String COL_HOURS = "hour";
        public static final String COL_MINUTES = "minute";
        public static final String COL_DAY_OF_WEEK= "dayOfWeek";
        public static final String COL_ALARM_PILL_NAME= "pillName";
        public static final String COL_PILL_PHOTO = "pillPhoto";



        /** Alarm Table: create statement */
        private static final String CREATE_TABLE =
                "create table "         + TABLE_PILLS_ALARMS.TABLE_NAME + "("
                        + TABLE_PILLS_ALARMS.COL_ID     + " integer primary key,"
                        + TABLE_PILLS_ALARMS.COL_INTENT    + " text,"
                        + TABLE_PILLS_ALARMS.COL_HOURS      + " integer,"
                        + TABLE_PILLS_ALARMS.COL_MINUTES    + " integer,"
                        + TABLE_PILLS_ALARMS.COL_ALARM_PILL_NAME  + " text not null,"
                        + TABLE_PILLS_ALARMS.COL_PILL_PHOTO  + " text,"
                        + TABLE_PILLS_ALARMS.COL_DAY_OF_WEEK  + " integer" + ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static final class TABLE_PILLS_ALARMS_LINKS {
        public static final String TABLE_NAME = "pill_alarm";
        public static final String COL_ID = "id";
        public static final String COL_PILL_TABLE_ID= "pill_id";
        public static final String COL_Alarm_TABLE_ID= "alarm_id";


        /** Pill-Alarm link table: create statement */
        private static final String CREATE_TABLE =
                "create table "             + TABLE_PILLS_ALARMS_LINKS.TABLE_NAME + "("
                        + TABLE_PILLS_ALARMS_LINKS.COL_ID         + " integer primary key not null,"
                        + TABLE_PILLS_ALARMS_LINKS.COL_PILL_TABLE_ID  + " integer not null,"
                        + TABLE_PILLS_ALARMS_LINKS.COL_Alarm_TABLE_ID + " integer not null" + ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static final class TABLE_PILLS_ALARMS_HISTORIES {
        public static final String TABLE_NAME = "histories";
        public static final String COL_ID = "id";
        public static final String COL_PILL_NAME = "pillName";
        public static final String COL_HOURS = "hour";
        public static final String COL_MINUTES = "minute";
        public static final String COL_DATE_STRING = "date";

        /** Histories Table: create statement */
        private static final String CREATE_TABLE =
                "CREATE TABLE "             + TABLE_PILLS_ALARMS_HISTORIES.TABLE_NAME + "("
                        + TABLE_PILLS_ALARMS_HISTORIES.COL_ID + " integer primary key, "
                        + TABLE_PILLS_ALARMS_HISTORIES.COL_PILL_NAME+ " text not null, "
                        + TABLE_PILLS_ALARMS_HISTORIES.COL_DATE_STRING   + " text, "
                        + TABLE_PILLS_ALARMS_HISTORIES.COL_HOURS          + " integer, "
                        + TABLE_PILLS_ALARMS_HISTORIES.COL_MINUTES        + " integer " + ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    /** Constructor */
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    /** Creating tables */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_PILLS.CREATE_TABLE);
        db.execSQL(TABLE_PILLS_ALARMS.CREATE_TABLE);
        db.execSQL(TABLE_PILLS_ALARMS_LINKS.CREATE_TABLE);
        db.execSQL(TABLE_PILLS_ALARMS_HISTORIES.CREATE_TABLE);
    }

    @Override
    // TODO: change this so that updating doesn't delete old data
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_PILLS.DROP_TABLE);
        db.execSQL(TABLE_PILLS_ALARMS.DROP_TABLE);
        db.execSQL(TABLE_PILLS_ALARMS_LINKS.DROP_TABLE );
        db.execSQL(TABLE_PILLS_ALARMS_HISTORIES.DROP_TABLE);
        onCreate(db);
    }

// ############################## create methods ###################################### //



    /**
     * createPill takes a pill object and inserts the relevant data into the database
     *
     * @param pill a model pill object
     * @return the long row_id generate by the database upon entry into the database
     */
    public long createPill(Pill pill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_PILLS.COL_PILL_NAME, pill.getPillName());

        long pill_id = db.insert(TABLE_PILLS.TABLE_NAME, null, values);

        return pill_id;
    }

    /**
     * takes in a model alarm object and inserts a row into the database
     *      for each day of the week the alarm is meant to go off.
     * @param pillAlarm a model alarm object
     * @param pill_id the id associated with the pill the alarm is for
     * @return a array of longs that are the row_ids generated by the database when the rows are inserted
     */
    public long[] createAlarm(PillAlarm pillAlarm, long pill_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long[] alarm_ids = new long[7];

        /** Create a separate row in the table for every day of the week for this alarm */
        int arrayPos = 0;
        for (boolean day : pillAlarm.getDayOfWeek()) {
            if (day) {
                ContentValues values = new ContentValues();
                values.put(TABLE_PILLS_ALARMS.COL_HOURS, pillAlarm.getHour());
                values.put(TABLE_PILLS_ALARMS.COL_MINUTES, pillAlarm.getMinute());
                values.put(TABLE_PILLS_ALARMS.COL_DAY_OF_WEEK, arrayPos + 1);
                values.put(TABLE_PILLS_ALARMS.COL_ALARM_PILL_NAME , pillAlarm.getPillName());
                values.put(TABLE_PILLS_ALARMS.COL_PILL_PHOTO, pillAlarm.getPillPhotoPath());
                /** Insert row */
                long alarm_id = db.insert(TABLE_PILLS_ALARMS.TABLE_NAME, null, values);
                alarm_ids[arrayPos] = alarm_id;

                /** Link alarm to a pill */
                createPillAlarmLink(pill_id, alarm_id);
            }
            arrayPos++;
        }
        return alarm_ids;
    }

    /**
     * private function that inserts a row into a table that links pills and alarms
     *
     * @param pill_id the row_id of the pill that is being added to or edited
     * @param alarm_id the row_id of the alarm that is being added to the pill
     * @return returns the row_id the database creates when a row is created
     */
    private long createPillAlarmLink(long pill_id, long alarm_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_PILLS_ALARMS_LINKS.COL_PILL_TABLE_ID , pill_id);
        values.put(TABLE_PILLS_ALARMS_LINKS.COL_Alarm_TABLE_ID , alarm_id);

        /** Insert row */
        long pillAlarmLink_id = db.insert(TABLE_PILLS_ALARMS_LINKS.TABLE_NAME , null, values);

        return pillAlarmLink_id;
    }


// ############################# get methods ####################################### //

    /**
     * allows pillBox to retrieve a row from pill table in Db
     * @param pillName takes in a string of the pill Name
     * @return returns a pill model object
     */
    public Pill getPillByName(String pillName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbPill = "select * from "
                + TABLE_PILLS.TABLE_NAME        + " where "
                + TABLE_PILLS.COL_PILL_NAME      + " = "
                + "'"   + pillName  + "'";

        Cursor c = db.rawQuery(dbPill, null);

        Pill pill = new Pill();

        if (c.moveToFirst() && c.getCount() >= 1) {
            pill.setPillName(c.getString(c.getColumnIndex(TABLE_PILLS.COL_PILL_NAME)));
            pill.setPillId(c.getLong(c.getColumnIndex(TABLE_PILLS.COL_ID)));
            c.close();
        }
        return pill;
    }

    /**
     * allows the pillBox to retrieve all the pill rows from database
     * @return a list of pill model objects
     */
    public List<Pill> getAllPills() {
        List<Pill> pills = new ArrayList<>();
        String dbPills = "SELECT * FROM " + TABLE_PILLS.TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbPills, null);

        /** Loops through all rows, adds to list */
        if (c.moveToFirst()) {
            do {
                Pill p = new Pill();
                p.setPillName(c.getString(c.getColumnIndex(TABLE_PILLS.COL_PILL_NAME)));
                p.setPillId(c.getLong(c.getColumnIndex(TABLE_PILLS.COL_ID)));

                pills.add(p);
            } while (c.moveToNext());
        }
        c.close();
        return pills;
    }


    /**
     * Allows pillBox to retrieve all Alarms linked to a Pill
     * uses mergeAlarms helper method
     * @param pillName string
     * @return list of alarm objects
     * @throws URISyntaxException honestly do not know why, something about alarm.getDayOfWeek()
     */
    public List<PillAlarm> getAllAlarmsByPill(String pillName) throws URISyntaxException {
        List<PillAlarm> alarmsByPill = new ArrayList<PillAlarm>();

        /** HINT: When reading string: '.' are not periods ex) pill.rowIdNumber */
        String selectQuery = "SELECT * FROM "       +
                TABLE_PILLS_ALARMS.TABLE_NAME         + " alarm, "    +
                TABLE_PILLS.TABLE_NAME          + " pill, "     +
                TABLE_PILLS_ALARMS_LINKS.TABLE_NAME     + " pillAlarm WHERE "           +
                "pill."             + TABLE_PILLS.COL_PILL_NAME      + " = '"    + pillName + "'" +
                " AND pill."        + TABLE_PILLS.COL_ID         + " = "     +
                "pillAlarm."        + TABLE_PILLS_ALARMS_LINKS.COL_PILL_TABLE_ID   +
                " AND alarm."       + TABLE_PILLS_ALARMS.COL_ID         + " = "     +
                "pillAlarm."        + TABLE_PILLS_ALARMS_LINKS.COL_Alarm_TABLE_ID ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                PillAlarm al = new PillAlarm();
                al.setId(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_ID)));
                al.setHour(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_HOURS)));
                al.setMinute(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_MINUTES)));
                al.setPillName(c.getString(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_ALARM_PILL_NAME )));

                alarmsByPill.add(al);
            } while (c.moveToNext());
        }

        c.close();


        return mergeAlarms(alarmsByPill);
    }

    /**
     * returns all individual alarms that occur on a certain day of the week,
     * alarms returned do not know of their counterparts that occur on different days
     * @param day an integer that represents the day of week
     * @return a list of Alarms (not combined into full-model-alarms)
     */
    public List<PillAlarm> getAlarmsByDay(int day) {
        List<PillAlarm> daysPillAlarms = new ArrayList<PillAlarm>();

        String selectQuery = "SELECT * FROM "       +
                TABLE_PILLS_ALARMS.TABLE_NAME     + " alarm WHERE "   +
                "alarm."        + TABLE_PILLS_ALARMS.COL_DAY_OF_WEEK      +
                " = '"          + day               + "'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                PillAlarm al = new PillAlarm();
                al.setId(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_ID)));
                al.setHour(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_HOURS)));
                al.setMinute(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_MINUTES)));
                al.setPillPhotoPath(c.getString(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_PILL_PHOTO)));
                al.setPillName(c.getString(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_ALARM_PILL_NAME )));

                daysPillAlarms.add(al);
            } while (c.moveToNext());
        }
        c.close();

        return daysPillAlarms;
    }


    /**
     *
     * @param alarm_id
     * @return
     * @throws URISyntaxException
     */
    public PillAlarm getAlarmById(long alarm_id) throws URISyntaxException {

        String dbAlarm = "SELECT * FROM "   +
                TABLE_PILLS_ALARMS.TABLE_NAME + " WHERE "     +
                TABLE_PILLS_ALARMS.COL_ID   + " = "         + alarm_id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        PillAlarm al = new PillAlarm();
        al.setId(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_ID )));
        al.setHour(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_HOURS)));
        al.setMinute(c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_MINUTES)));
        al.setPillName(c.getString(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_ALARM_PILL_NAME )));
        al.setPillPhotoPath(c.getString(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_PILL_PHOTO)));
        c.close();

        return al;
    }

    /**
     * Private helper function that combines rows in the databse back into a
     * full model-alarm with a dayOfWeek array.
     * @param dbPillAlarms a list of dbAlarms (not-full-alarms w/out day of week info)
     * @return a list of model-alarms
     * @throws URISyntaxException
     */
    private List<PillAlarm> mergeAlarms(List<PillAlarm> dbPillAlarms) throws URISyntaxException {
        List<String> timesOfDay = new ArrayList<>();
        List<PillAlarm> combinedPillAlarms = new ArrayList<>();

        for (PillAlarm al : dbPillAlarms) {
            if (timesOfDay.contains(al.getStringTime())) {
                /** Add this db row to alarm object */
                for (PillAlarm ala : combinedPillAlarms) {
                    if (ala.getStringTime().equals(al.getStringTime())) {
                        int day = getDayOfWeek(al.getId());
                        boolean[] days = ala.getDayOfWeek();
                        days[day-1] = true;
                        ala.setDayOfWeek(days);
                        ala.addId(al.getId());
                    }
                }
            } else {
                /** Create new Alarm object with day of week array */
                PillAlarm newPillAlarm = new PillAlarm();
                boolean[] days = new boolean[7];

                newPillAlarm.setPillName(al.getPillName());
                newPillAlarm.setMinute(al.getMinute());
                newPillAlarm.setHour(al.getHour());
                newPillAlarm.addId(al.getId());

                int day = getDayOfWeek(al.getId());
                days[day-1] = true;
                newPillAlarm.setDayOfWeek(days);

                timesOfDay.add(al.getStringTime());
                combinedPillAlarms.add(newPillAlarm);
            }
        }

        Collections.sort(combinedPillAlarms);
        return combinedPillAlarms;
    }

    /**
     * Get a single pillapp.Model-Alarm
     * Used as a helper function
     */
    public int getDayOfWeek(long alarm_id) throws URISyntaxException {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbAlarm = "SELECT * FROM "   +
                TABLE_PILLS_ALARMS.TABLE_NAME + " WHERE "     +
                TABLE_PILLS_ALARMS.COL_ID   + " = "         + alarm_id;

        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        int dayOfWeek = c.getInt(c.getColumnIndex(TABLE_PILLS_ALARMS.COL_DAY_OF_WEEK));
        c.close();

        return dayOfWeek;
    }




// -------------- delete methods ------------------- //


    private void deletePillAlarmLinks(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PILLS_ALARMS_LINKS.TABLE_NAME , TABLE_PILLS_ALARMS_LINKS.COL_Alarm_TABLE_ID
                + " = ?", new String[]{String.valueOf(alarmId)});
    }

    public void deleteAlarmById(long alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();

        /** First delete any link in PillAlarmLink Table */
        deletePillAlarmLinks(alarmId);

        /* Then delete alarm */
        db.delete(TABLE_PILLS_ALARMS.TABLE_NAME, TABLE_PILLS_ALARMS.COL_ID
                + " = ?", new String[]{String.valueOf(alarmId)});
    }

    public void deletePill(String pillName) throws URISyntaxException {
        SQLiteDatabase db = this.getWritableDatabase();
        List<PillAlarm> pillsPillAlarms;

        /** First get all Alarms and delete them and their Pill-links */
        pillsPillAlarms = getAllAlarmsByPill(pillName);
        for (PillAlarm pillAlarm : pillsPillAlarms) {
            long id = pillAlarm.getId();
            deleteAlarmById(id);
        }

        /** Then delete Pill */
        db.delete(TABLE_PILLS.TABLE_NAME, TABLE_PILLS.COL_PILL_NAME
                + " = ?", new String[]{pillName});
    }
}