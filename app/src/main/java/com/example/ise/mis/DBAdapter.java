package com.example.ise.mis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    // Field Names:
    public static final String KEY_ROWID = "_id";
    // Field Names Notice Table
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_NOTICE = "notice";
    public static final String[] ALL_KEYS_NOTICES = new String[] {KEY_ROWID, KEY_SUBJECT, KEY_NOTICE};
    // Field Names Tag Table
    public static final String KEY_NOTICEID = "notice_id";
    public static final String KEY_TAG = "tag";
    public static final String[] ALL_KEYS_TAGS = new String[] {KEY_ROWID, KEY_NOTICEID, KEY_TAG};
    // Field Names Location Table
    //public static final String KEY_NOTICEID = "notice_id";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String[] ALL_KEYS_LOCATIONS = new String[] {KEY_ROWID, KEY_NOTICEID, KEY_LATITUDE, KEY_LONGITUDE};

    // Column Numbers for each Field Name:
    public static final int COL_ROWID = 0;
    // Column Numbers for each Field Name Notice Table:
    public static final int COL_SUBJECT = 1;
    public static final int COL_NOTICE = 2;
    // Column Numbers for each Field Name Tag Table:
    public static final int COL_NOTICEID = 1;
    public static final int COL_TAG = 2;
    // Column Numbers for each Field Name Location Table:
    //public static final int COL_NOTICEID = 1;
    public static final int COL_LATITUDE = 2;
    public static final int COL_LONGITUDE = 3;

    // DataBase info:
    public static final String DATABASE_NAME = "dbNotice";
    public static final String DATABASE_TABLE_NOTICE = "TableNotice";
    public static final String DATABASE_TABLE_TAG = "TableTag";
    public static final String DATABASE_TABLE_LOCATION = "TableLocation";
    public static final int DATABASE_VERSION = 2; // The version number must be incremented each time a change to DB structure occurs.

    //SQL statement to create database table Notice
    private static final String DATABASE_CREATE_SQL_NOTICE =
            "CREATE TABLE " + DATABASE_TABLE_NOTICE
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_SUBJECT + " TEXT NOT NULL, "
                    + KEY_NOTICE + " TEXT"
                    + ");";
    //SQL statement to create database table Tag
    private static final String DATABASE_CREATE_SQL_TAG =
            "CREATE TABLE " + DATABASE_TABLE_TAG
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NOTICEID + " INTEGER NOT NULL, "
                    + KEY_TAG + " TEXT"
                    + ");";
    //SQL statement to create database table Location
    private static final String DATABASE_CREATE_SQL_LOCATION =
            "CREATE TABLE " + DATABASE_TABLE_LOCATION
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NOTICEID + " INTEGER NOT NULL, "
                    + KEY_LATITUDE + " DOUBLE, "
                    + KEY_LONGITUDE + " DOUBLE"
                    + ");";

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to be inserted into the Notice database.
    public long insertRowNotice(String subject, String notice) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SUBJECT, subject);
        initialValues.put(KEY_NOTICE, notice);

        // Insert the data into the database.
        return db.insert(DATABASE_TABLE_NOTICE, null, initialValues);
    }

    // Add a new set of values to be inserted into the Tag database.
    public long insertRowTag(long noticeID, String tag) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOTICEID, noticeID);
        initialValues.put(KEY_TAG, tag);

        // Insert the data into the database.
        return db.insert(DATABASE_TABLE_TAG, null, initialValues);
    }

    // Add a new set of values to be inserted into the Location database.
    public long insertRowLocation(long noticeID, double latitude, double longitude) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOTICEID, noticeID);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);

        // Insert the data into the database.
        return db.insert(DATABASE_TABLE_LOCATION, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRowNotice(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE_NOTICE, where, null) != 0;
    }

    // Delete a row from the database, by noticeId (primary key notice table)
    public boolean deleteRowTag(long noticeId) {
        String where = KEY_NOTICEID + "=" + noticeId;
        return db.delete(DATABASE_TABLE_TAG, where, null) != 0;
    }

    // Delete a row from the database, by noticeId (primary key notice table)
    public boolean deleteRowLocation(long noticeId) {
        String where = KEY_NOTICEID + "=" + noticeId;
        return db.delete(DATABASE_TABLE_LOCATION, where, null) != 0;
    }

    public void deleteAllNotice() {
        Cursor c = getAllRowsNotice();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRowNotice(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    public void deleteAllTag() {
        Cursor c = getAllRowsTag();
        long noticeId = c.getColumnIndexOrThrow(KEY_NOTICEID);
        if (c.moveToFirst()) {
            do {
                deleteRowTag(c.getLong((int) noticeId));
            } while (c.moveToNext());
        }
        c.close();
    }

    public void deleteAllLocation() {
        Cursor c = getAllRowsLocation();
        long noticeId = c.getColumnIndexOrThrow(KEY_NOTICEID);
        if (c.moveToFirst()) {
            do {
                deleteRowLocation(c.getLong((int) noticeId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the Notice database.
    public Cursor getAllRowsNotice() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE_NOTICE, ALL_KEYS_NOTICES, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Return all data in the Tag database.
    public Cursor getAllRowsTag() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE_TAG, ALL_KEYS_TAGS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Return all data in the location database.
    public Cursor getAllRowsLocation() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE_LOCATION, ALL_KEYS_LOCATIONS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Return all data in the Tag database of a noticeId.
    public Cursor getAllNoticeIdRowsTag(long noticeId) {
        String where = KEY_NOTICEID + "=" + noticeId;
        Cursor c = 	db.query(true, DATABASE_TABLE_TAG, ALL_KEYS_TAGS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Return all data in the location database of a noticeId.
    public Cursor getAllNoticeIdRowsLocation(long noticeId) {
        String where = KEY_NOTICEID + "=" + noticeId;
        Cursor c = 	db.query(true, DATABASE_TABLE_LOCATION, ALL_KEYS_LOCATIONS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRowNotice(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_NOTICE, ALL_KEYS_NOTICES,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRowTag(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_TAG, ALL_KEYS_TAGS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRowLocation(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_LOCATION, ALL_KEYS_LOCATIONS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRowNotice(long rowId, String subject, String notice) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_SUBJECT, subject);
        newValues.put(KEY_NOTICE, notice);
        // Insert it into the database.
        return db.update(DATABASE_TABLE_NOTICE, newValues, where, null) != 0;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRowTag(long rowId, long noticeId, String tag) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NOTICEID, noticeId);
        newValues.put(KEY_TAG, tag);
        // Insert it into the database.
        return db.update(DATABASE_TABLE_TAG, newValues, where, null) != 0;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRowLocation(long rowId, long noticeId, double latitude, double longitude) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NOTICE, noticeId);
        newValues.put(KEY_LATITUDE, latitude);
        newValues.put(KEY_LONGITUDE, longitude);
        // Insert it into the database.
        return db.update(DATABASE_TABLE_LOCATION, newValues, where, null) != 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL_NOTICE);
            _db.execSQL(DATABASE_CREATE_SQL_TAG);
            _db.execSQL(DATABASE_CREATE_SQL_LOCATION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w("DBAdapter", "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NOTICE);
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TAG);
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LOCATION);

            // Recreate new database:
            onCreate(_db);
        }
    }


}

