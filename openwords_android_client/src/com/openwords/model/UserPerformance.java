package com.openwords.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.orm.SugarRecord;

public class UserPerformance extends SugarRecord<UserPerformance>  {
	
	public int connection_id;
	public int user_id;
	public int module;
	public int total_correct;
	public int total_close;
	public int total_skipped;
	public int total_exposure;
	public int last_time;
	public int last_performance;
	public int user_exclude;
	public UserPerformance(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//override constructor
	public UserPerformance(Context c, int connection_id,int user_id,
			int total_correct, int total_skipped, int total_exposure, int last_time,
			int last_performance, int user_exclude)
	{
		super(c);
		this.connection_id=connection_id;
		this.user_id=user_id;
		this.module = 0;
		this.total_correct=total_correct;
		this.total_skipped=total_skipped;
		this.total_exposure=total_exposure;
		this.last_time=last_time;
		this.last_performance=last_performance;
		this.user_exclude=user_exclude;
	}
	
	//override constructor
		public UserPerformance(int connection_id,int user_id, int module, int total_close,
				int total_correct, int total_skipped, int total_exposure, int last_time,
				int last_performance, int user_exclude, Context c)
		{
			super(c);
			this.connection_id=connection_id;
			this.user_id=user_id;
			this.module = module;
			this.total_correct=total_correct;
			this.total_close=total_close;
			this.total_skipped=total_skipped;
			this.total_exposure=total_exposure;
			this.last_time=last_time;
			this.last_performance=last_performance;
			this.user_exclude=user_exclude;
		}
	
	public static List<UserPerformance> findByUserConnection(int user_id, int connection_id)
	{
		List<UserPerformance> result = UserPerformance.find(UserPerformance.class, "connectionid="+connection_id+" and userid="+user_id);
		return result;
	}
	
	public static List<UserPerformance> findByUser(int user_id)
	{
		List<UserPerformance> result = UserPerformance.find(UserPerformance.class, "userid="+user_id);
		//Log.d("size of result", Integer.toString(result.size()));
		return result;
	}
	
	public static List<UserPerformance> findByUserLanguage(int user_id, int language_id) {
		List<UserWords> wordlist = UserWords.findByLanguage(language_id);
		List<UserPerformance> result = new ArrayList<UserPerformance>();
		if(wordlist!=null) {
			for(UserWords word : wordlist) {
				result.add(UserPerformance.find(UserPerformance.class, "userid="+user_id+" and connectionid="+word.connectionId).get(0));
			}
		}
		return result;
	}
	
	public static List<UserPerformance> findByUserConnectionModule(int user, int con,int module)
	{
		List<UserPerformance> upList = UserPerformance.find(UserPerformance.class, "userid="+user+" and connectionid="+con+" and module="+module);
		return upList;
	}
	
	public static void deleteByUser(int user_id)
	{
		UserPerformance.deleteAll(UserPerformance.class, "userid="+user_id);
	}
	public static void deleteByUserConnection(int user_id, int connection_id)
	{
		UserPerformance.deleteAll(UserPerformance.class, "userid="+user_id+" AND connectionid="+connection_id);
	}
	
	public static void updateById(Long id, int perf, int time)
	{
		switch(perf)
		{
		case 0:	UserPerformance.executeQuery("update user_performance set totalexposure=totalexposure+1,"
				+ " lasttime="+time+",lastperformance="+perf+" where id="+id);
		case 1: UserPerformance.executeQuery("update user_performance set totalskipped=totalskipped+1,"
				+ "totalexposure=totalexposure+1, lasttime="+time+",lastperformance="+perf+" where id="+id);
		case 2: UserPerformance.executeQuery("update user_performance set totalclose=totalclose+1,"
				+ "totalexposure=totalexposure+1, lasttime="+time+",lastperformance="+perf+" where id="+id);
		case 3: UserPerformance.executeQuery("update user_performance set totalcorrect=totalcorrect+1,"
				+ "totalexposure=totalexposure+1, lasttime="+time+",lastperformance="+perf+" where id="+id);
		}
	}
	
	public String toString() {
		return "connection_id: "+connection_id+";user_id: "+user_id+";total_correct: "+
				total_correct+";total_skipped:"+total_skipped+";total_exposure: "+total_exposure+";last_time: "+last_time+
				";last_performance"+last_performance;
	}

}
