/*
 * ------------------------------------------------------------------------------
 * Author		:	Mayukh Das
 * Description 	:	This class file contains methods for Refreshing a Syncing
 * 					data with server where and when necessary
 * ------------------------------------------------------------------------------
 */

package com.openwords.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.openwords.util.InternetCheck;
import com.openwords.util.TimeConvertor;
import com.openwords.util.preference.OpenwordsSharedPreferences;

import android.content.Context;
import android.util.Log;

public class InitDatabase {
	public static String url_get_user_perf_summary = "http://geographycontest.ipage.com/OpenwordsOrg/getSummaryPerformance.php";
	public static String url_writeback_user_perf = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/writeBackUserPerf.php";
	public static String url_get_user_words="http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/getUserWordsRecords.php";
	public static String url_writeback_user_words="http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/writeBackUserWords.php";
	public static UserInfo user;
	public static int userId;
	public static int prevUser;
	public static List<UserPerformanceDirty> dirtyPerf;
	//-----
	public static void checkAndRefreshPerf(Context ctx, int module, int override)
	{
		user=OpenwordsSharedPreferences.getUserInfo();
		userId = user.getUserId();
		prevUser = user.getLast_userid();
		dirtyPerf = UserPerformanceDirty.listAll(UserPerformanceDirty.class);
		List<UserWords> uwList = UserWords.listAll(UserWords.class);
		
		boolean connected = InternetCheck.checkConn(ctx);
		//if Dirty performance has records for this user...
		if(dirtyPerf.size()>0)
		{
			if(connected==true)
			{
				Log.d("chkpnt0", "before write back perf");
				int success = InitDatabase.writeBackUserPerformance();
	                if(success==1)
	                {
	                	UserPerformanceDirty.deleteByUser(userId);
	                	                	
	                }
				
			}
			else
			{Log.d("msg", "not connected");}
			
			//Updating summary performance----------------------
						
			InitDatabase.updateLocalPerformanceSummary(ctx);
		}
		//------------------------------
		
		if(connected==true) //if internet connection is available.
		{
				
			
			//deleting all User words data
			if(userId!=prevUser || override == 1)
			{
				Log.d("checkpoint1", "----before delete---");
				UserWords.deleteAll(UserWords.class);
				WordTranscription.deleteAll(WordTranscription.class);
				InitDatabase.loadUserWords(ctx, OpenwordsSharedPreferences.getUserInfo().getLang_id(), userId);
				Log.d("place", "loading user words");
				//deleting all user performance data for particular user
				UserPerformance.deleteByUser(userId);
				
				List<UserWords> uwListLocal = UserWords.listAll(UserWords.class);
				for(int i=0;i<uwListLocal.size();i++)
				{
					
					InitDatabase.loadPerformanceSummary(ctx,userId,uwListLocal.get(i).connectionId,module);
				}
				
			}
			//Loading user performance summary
			
		}
		else
		{
			Log.d("msg", "not connected---Cannot refresh!");
		}
	}
	
