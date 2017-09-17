package edu.gatech.teamnull.thdhackathon2017.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

/**
 * Created by karshinlin on 9/16/17.
 */

public class VideoDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Videos.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Data.VideoEntry.TABLE_NAME + " (" +
                    Data.VideoEntry.COLUMN_NAME_ID + " TEXT," +
                    Data.VideoEntry.COLUMN_NAME_THUMBNAIL + " TEXT," +
                    Data.VideoEntry.COLUMN_NAME_TITLE + " TEXT," +
                    Data.VideoEntry.COLUMN_NAME_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Data.VideoEntry.TABLE_NAME;

    public VideoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void write(SQLiteDatabase db, Video current) {
        ContentValues values = new ContentValues();
        values.put(Data.VideoEntry.COLUMN_NAME_ID, current.getId());
        values.put(Data.VideoEntry.COLUMN_NAME_THUMBNAIL, current.getThumbnail().getUrl());
        values.put(Data.VideoEntry.COLUMN_NAME_TITLE, current.getTitle());
        values.put(Data.VideoEntry.COLUMN_NAME_DATE, Calendar.getInstance().getTimeInMillis() + "");
        long newRowId = db.insert(Data.VideoEntry.TABLE_NAME, null, values);
    }
}