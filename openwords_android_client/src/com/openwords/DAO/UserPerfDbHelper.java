package com.openwords.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserPerfDbHelper extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME+".db";
	private static final String SQL_CREATE_PLATE = "CREATE TABLE IF NOT EXISTS " + OpenwordsDatabaseManager.Plate_DB.TABLE_NAME + " ("+OpenwordsDatabaseManager.UserPerfDB.USERID + " INTEGER,"
			+ OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID + " INTEGER," + OpenwordsDatabaseManager.UserPerfDB.TOTALCORRECT + " INTEGER," + OpenwordsDatabaseManager.UserPerfDB.TOTALSKIPPED + " INTEGER"
			+ OpenwordsDatabaseManager.UserPerfDB.TOTALEXPOSURE + " INTEGER" + OpenwordsDatabaseManager.UserPerfDB.LASTTIME + " INTEGER" + OpenwordsDatabaseManager.UserPerfDB.LASTPERFORMANCE + " INTEGER"
			+ OpenwordsDatabaseManager.UserPerfDB.USEREXCLUDE + " INTEGER" +")";
	private static final String SQL_DELETE = "DROP TABLE IF EXISTS "+ OpenwordsDatabaseManager.Plate_DB.TABLE_NAME;
	
	public UserPerfDbHelper(Context john) {
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
