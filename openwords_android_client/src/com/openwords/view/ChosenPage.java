package com.openwords.view;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.openwords.model.InitDatabase;
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
		
		
		
		// Refrshing User Data --- in Aysnc Task
		new RefreshData().execute();
				
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
    	Intent returnIntent = new Intent();
    	setResult(RESULT_OK,returnIntent);
    	finish(); //finish current page
    	//startActivity(new Intent(this, HomePage.class)); //in the activity stack, the HomePage exists
    	return true;
    }
    
  //********************* AYSNC TASK for refreshing data in background **********************
	 private class RefreshData extends AsyncTask<Void, Void, Void> {
		 @Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
			 Log.d("******in asynctasck", "yes");
			 InitDatabase.checkAndRefreshPerf(ChosenPage.this, 0, 0);
				return null;
			}
	
	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }
	
	     protected void onPostExecute(Long result) {
	         //showDialog("Refreshed");
	    	 Log.d("Refresh Complete", "yes");
	     }

		
	 }

}
