package com.openwords.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * @author han
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "openwords.sqlite";
    private static DatabaseHandler instance;
    private static final String className = DatabaseHandler.class.getName();

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHandler(context);
        }
        return instance;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Word.TABLE_NAME + " ("
                + Word.COL_ID + " INTEGER PRIMARY KEY,"
                + Word.COL_LANGUAGE + " INTEGER,"
                + Word.COL_SPELLING + " TEXT"
                + ")";
        db.execSQL(sql);
        Log.d(className, sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(className, "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + Word.TABLE_NAME);
        onCreate(db);
    }

    public void clean() {
        instance.close();
        instance = null;
    }
}
