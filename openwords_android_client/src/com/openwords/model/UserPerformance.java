/* --------------------------------------------------------------------
 * Author: Mayukh Das
 * Purpose: Class file for USER_PERFORMANCE table on client 
 * 			(stores summarized performances)
 * Components: Constructors, Custom functions
 * --------------------------------------------------------------------
 */

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
	public long last_time;
	public int last_performance;
	public int l_one_id;
	public int l_two_id;
	public int user_exclude;
	public UserPerformance() {
		// TODO Auto-generated constructor stub
	}
	
	//override constructor
	public UserPerformance(int connection_id,int user_id,
			int total_correct, int total_skipped, int total_exposure, long last_time,
			int last_performance, int user_exclude)
	{
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
				int total_correct, int total_skipped, int total_exposure, long last_time,
				int last_performance, int user_exclude, Context c)
		{
			
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
		
		//override constructor
				public UserPerformance(int connection_id,int user_id, int module, int total_close,
						int total_correct, int total_skipped, int total_exposure, long last_time,
						int last_performance, int l1Id,int l2Id, int user_exclude, Context c)
				{
					
					this.connection_id=connection_id;
					this.user_id=user_id;
					this.module = module;
					this.total_correct=total_correct;
					this.total_close=total_close;
					this.total_skipped=total_skipped;
					this.total_exposure=total_exposure;
					this.last_time=last_time;
					this.last_performance=last_performance;
					this.l_one_id=l1Id;
					this.l_two_id=l2Id;
					this.user_exclude=user_exclude;
				}
	
	//************ Method to Find Performance records by User & connection ID*********
	public static List<UserPerformance> findByUserConnection(int user_id, int connection_id)
	{
		List<UserPerformance> result = UserPerformance.find(UserPerformance.class, "connectionid="+connection_id+" and userid="+user_id);
		return result;
	}
	//********************************************************************************
	
	//*********** Method to Find Performance records by User *************************
	public static List<UserPerformance> findByUser(int user_id)
	{
		List<UserPerformance> result = UserPerformance.find(UserPerformance.class, "userid="+user_id);
		//Log.d("size of result", Integer.toString(result.size()));
		return result;
	}
	//********************************************************************************
	
	
	//************ Method to Find Performance records by User & 2nd Language *********
		
	public static List<UserPerformance> findByUserLanguage(int user_id, int language_id) {
	
		List<UserPerformance> result = UserPerformance.find(UserPerformance.class, "userid="+user_id+" and ltwoid="+language_id);
		return result;
	}
	//********************************************************************************
	
	//*** Method to Find Performance records by User, connection ID and learning module *********
	public static List<UserPerformance> findByUserConnectionModule(int user, int con,int module)
	{
		List<UserPerformance> upList = UserPerformance.find(UserPerformance.class, "userid="+user+" and connectionid="+con+" and module="+module);
		return upList;
	}
	//********************************************************************************
	
	
	
	//*** Method to Find Performance records by User and 2nd Language w/ Audio *********
	public static List<UserPerformance> findByUserLanguageWithAudio(int user_id, int language_id)
	{
		List<UserWords> wordlist = UserWords.findByLanguageWithAudio(language_id);
		List<UserPerformance> result = new ArrayList<UserPerformance>();
			for(UserWords word : wordlist) {
				List<UserPerformance> perform = UserPerformance.find(UserPerformance.class, "userid="+user_id+" and connectionid="+word.connectionId);
				if(perform.size()==0) break;
				else result.add(perform.get(0));
			}
		return result;
	}
	//********************************************************************************
	
	//************ Method to delete Performance records by User ************************
	public static void deleteByUser(int user_id)
	{
		UserPerformance.deleteAll(UserPerformance.class, "userid="+user_id);
	}
	//********************************************************************************
	
	
	//************ Method to delete Performance records by User & Connection ID *******
	public static void deleteByUserConnection(int user_id, int connection_id)
	{
		UserPerformance.deleteAll(UserPerformance.class, "userid="+user_id+" AND connectionid="+connection_id);
	}
	//********************************************************************************
	
	
	//************ Method to update Performance records by ID  ************************
	public static void updateById(Long id, int perf, long time)
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
	//********************************************************************************
	
	public String toString() {
		return "connection_id: "+connection_id+";user_id: "+user_id+";total_correct: "+
				total_correct+";total_skipped:"+total_skipped+";total_exposure: "+total_exposure+";last_time: "+last_time+
				";last_performance"+last_performance;
	}
	
	
	//**************** method for calculating number of words learned **********
	public static int findNumberOfWordsLearnt(int user_id, int language_id) {
		
		List<UserPerformance> result = UserPerformance.find(UserPerformance.class, "userid="+user_id+" and ltwoid="+language_id);
		ArrayList<Integer> wordCons = new ArrayList<Integer>();
		for (int i=0; i<result.size();i++)
		{
			if(result.get(i).module!=0)
			{
				int tWrong = result.get(i).total_exposure - (result.get(i).total_close+result.get(i).total_correct
						+result.get(i).total_skipped);
				if(result.get(i).total_correct+result.get(i).total_close > tWrong && !(wordCons.contains(result.get(i).connection_id)))
				{
					wordCons.add(result.get(i).connection_id);
				}
				else
				{
					if(result.get(i).last_performance >= 2)
					{
						wordCons.add(result.get(i).connection_id);
					}
						
				}
			}
		}
		return wordCons.size();
	}
	//********************************************************************************

}
