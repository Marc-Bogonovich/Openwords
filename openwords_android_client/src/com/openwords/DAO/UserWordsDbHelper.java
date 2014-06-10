package com.openwords.DAO;

import java.util.ArrayList;

import com.openwords.dto.UserWordsDto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserWordsDbHelper extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = OpenwordsDatabaseManager.UserWordsDB.TABLE_NAME;
	private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + OpenwordsDatabaseManager.UserWordsDB.CONNECTION_ID + " INTEGER,"
	+ OpenwordsDatabaseManager.UserWordsDB.WORDL2ID+" INTEGER,"+ OpenwordsDatabaseManager.UserWordsDB.WORDL2+" TEXT,"
	+ OpenwordsDatabaseManager.UserWordsDB.WORDL1ID+" INTEGER,"+OpenwordsDatabaseManager.UserWordsDB.WORDL1+" TEXT,"
	+ OpenwordsDatabaseManager.UserWordsDB.L2ID+" INTEGER,"+OpenwordsDatabaseManager.UserWordsDB.L2NAME+" TEXT,"
	+ OpenwordsDatabaseManager.UserWordsDB.AUDIO+" BLOB"+")";
	private static final String SQL_DELETE = "DROP TABLE IF EXISTS "+ OpenwordsDatabaseManager.UserWordsDB.TABLE_NAME;
	
	public UserWordsDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE);
		db.execSQL(SQL_CREATE);
		
	}
	
	public void addWord(UserWordsDto uw)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(OpenwordsDatabaseManager.UserWordsDB.CONNECTION_ID, uw.connection_id);
		cv.put(OpenwordsDatabaseManager.UserWordsDB.WORDL2ID, uw.wordl2_id);
		cv.put(OpenwordsDatabaseManager.UserWordsDB.WORDL2, uw.wordl2);
		cv.put(OpenwordsDatabaseManager.UserWordsDB.WORDL1ID, uw.wordl1_id);
		cv.put(OpenwordsDatabaseManager.UserWordsDB.WORDL1, uw.wordl1);
		cv.put(OpenwordsDatabaseManager.UserWordsDB.L2ID, uw.l2_id);
		cv.put(OpenwordsDatabaseManager.UserWordsDB.L2NAME, uw.l2_name);
		cv.put(OpenwordsDatabaseManager.UserWordsDB.AUDIO, "");
		
		//inserting
		db.insert(OpenwordsDatabaseManager.UserWordsDB.TABLE_NAME, null, cv);
		db.close();
	}
	
	public ArrayList<UserWordsDto> getAll()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<UserWordsDto> result = new ArrayList<UserWordsDto>();
		Cursor cursor = db.query(OpenwordsDatabaseManager.UserWordsDB.TABLE_NAME, 
				new String[] {OpenwordsDatabaseManager.UserWordsDB.CONNECTION_ID,
				OpenwordsDatabaseManager.UserWordsDB.WORDL2ID,
				OpenwordsDatabaseManager.UserWordsDB.WORDL2,
				OpenwordsDatabaseManager.UserWordsDB.WORDL1ID,
				OpenwordsDatabaseManager.UserWordsDB.WORDL1,
				OpenwordsDatabaseManager.UserWordsDB.L2ID,
				OpenwordsDatabaseManager.UserWordsDB.L2NAME,
				OpenwordsDatabaseManager.UserWordsDB.AUDIO}, null, null, null, null, null);
		if (cursor != null)
		{
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
            	UserWordsDto newWord = new UserWordsDto(cursor.getInt(0),cursor.getInt(1),
            			cursor.getString(2),cursor.getInt(3),cursor.getString(4),cursor.getInt(5),
            			cursor.getString(6));
            	result.add(newWord);
            }
            
		}
		return result;
	}
	
}
