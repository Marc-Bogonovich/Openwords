package com.openwords.model;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class UserPerformanceDirty extends SugarRecord<UserPerformanceDirty> {

	public int connection_id;
	public int user_id;
	public int type;
	public int performance;
	public int time;
	public int user_exclude;
	
	public UserPerformanceDirty(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public UserPerformanceDirty(int connection_id,int user_id, int type,
			int last_time, int performance,
			 int user_exclude, Context c)
	{	
		super(c);
		this.connection_id=connection_id;
		this.user_id=user_id;
		this.type=type;
		this.time=last_time;
		this.performance=performance;
		this.user_exclude=user_exclude;
	
	}
	
	public static List<UserPerformanceDirty> findByUserConnection(int user_id, int connection_id)
	{
		List<UserPerformanceDirty> result = UserPerformanceDirty.find(UserPerformanceDirty.class, "connectionid="+connection_id+" and userid="+user_id);
		return result;
	}
	
	public static List<UserPerformanceDirty> findByUser(int user_id)
	{
		List<UserPerformanceDirty> result = UserPerformanceDirty.find(UserPerformanceDirty.class, "userid="+user_id);
		return result;
	}
	
	public static List<UserPerformanceDirty> findByUserConnectionType(int user_id, int con, int type)
	{
		List<UserPerformanceDirty> result = UserPerformanceDirty.find(UserPerformanceDirty.class, "userid="+user_id+" and connectionid="+con+" and type="+type);
		return result;
	}
	
	public static List<UserPerformanceDirty> findByTime(int user, long time)
	{
		List<UserPerformanceDirty> result = UserPerformanceDirty.find(UserPerformanceDirty.class, "userid="+user+" and time >="+time);
		return result;
	}
	
	
	public static void deleteByUser(int user_id)
	{
		UserPerformanceDirty.executeQuery("Delete from user_performance_dirty where userid="+user_id);
	}
}
