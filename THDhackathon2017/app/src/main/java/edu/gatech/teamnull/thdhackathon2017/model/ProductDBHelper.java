package edu.gatech.teamnull.thdhackathon2017.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by karshinlin on 9/16/17.
 */

public class ProductDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Products.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Data.ProductEntry.TABLE_NAME + " (" +
                    Data.ProductEntry._ID + " INTEGER PRIMARY KEY," +
                    Data.ProductEntry.COLUMN_NAME_PRICE + " TEXT," +
                    Data.ProductEntry.COLUMN_NAME_TITLE + " TEXT," +
                    Data.ProductEntry.COLUMN_NAME_SKU + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Data.ProductEntry.TABLE_NAME;

    public ProductDBHelper(Context context) {
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

    public void write(SQLiteDatabase db, Product current) {
        ContentValues values = new ContentValues();
        values.put(Data.ProductEntry.COLUMN_NAME_PRICE, current.getPrice());
        values.put(Data.ProductEntry.COLUMN_NAME_TITLE, current.getTitle());
        values.put(Data.ProductEntry.COLUMN_NAME_SKU, current.getSku());
        long newRowId = db.insert(Data.ProductEntry.TABLE_NAME, null, values);
    }
}