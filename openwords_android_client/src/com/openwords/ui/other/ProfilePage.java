package com.openwords.ui.other;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.openwords.R;
import com.openwords.model.UserInfo;
import com.openwords.util.HomePageTool;
import com.openwords.util.preference.OpenwordsSharedPreferences;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ProfilePage extends Activity implements OnClickListener {

	private static String url_dropdown = "http://www.openwords.org/ServerPages/OpenwordsDB/homePageChooseLanguage.php";
	public static ArrayList<HomePageTool> dropdown_list = null;
	private UserInfo userinfo;
	private static int pos=-1;
	LinearLayout parentLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_page);

		BackIcons.builder(this);

		parentLayout = (LinearLayout) findViewById(R.id.profilePage_LinearLayout_parent);
		//Spinner l2_dropdown = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);
		userinfo = OpenwordsSharedPreferences.getUserInfo();
		Log.d("UserID in LangPage",Integer.toString(userinfo.getUserId()));

		runOnUiThread(new Runnable() { @Override
			public void run(){readFromServer();} } );
		
		
		addItemsOnBegin();
	}

	public void addItemsOnBegin() {
		for(int i=0;i<dropdown_list.size();i++) {
			LinearLayout language = new LinearLayout(ProfilePage.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			language.setLayoutParams(params);
			language.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView langName = new TextView(ProfilePage.this);
			langName.setText(dropdown_list.get(i).getName());
			
			langName.setLayoutParams(new LinearLayout.LayoutParams(0, 
					LayoutParams.MATCH_PARENT, 0.3f));
			language.addView(langName);
			
			Spinner wordAmount = new Spinner(ProfilePage.this);
			List<String> amountList = new ArrayList<String>();
			amountList.add("50");
			amountList.add("100");
			amountList.add("200");
			amountList.add("500");
			amountList.add("1000");
			amountList.add("2000");
			amountList.add("5000");
			amountList.add("More than 5000");
			
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, amountList); //selected item will look like a spinner set from XML
			spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			wordAmount.setAdapter(spinnerArrayAdapter);
			wordAmount.setLayoutParams(new LinearLayout.LayoutParams(0, 
					LayoutParams.MATCH_PARENT, 0.3f));
			language.addView(wordAmount);
			
			parentLayout.addView(language);
			
		}
	}

	public void readFromServer() {
		/*
    ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this, R.array.homePage_Spinner_language_array, android.R.layout.simple_spinner_item);
    languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    language.setAdapter(languageAdapter);
		 */

		ArrayList<HomePageTool> dropdown = new ArrayList<HomePageTool>();
		try 
		{
			List<NameValuePair> params1 = new ArrayList<NameValuePair>(2);
			params1.add(new BasicNameValuePair("userid",Integer.toString(userinfo.getUserId())));
			Log.d("User", "Passed Validation");
			
			JSONObject jObj = null;//jsonParse.makeHttpRequest(url_dropdown, "POST", params1);
			Log.d("Obj", jObj.toString());
			JSONArray jArr = jObj.getJSONArray("language");

			for (int i = 0; i < jArr.length(); i++) 
			{  // **line 2**
				JSONObject childJSONObject = jArr.getJSONObject(i);

				dropdown.add(new HomePageTool(childJSONObject.getString("l2name"),childJSONObject.getInt("l2id")));

				//Log.d("Loop", childJSONObject.getString("l2name"));
				//Log.d("Loop", childJSONObject.getString("l2name"));
			}
		}
		catch(Exception e)
		{e.printStackTrace();}
		dropdown_list=dropdown;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}

