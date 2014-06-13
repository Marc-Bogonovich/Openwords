package com.openwords.view;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.openwords.R;
import com.openwords.model.JSONParser;
import com.openwords.model.UserInfo;
import com.openwords.model.UserPerformanceDirty;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;

public class ChosenPage extends Activity{
	private static String url_l2_chosen = "http://geographycontest.ipage.com/OpenwordsOrg/WordsDB/getLtwoOptions.php";
	public static ArrayList<String> chosen_list = null;
	private UserInfo userinfo;
	
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chosen_page);
		userinfo = OpenwordsSharedPreferences.getUserInfo();
		
		//--------------get Data
		runOnUiThread(new Runnable() { public void run(){GetFromServer();} } );
		
		GridView chosenview = (GridView)findViewById(R.id.chosen_gridview);
		
		
		ArrayAdapter<String> chosenadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, chosen_list);
		chosenview.setAdapter(chosenadapter);
		
		
		//***********db test********
		
		UserPerformanceDirty upd = new UserPerformanceDirty(this);
		upd.connection_id=3;
		upd.user_id=OpenwordsSharedPreferences.getUserInfo().getUserId();
		upd.save();
		//************************
		
		/*
		//-------test----json parameter----
		try
		{
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		JSONObject jParent = new JSONObject();
		
		jo.put("user", 1);
		jo.put("name", new String("Mayukh"));
		ja.put(jo);
		
		JSONObject jo1 = new JSONObject();
		
		jo1.put("user", 2);
		jo1.put("name", new String("maydas"));
		ja.put(jo1);
		
		jParent.put("params", ja);
		
		Log.d("display", jParent.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	*/	
	}

	public void GetFromServer()
	{
		ArrayList<String> chosen = new ArrayList<String>();
		try 
        {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>(2);
                params1.add(new BasicNameValuePair("userid",Integer.toString(userinfo.getUserId())));
                Log.d("User", "Passed Validation");
                JSONParser jsonParse = new JSONParser();
                JSONObject jObj = jsonParse.makeHttpRequest(url_l2_chosen, "POST", params1);
                Log.d("Obj", jObj.toString());
                JSONArray jArr = jObj.getJSONArray("langdata");
                
                
                for (int i = 0; i < jArr.length(); i++) 
                {  // **line 2**
                	JSONObject childJSONObject = jArr.getJSONObject(i);
                	if(childJSONObject.getBoolean("chosen")==true)
                	{
                		chosen.add(childJSONObject.getString("l2name"));
                //Log.d("Loop", childJSONObject.getString("l2name"));
                		Log.d("Loop", childJSONObject.getString("l2name"));
                	}
             }
                
        
        }
                catch(Exception e)
                {e.printStackTrace();}
				chosen_list=chosen;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chosen_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public boolean onTouchEvent(MotionEvent event) {
    	startActivity(new Intent(this, HomePage.class));
    	finish();
    	return true;
    }

}
