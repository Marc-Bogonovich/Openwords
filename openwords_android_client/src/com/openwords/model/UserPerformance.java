package com.openwords.model;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class UserPerformance extends SugarRecord<UserPerformance> {
	
	public int connection_id;
	public int user_id;
	public int total_correct;
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
	public UserPerformance(int connection_id,int user_id, 
			int total_correct, int total_skipped, int total_exposure, int last_time,
			int last_performance, int user_exclude, Context c)
	{
		super(c);
		this.connection_id=connection_id;
		this.user_id=user_id;
		this.total_correct=total_correct;
		this.total_skipped=total_skipped;
		this.total_exposure=total_exposure;
		this.last_time=last_time;
		this.last_performance=last_performance;
		this.user_exclude=user_exclude;
	}
	
	public static List<UserPerformance> findByUserConnectionUser(int user_id, int connection_id)
	{
		List<UserPerformance> result = UserPerformance.find(UserPerformance.class, "connectionid=? and userid=?", Integer.toString(connection_id),
				Integer.toString(user_id));
		return result;
	}

}
