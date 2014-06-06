package com.openwords.DAO;

import android.provider.BaseColumns;

//Here we are storing the metadata for the tables of our database
public class OpenwordsDatabaseManager {
	public static abstract class Plate_DB implements BaseColumns{
		public static final String TABLE_NAME = "Plate_db";
		public static final String CONNECTIONID = "connection_id";
		public static final String WORDLtwo = "wordL2";
		public static final String WORDLone = "wordL1";
		public static final String TRANSCRIPTION = "transcriptionL2";
		public static final String PERF = "perf";
		public static final String EXPOSURE = "exposure";
		
	}
	
	public static abstract class UserPerfDB implements BaseColumns{
		//--------------Main performance table-----------
		public static final String TABLE_NAME = "User_perf";
		public static final String USERID = "user_id";
		public static final String CONNECTIONID = "connection_id";
		public static final String TOTALCORRECT = "total_correct";
		public static final String TOTALSKIPPED = "total_skipped";
		public static final String TOTALEXPOSURE = "total_exposure";
		public static final String LASTTIME = "last_time";
		public static final String LASTPERFORMANCE = "last_performance";
		public static final String USEREXCLUDE = "user_exclude";
		//------------------------------------------------
		
		//----------Dirty Performance table----------------
		public static final String TABLE_NAME_D = "User_perf";
		public static final String D_USERID = "user_id";
		public static final String D_CONNECTIONID = "connection_id";
		public static final String D_TOTALCORRECT = "total_correct";
		public static final String D_TOTALSKIPPED = "total_skipped";
		public static final String D_TOTALEXPOSURE = "total_exposure";
		public static final String D_LASTTIME = "last_time";
		public static final String D_LASTPERFORMANCE = "last_performance";
		public static final String D_USEREXCLUDE = "user_exclude";
		//-------------------------------------------------
	}
	
	public static abstract class UserWordsDB implements BaseColumns{
		
	}
}
