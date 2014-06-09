package com.openwords.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserPerfDbHelper extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME+".db";
	
	//------------ User Perf creation
	private static final String SQL_CREATE_USER_PERF = "CREATE TABLE IF NOT EXISTS " + OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME + " ("+OpenwordsDatabaseManager.UserPerfDB.USERID + " INTEGER,"
			+ OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID + " INTEGER," + OpenwordsDatabaseManager.UserPerfDB.TOTALCORRECT + " INTEGER," + OpenwordsDatabaseManager.UserPerfDB.TOTALSKIPPED + " INTEGER"
			+ OpenwordsDatabaseManager.UserPerfDB.TOTALEXPOSURE + " INTEGER" + OpenwordsDatabaseManager.UserPerfDB.LASTTIME + " INTEGER" + OpenwordsDatabaseManager.UserPerfDB.LASTPERFORMANCE + " INTEGER"
			+ OpenwordsDatabaseManager.UserPerfDB.USEREXCLUDE + " INTEGER" +")";
	private static final String SQL_DELETE = "DROP TABLE IF EXISTS "+ OpenwordsDatabaseManager.Plate_DB.TABLE_NAME;
	//--------------------------------
	
	//---------- Dirty User Perf creation-----
	private static final String SQL_CREATE_USER_PERF_DIRTY = "CREATE TABLE IF NOT EXISTS " + OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME_D + " ("+OpenwordsDatabaseManager.UserPerfDB.D_USERID + " INTEGER,"
			+ OpenwordsDatabaseManager.UserPerfDB.D_CONNECTIONID + " INTEGER," + OpenwordsDatabaseManager.UserPerfDB.D_TOTALCORRECT + " INTEGER," + OpenwordsDatabaseManager.UserPerfDB.D_TOTALSKIPPED + " INTEGER"
			+ OpenwordsDatabaseManager.UserPerfDB.D_TOTALEXPOSURE + " INTEGER" + OpenwordsDatabaseManager.UserPerfDB.D_LASTTIME + " INTEGER" + OpenwordsDatabaseManager.UserPerfDB.D_LASTPERFORMANCE + " INTEGER"
			+ OpenwordsDatabaseManager.UserPerfDB.D_USEREXCLUDE + " INTEGER" +")";
	private static final String SQL_DELETE_DIRTY = "DROP TABLE IF EXISTS "+ OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME_D;
	
	public UserPerfDbHelper(Context john) {
		super(john, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_USER_PERF);
		db.execSQL(SQL_CREATE_USER_PERF_DIRTY);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE);
		db.execSQL(SQL_CREATE_USER_PERF);
		
		db.execSQL(SQL_DELETE_DIRTY);
		db.execSQL(SQL_CREATE_USER_PERF_DIRTY);
		
	}
	
	//-----------------CRUDs for User Perf----------
	

}
