package edu.gatech.teamnull.thdhackathon2017.model;

/**
 * Created by karshinlin on 9/17/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by karshinlin on 9/16/17.
 */

public class ReviewDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Reviews.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Data.ReviewEntry.TABLE_NAME + " (" +
                    Data.ReviewEntry._ID + " INTEGER PRIMARY KEY," +
                    Data.ReviewEntry.COLUMN_NAME_REVIEWER + " TEXT," +
                    Data.ReviewEntry.COLUMN_NAME_TEXT + " TEXT," +
                    Data.ReviewEntry.COLUMN_NAME_RATING + " INTEGER," +
                    Data.ReviewEntry.COLUMN_NAME_SKU + " TEXT," +
                    Data.ReviewEntry.COLUMN_NAME_TITLE + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Data.ReviewEntry.TABLE_NAME;

    public ReviewDBHelper(Context context) {
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

    public void write(SQLiteDatabase db, Review current) {
        ContentValues values = new ContentValues();
        values.put(Data.ReviewEntry.COLUMN_NAME_REVIEWER, Customer.customerName);
        values.put(Data.ReviewEntry.COLUMN_NAME_TEXT, current.getText());
        values.put(Data.ReviewEntry.COLUMN_NAME_RATING, current.getRating());
        values.put(Data.ReviewEntry.COLUMN_NAME_SKU, current.getSku());
        values.put(Data.ReviewEntry._ID, current.getId());
        long newRowId = db.insert(Data.ReviewEntry.TABLE_NAME, null, values);
    }
}
