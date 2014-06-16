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

public class InitDatabase {
	public static String url_get_user_perf_from_server = "";
	public static String url_writeback_user_perf = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/writeBackUserPerf.php";
	public static String url_get_user_words="http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/getUserWordsRecords.php";
	public static UserInfo user;
	
	public static void checkAndRefreshPerf()
	{
		user=OpenwordsSharedPreferences.getUserInfo();
		int userId = user.getUserId();
		List<UserPerformanceDirty> dirtyPerf = UserPerformanceDirty.listAll(UserPerformanceDirty.class);
		
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
		//------------------------------
		
		//deleting all user performance data for particular user
		List<UserPerformance> upList = UserPerformance.findByUser(userId);
		if(upList.size()!=0)
			UserPerformance.deleteByUser(userId);
		
		
		
		//deleting all User words data 
	}
	
	public static void loadUserWords(Context ctx, int languageTwoId, int userId)
	{
		try
		{
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("language",Integer.toString(languageTwoId)));
			params1.add(new BasicNameValuePair("user",Integer.toString(userId)));
			JSONParser jsonParse = new JSONParser();
			JSONObject jObj = jsonParse.makeHttpRequest(url_get_user_words, "POST", params1);
			
			JSONArray jArr = jObj.getJSONArray("words");
			for(int i=0;i<jArr.length();i++)
			{
				JSONObject childObj = jArr.getJSONObject(i);
				UserWords uw = new UserWords(ctx,childObj.getInt("connection_id"),
						childObj.getInt("wordl2id"),childObj.getString("wordl2name"),
						childObj.getInt("wordl1id"),childObj.getString("wordl1name"),
						childObj.getInt("l2id"),childObj.getString("l2name"),"");
				uw.save();
				
				//rest of the code to be written for word transcription...
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