	public static void loadPerformanceSummary(Context ctx,int user,int con,int mod){
		
		Log.d("paramsInPerfSum", "="+user+con+mod);
		
		List<UserPerformance> upTempList = UserPerformance.findByUserConnection(user, con);
		if(upTempList.size()!=0)
			//Log.d("..", "");
			UserPerformance.deleteByUserConnection(user, con);
		try
		{
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("user",Integer.toString(user)));
			params1.add(new BasicNameValuePair("connectionid",Integer.toString(con)));
			//params1.add(new BasicNameValuePair("module",Integer.toString(mod)));
			JSONParser jsonParse = new JSONParser();
			JSONObject jObj = jsonParse.makeHttpRequest(url_get_user_perf_summary, "POST", params1);
			if(jObj.getInt("success")==1)
			{
			JSONArray jArr = jObj.getJSONArray("data");
			for(int i=0;i<jArr.length();i++)
			{
				JSONObject childObj = jArr.getJSONObject(i);
				
				UserPerformance upRec = new UserPerformance(childObj.getInt("connectionid"),childObj.getInt("userid"),childObj.getInt("module"),
						 childObj.getInt("total_correct"), childObj.getInt("total_close"),
						childObj.getInt("totalSkipped"), childObj.getInt("total_expose"),
						childObj.getInt("last_time"), childObj.getInt("last_performance"),
						0,ctx);
				upRec.save();
			}
			}else{Log.d("message", jObj.getString("message"));}
	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadUserWords(Context ctx, int languageTwoId, int userId)
	{
			Log.d("params", "_"+languageTwoId+userId);
		try
		{
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("language",Integer.toString(languageTwoId)));
			params1.add(new BasicNameValuePair("user",Integer.toString(userId)));
			JSONParser jsonParse = new JSONParser();
			JSONObject jObj = jsonParse.makeHttpRequest(url_get_user_words, "POST", params1);
			
			if(jObj.getInt("success")==1)
			{
			JSONArray jArr = jObj.getJSONArray("data");
			for(int i=0;i<jArr.length();i++)
			{
				JSONObject childObj = jArr.getJSONObject(i);
				UserWords uw = new UserWords(childObj.getInt("connection_id"),
						childObj.getInt("wordl2id"),childObj.getString("wordl2name"),
						childObj.getInt("wordl1id"),childObj.getString("wordl1name"),
						childObj.getInt("l2id"),childObj.getString("l2name"),childObj.getString("audiocall"),
						childObj.getInt("fresh"));
				uw.save();
				
				//loading transcription
				
				WordTranscription.insertMerge(childObj.getInt("wordl2id"), childObj.getString("trans"));
				//rest of the code to be written for word transcription...
			}
			}else{Log.d("message", jObj.getString("data"));}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//write back user performance to server.
	public static int writeBackUserPerformance()
	{
		 int success = 0;
		try
		{
			
			JSONArray ja = new JSONArray();
			JSONObject jParent = new JSONObject();
			
			for(int i=0;i<dirtyPerf.size();i++)
			{
				JSONObject jo = new JSONObject();
				jo.put("connection_id", dirtyPerf.get(i).connection_id);
				jo.put("user_id", dirtyPerf.get(i).user_id);
				jo.put("type", dirtyPerf.get(i).type);
				jo.put("perf", dirtyPerf.get(i).performance);
				jo.put("time", dirtyPerf.get(i).time);
				jo.put("user_ex", dirtyPerf.get(i).user_exclude);
				
				ja.put(jo);
			}
			
			jParent.put("data", ja); //packing records as JSON object
			Log.d("while writing back user perf******",jParent.toString());
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("params",jParent.toString()));
			JSONParser jsonParse = new JSONParser();
            JSONObject jObj = jsonParse.makeHttpRequest(url_writeback_user_perf, "POST", params1);
            success = jObj.getInt("success");
           
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		 return success;
	}
	
	
	//method for updating summary --------------
	
	public static void updateLocalPerformanceSummary(Context ctx)
	{
		List<UserPerformanceDirty> dirtyPerf = UserPerformanceDirty.findByTime(userId, OpenwordsSharedPreferences.getUserInfo().getLastPerfUpd());
		for(int i=0;i<dirtyPerf.size();i++)
		{
    		//InitDatabase.loadPerformanceSummary(ctx,userId,dirtyPerf.get(i).connection_id,module);
			List<UserPerformance> upList = UserPerformance.findByUserConnectionModule(userId, dirtyPerf.get(i).connection_id, dirtyPerf.get(i).type);
			Log.d("data indirty", Integer.toString(dirtyPerf.get(0).user_id));
			if(upList.size()>0) //if record exists then update
			{
				UserPerformance.updateById(upList.get(0).getId(), dirtyPerf.get(i).performance, dirtyPerf.get(i).time);
			}
			else //else insert
			{
				int totSkp=0,totClose=0,totExp=0,totCor=0;
				if(dirtyPerf.get(i).type>0)
				{
					switch(dirtyPerf.get(i).performance)
					{
					case 0: totExp=1;
					case 1: totSkp=1; totExp=1;
					case 2: totClose=1; totExp=1;
					case 3: totCor=1; totExp=1;
					}
				}
				else
				{
					totExp=1;
				}
				
				UserPerformance up = new UserPerformance(dirtyPerf.get(i).connection_id,
						dirtyPerf.get(i).user_id,dirtyPerf.get(i).type,totClose,totCor,totSkp,totExp,
						dirtyPerf.get(i).time,dirtyPerf.get(i).performance,0,ctx);
				up.save();
			
			}
		}
		
		
		//updating last update time.-------
		UserInfo uInf = OpenwordsSharedPreferences.getUserInfo();
		uInf.setLastPerfUpd(TimeConvertor.getUnixTime());
		OpenwordsSharedPreferences.setUserInfo(uInf);
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////
	public static void updateBackUserWords(int user,int connection, int fresh)
	{
		
			try
			{
				
				
				//building params
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				params1.add(new BasicNameValuePair("user",Integer.toString(user)));
				params1.add(new BasicNameValuePair("con",Integer.toString(connection)));
				params1.add(new BasicNameValuePair("fresh",Integer.toString(fresh)));
				JSONParser jsonParse = new JSONParser();
				JSONObject jObj = jsonParse.makeHttpRequest(url_writeback_user_words, "POST", params1);
                if(jObj.getInt("success")==1)
                	//UserWords.deleteAll(UserWords.class);
                	Log.d("writeback", "success");
			
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		
	}

}

