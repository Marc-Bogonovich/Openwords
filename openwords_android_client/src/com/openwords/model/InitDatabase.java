package com.openwords.model;

import com.openwords.DAO.PlateDbHelper;
import com.openwords.DAO.UserPerfDbHelper;
import com.openwords.DAO.UserWordsDbHelper;

import android.content.Context;

public class InitDatabase {
	public Context context;
	public PlateDbHelper plateDb;
	public UserPerfDbHelper userPerfDb;
	public UserWordsDbHelper userWordsDb;
	
	public InitDatabase(Context c)
	{
		this.context = c;
	}
	
	public void CreateDatabase()
	{
		this.plateDb = new PlateDbHelper(this.context);
		this.userPerfDb = new UserPerfDbHelper(this.context);
		this.userWordsDb = new UserWordsDbHelper(this.context);
	}
	
	public void RefreshTables(String url_get_from_server)
	{
		this.plateDb.deleteAll();
		this.userPerfDb.deleteAllUserPerf();
		this.userWordsDb.deleteAllUserWords();
		this.userWordsDb.deleteAllTranscriptions();
		
		//-----read from server-----
		try
		{
			//------------ for User Perf-------
			//------------ for User words and TRanscriptions-------
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
