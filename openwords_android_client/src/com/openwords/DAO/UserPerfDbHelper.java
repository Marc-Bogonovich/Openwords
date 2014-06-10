package com.openwords.DAO;

import java.util.ArrayList;

import com.openwords.dto.PlateDbDto;
import com.openwords.dto.UserPerfDto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	public void addUserPerf(UserPerfDto up)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
        values.put(OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID,Integer.toString(up.connection_id));
        values.put(OpenwordsDatabaseManager.UserPerfDB.USERID, Integer.toString(up.user_id));
        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALCORRECT, Integer.toString(up.total_correct));
        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALSKIPPED, Integer.toString(up.total_skipped));
        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALEXPOSURE, Integer.toString(up.total_skipped));
        values.put(OpenwordsDatabaseManager.UserPerfDB.LASTTIME, Integer.toString(up.last_time));
        values.put(OpenwordsDatabaseManager.UserPerfDB.LASTPERFORMANCE, Integer.toString(up.last_performance));
        values.put(OpenwordsDatabaseManager.UserPerfDB.USEREXCLUDE, "");
        
        db.insert(OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME, null, values);
		db.close();
	}
	
	public ArrayList<UserPerfDto> getAll()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<UserPerfDto> result = new ArrayList<UserPerfDto>();
		
		Cursor cursor = db.query(OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME, 
				new String[] {OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID,
				OpenwordsDatabaseManager.UserPerfDB.USERID,
				OpenwordsDatabaseManager.UserPerfDB.TOTALCORRECT,
				OpenwordsDatabaseManager.UserPerfDB.TOTALSKIPPED,
				OpenwordsDatabaseManager.UserPerfDB.TOTALEXPOSURE,
				OpenwordsDatabaseManager.UserPerfDB.LASTTIME,
				OpenwordsDatabaseManager.UserPerfDB.LASTPERFORMANCE,
				OpenwordsDatabaseManager.UserPerfDB.USEREXCLUDE}, null, null, null, null, null);
		 if (cursor != null)
	            cursor.moveToFirst();
		 while(!cursor.isAfterLast())
	        {
			 UserPerfDto newPerf = new UserPerfDto(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
	        			cursor.getInt(3),cursor.getInt(4), cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
			 	result.add(newPerf);
	        	cursor.moveToNext();
	        }
	        db.close();	
	        
	        return result;
		
	}
	
	
	public ArrayList<UserPerfDto> getByConnectionId(int connection_id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<UserPerfDto> result = new ArrayList<UserPerfDto>();
		
		Cursor cursor = db.query(OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME, 
				new String[] {OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID,
				OpenwordsDatabaseManager.UserPerfDB.USERID,
				OpenwordsDatabaseManager.UserPerfDB.TOTALCORRECT,
				OpenwordsDatabaseManager.UserPerfDB.TOTALSKIPPED,
				OpenwordsDatabaseManager.UserPerfDB.TOTALEXPOSURE,
				OpenwordsDatabaseManager.UserPerfDB.LASTTIME,
				OpenwordsDatabaseManager.UserPerfDB.LASTPERFORMANCE,
				OpenwordsDatabaseManager.UserPerfDB.USEREXCLUDE}, 
				OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID+"="+connection_id, null, null, null, null);
		 if (cursor != null)
	            cursor.moveToFirst();
		 while(!cursor.isAfterLast())
	        {
			 UserPerfDto newPerf = new UserPerfDto(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
	        			cursor.getInt(3),cursor.getInt(4), cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
			 	result.add(newPerf);
	        	cursor.moveToNext();
	        }
		 	
	        db.close();	
	        return result;
		
	}
	//---------------------------------------------------------------
	
	
	//---------CRUDs for User Perf Dirty------------------------------
	
	//----------------------------------------------------------------

}
