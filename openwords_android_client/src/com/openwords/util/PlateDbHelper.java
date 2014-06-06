package com.openwords.util;


import java.util.ArrayList;

import com.openwords.dto.PlateDbDto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// THIS IS THE CODE CREATED BY CHRIS WHICH MANIPULATES Plate_DB
public class PlateDbHelper   extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = OpenwordsDatabaseManager.Plate_DB.TABLE_NAME+".db";
	private static final String SQL_CREATE_PLATE = "CREATE TABLE IF NOT EXISTS " + OpenwordsDatabaseManager.Plate_DB.TABLE_NAME + " ("+OpenwordsDatabaseManager.Plate_DB.CONNECTIONID + " INTEGER,"
			+ OpenwordsDatabaseManager.Plate_DB.WORDLtwo + " TEXT," + OpenwordsDatabaseManager.Plate_DB.WORDLone + " TEXT," + OpenwordsDatabaseManager.Plate_DB.TRANSCRIPTION + " TEXT,"
			+ OpenwordsDatabaseManager.Plate_DB.PERF + " BOOLEAN"+")";
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
	
	public ArrayList<PlateDbDto> getPlate(int user)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<PlateDbDto> newPlate = new ArrayList<PlateDbDto>();
		
   	 
        Cursor cursor = db.query(OpenwordsDatabaseManager.Plate_DB.TABLE_NAME, 
        		new String[] { OpenwordsDatabaseManager.Plate_DB.CONNECTIONID,
        		OpenwordsDatabaseManager.Plate_DB.WORDLtwo, 
        		OpenwordsDatabaseManager.Plate_DB.WORDLone,
        		OpenwordsDatabaseManager.Plate_DB.TRANSCRIPTION,
        		OpenwordsDatabaseManager.Plate_DB.PERF}, null,
                null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
        	PlateDbDto newProblem = new PlateDbDto(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
        			cursor.getString(3),cursor.getInt(4));
        	newPlate.add(newProblem);
        	cursor.moveToNext();
        }
        	
        return newPlate;
	}
	
}

