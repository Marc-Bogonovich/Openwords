package com.openwords.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// THIS IS THE CODE CREATED BY CHRIS WHICH MANIPULATES Plate_DB
public class PlateDbHelper   extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = OpenwordsDatabaseManager.Plate_DB.TABLE_NAME+".db";
	private static final String SQL_CREATE_PLATE = "CREATE TABLE IF NOT EXISTS " + OpenwordsDatabaseManager.Plate_DB.TABLE_NAME + " ("+OpenwordsDatabaseManager.Plate_DB._ID + " INTEGER PRIMARY KEY,"
			+ OpenwordsDatabaseManager.Plate_DB.PROBLEM + " TEXT," + OpenwordsDatabaseManager.Plate_DB.ANSWER + " TEXT," + OpenwordsDatabaseManager.Plate_DB.PERF + " BOOLEAN"+")";
	private static final String SQL_DELETE = "DROP TABLE IF EXISTS "+ OpenwordsDatabaseManager.Plate_DB.TABLE_NAME;
	
	public PlateDbHelper(Context john) {
		super(john, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_PLATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE);
		db.execSQL(SQL_CREATE_PLATE);
		
	}	
	
}

