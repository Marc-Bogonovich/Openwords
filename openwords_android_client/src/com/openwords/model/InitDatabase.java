package com.openwords.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.openwords.util.InternetCheck;
import com.openwords.util.preference.OpenwordsSharedPreferences;

import android.content.Context;
import android.util.Log;

public class InitDatabase {
	public static String url_get_user_perf_summary = "http://geographycontest.ipage.com/OpenwordsOrg/getSummaryPerformance.php";
	public static String url_writeback_user_perf = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/writeBackUserPerf.php";
	public static String url_get_user_words="http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/getUserWordsRecords.php";
	public static String url_writeback_user_words="http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/writeBackUserWords.php";
	public static UserInfo user;
	//-----
	public static void checkAndRefreshPerf(Context ctx, int module)
	{
		user=OpenwordsSharedPreferences.getUserInfo();
		int userId = user.getUserId();
		int prevUser = user.getLast_userid();
		List<UserPerformanceDirty> dirtyPerf = UserPerformanceDirty.listAll(UserPerformanceDirty.class);
		List<UserWords> uwList = UserWords.listAll(UserWords.class);
		
		boolean connected = InternetCheck.checkConn(ctx);
		//if Dirty performance has records for this user
		if(dirtyPerf.size()>0)
		{
			if(connected==true)
			{
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
					
					List<NameValuePair> params1 = new ArrayList<NameValuePair>();
					params1.add(new BasicNameValuePair("params",jParent.toString()));
					JSONParser jsonParse = new JSONParser();
	                JSONObject jObj = jsonParse.makeHttpRequest(url_writeback_user_perf, "POST", params1);
	                if(jObj.getInt("success")==1)
	                	UserPerformanceDirty.deleteByUser(userId);
				}catch(Exception e)
				{ e.printStackTrace();}
			}
			else
			{Log.d("msg", "not connected");}
		}
		//------------------------------
		
		if(connected==true)
		{
				
			
			//deleting all User words data
			if(userId!=prevUser || user.getLang_id()!=uwList.get(0).lTwoId)
			{
				UserWords.deleteAll(UserWords.class);
				InitDatabase.loadUserWords(ctx, OpenwordsSharedPreferences.getUserInfo().getLang_id(), userId);
				
				//deleting all user performance data for particular user
				UserPerformance.deleteByUser(userId);
				
				
				
			}
			//Loading user performance summary
			List<UserWords> uwListLocal = UserWords.listAll(UserWords.class);
			for(int i=0;i<uwListLocal.size();i++)
			{
				
				InitDatabase.loadPerformanceSummary(ctx,userId,uwListLocal.get(i).connectionId,module);
			}
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
				UserWords uw = new UserWords(ctx,childObj.getInt("connection_id"),
						childObj.getInt("wordl2id"),childObj.getString("wordl2name"),
						childObj.getInt("wordl1id"),childObj.getString("wordl1name"),
						childObj.getInt("l2id"),childObj.getString("l2name"),childObj.getString("audiocall"));
				uw.save();
				
				//rest of the code to be written for word transcription...
			}
			}else{Log.d("message", jObj.getString("data"));}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void writeBackUserWords()
	{
		List<UserWords> uwList = UserWords.listAll(UserWords.class);
		if (uwList.size()>0)
		{
			try
			{
				JSONArray ja = new JSONArray();
				JSONObject jParent = new JSONObject();
				for(int i=0;i<uwList.size();i++)
				{
					JSONObject rec = new JSONObject();
					rec.put("connection_id", uwList.get(i).connectionId);
					rec.put("user_id", OpenwordsSharedPreferences.getUserInfo().getUserId());
					rec.put("wordl2id", uwList.get(i).wordLTwoId);
					rec.put("wordl1id", uwList.get(i).wordLOneId);
					rec.put("l2id", uwList.get(i).lTwoId);
					
					ja.put(rec);
				}
				jParent.put("data", ja);
				
				
				//building params
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				params1.add(new BasicNameValuePair("params",jParent.toString()));
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

}

