package com.openwords.util;

import android.provider.BaseColumns;

//Here we are storing the metadata for the tables of our database
public class OpenwordsDatabaseManager {
	public static abstract class Plate_DB implements BaseColumns{
		public static final String TABLE_NAME = "Plate_db";
		public static final String PROBLEM = "problem";
		public static final String ANSWER = "answer";
		public static final String PERF = "perf";
		
	}
	
	public static abstract class UserPerfDB implements BaseColumns{
		
	}
	
	public static abstract class UserWordsDB implements BaseColumns{
		
	}
}
