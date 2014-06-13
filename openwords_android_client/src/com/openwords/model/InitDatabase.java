package com.openwords.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.openwords.util.preference.OpenwordsSharedPreferences;

import android.content.Context;

public class InitDatabase {
	public static String url_get_user_perf_from_server = "";
	public static String url_writeback_user_perf = "";
	public static UserInfo user;
	
	public static void checkAndRefresh()
	{
		user=OpenwordsSharedPreferences.getUserInfo();
		int userId = user.getUserId();
		List<UserPerformanceDirty> dirtyPerf = UserPerformanceDirty.findByUser(userId);
		
		//if Dirty performance has records for this user
		if(dirtyPerf.size()>0)
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
					jo.put("total_correct", dirtyPerf.get(i).total_correct);
					jo.put("total_skipped", dirtyPerf.get(i).total_skipped);
					jo.put("total_exposure", dirtyPerf.get(i).total_exposure);
					jo.put("time", dirtyPerf.get(i).last_time);
					jo.put("last_perf", dirtyPerf.get(i).last_performance);
					jo.put("user_ex", dirtyPerf.get(i).user_exclude);
					
					ja.put(jo);
				}
				
				jParent.put("data", ja);
				
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				params1.add(new BasicNameValuePair("params",jParent.toString()));
				JSONParser jsonParse = new JSONParser();
                JSONObject jObj = jsonParse.makeHttpRequest(url_writeback_user_perf, "POST", params1);
                if(jObj.getInt("success")==1)
                	UserPerformanceDirty.deleteByUser(userId);
			}catch(Exception e)
			{ e.printStackTrace();}
		}
		//------------------------------
		
		
	}

}
