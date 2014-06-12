package com.openwords.DAO;

import java.util.ArrayList;

//import com.openwords.dto.PlateDbDto;
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
        values.put(OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID,up.connection_id);
        values.put(OpenwordsDatabaseManager.UserPerfDB.USERID, up.user_id);
        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALCORRECT, up.total_correct);
        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALSKIPPED, up.total_skipped);
        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALEXPOSURE, up.total_exposure);
        values.put(OpenwordsDatabaseManager.UserPerfDB.LASTTIME, up.last_time);
        values.put(OpenwordsDatabaseManager.UserPerfDB.LASTPERFORMANCE, up.last_performance);
        values.put(OpenwordsDatabaseManager.UserPerfDB.USEREXCLUDE, 0);
        
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
		 {
	            cursor.moveToFirst();
	            while(!cursor.isAfterLast())
		        {
				 UserPerfDto newPerf = new UserPerfDto(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
		        			cursor.getInt(3),cursor.getInt(4), cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
				 	result.add(newPerf);
		        	cursor.moveToNext();
		        }
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
		 {
	            cursor.moveToFirst();
	            while(!cursor.isAfterLast())
		        {
				 UserPerfDto newPerf = new UserPerfDto(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
		        			cursor.getInt(3),cursor.getInt(4), cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
				 	result.add(newPerf);
		        	cursor.moveToNext();
		        }
		 
		 }
	        db.close();	
	        return result;
		
	}
	
	//************ get distinct user ***********
	public int getUserFromUserPerf()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT distinct user_id FROM "+OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME,
				null);
		if(c!=null)
		{
			c.moveToFirst();
			return c.getInt(0);
		}
		else
			return 0;
	}
	
	//************ Delete all**************
	public void deleteAllUserPerf()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME, null, null);
		db.close();
	}
	
	//*********InsertUpdate->merge********
	public void insertUpdateMerge(UserPerfDto up)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor readCursor = db.query(OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME,
				new String[]{OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID,
				OpenwordsDatabaseManager.UserPerfDB.USERID}, 
				OpenwordsDatabaseManager.UserPerfDB.USERID+"="+up.user_id
				+" AND "+OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID+"="+up.connection_id,
				null, null, null, null);
		db.close();
		
		db=this.getWritableDatabase();
		if(readCursor.getCount()>0)
		{
			ContentValues values = new ContentValues();
	        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALCORRECT, up.total_correct);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALSKIPPED, up.total_skipped);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALEXPOSURE, up.total_exposure);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.LASTTIME, up.last_time);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.LASTPERFORMANCE, up.last_performance);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.USEREXCLUDE, up.user_exclude);
	        
	        db.update(OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME, values,
	        		OpenwordsDatabaseManager.UserPerfDB.USERID+"="+up.user_id+
	        		" AND "+OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID+"="+up.connection_id,
	        		null);
	        db.close();
		}
		else
		{
			ContentValues values = new ContentValues();
	        values.put(OpenwordsDatabaseManager.UserPerfDB.CONNECTIONID,up.connection_id);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.USERID, up.user_id);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALCORRECT, up.total_correct);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALSKIPPED, up.total_skipped);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.TOTALEXPOSURE, up.total_exposure);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.LASTTIME, up.last_time);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.LASTPERFORMANCE, up.last_performance);
	        values.put(OpenwordsDatabaseManager.UserPerfDB.USEREXCLUDE, up.user_exclude);
	        
	        db.insert(OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME, null, values);
	        db.close();
		}
			
	}
	//---------------------------------------------------------------
	
	
	//---------CRUDs for User Perf Dirty------------------------------
	public int getCountDirty()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(OpenwordsDatabaseManager.UserPerfDB.TABLE_NAME_D,
				new String[]{OpenwordsDatabaseManager.UserPerfDB.D_CONNECTIONID}, null, null, null, null, null);
		int cnt = c.getCount();
		db.close();
		return cnt;
	}
	
	
	//----------------------------------------------------------------

}
