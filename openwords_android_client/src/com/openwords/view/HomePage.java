package com.openwords.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.openwords.R;
import com.openwords.model.JSONParser;
import com.openwords.model.UserInfo;
import com.openwords.util.HomePageTool;
import com.openwords.util.LanguagePageTool;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.view.actionbar.ActionBarBuilder;
import com.openwords.view.learningModule.Hearing;
import com.openwords.view.learningModule.Review;
import com.openwords.view.learningModule.SelfEvaluate;
import com.openwords.view.learningModule.TypeEvaluate;

public class HomePage extends Activity implements OnClickListener {

    private static Spinner begin, l2_dropdown;
    public static ArrayList<HomePageTool> dropdown_list = null;
    public static ArrayList<String> strArr=null;
    public static int pos=-1;
    public int homelang_id;
    // Add another array of type HomePage Tool that holds the name and id of each chosen language
    // private static ArrayList<HomePageTool> drop_down = null;
    private static String url_dropdown = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/homePageChooseLanguage.php";
    private UserInfo userinfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose

        setContentView(R.layout.activity_home_page);

        //building the action bar
        new ActionBarBuilder(this, ActionBarBuilder.Home_Page);
        
        // Creating the drop down object
        l2_dropdown = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);
        
        userinfo = OpenwordsSharedPreferences.getUserInfo();
        Log.d("UserID in LangPage",Integer.toString(userinfo.getUserId()));
        
        runOnUiThread(new Runnable() { public void run(){readFromServer();} } );
      
        addItemsOnBegin();
        Button testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
        testPageGo.setOnClickListener(this);
        
        
        //------creating string array-----
        strArr = new ArrayList<String>();
        for(int i=0;i<dropdown_list.size();i++)
        {
        	strArr.add(dropdown_list.get(i).getName());
        }
        
        ArrayAdapter<String> dropdownadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, strArr);
                l2_dropdown.setAdapter(dropdownadapter);
                l2_dropdown.setOnItemSelectedListener(new OnItemSelectedListener(){
                	@Override
                	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        Log.d("whatever", Integer.toString(position));
                        pos=position;
                        Log.d("ID", Integer.toString(dropdown_list.get(position).getId()));
                        homelang_id = dropdown_list.get(position).getId();
                    }
                	@Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                		Log.d("whatever", "_nothing");
                    }
                }
                );
        //Log.d("ID",Integer.toString(dropdown_list.get(pos).getId()));        
    }
 
    public void addItemsOnBegin() {
        begin = (Spinner) findViewById(R.id.homePage_Spinner_begin);
        ArrayAdapter<CharSequence> beginAdapter = ArrayAdapter.createFromResource(this, R.array.homePage_spinner_begin_array, android.R.layout.simple_spinner_item);
        beginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        begin.setAdapter(beginAdapter);
    }
    
   /* public class HomeAdapter extends ArrayAdapter<HomePageTool>
    {
            public HomeAdapter(Context context, int textViewResourceId, 
                            ArrayList<HomePageTool> langlist) {
                    super(context, textViewResourceId, langlist);
                    this.langlist = new ArrayList<LanguagePageTool>();
                    this.langlist.addAll(langlist);
            }
*/
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
                JSONParser jsonParse = new JSONParser();
                JSONObject jObj = jsonParse.makeHttpRequest(url_dropdown, "POST", params1);
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
                //Insert add more button
                dropdown.add(new HomePageTool("Add more", 999));
                                dropdown_list=dropdown;
    }

    public void testPageButtonClick() {
        String taskPage = begin.getSelectedItem().toString();
        LogUtil.logDeubg(this, "Task: " + taskPage);
        Class targetClass = HomePage.class;
        // Set the language id for the shared preferences here
        userinfo.setLang_id(homelang_id); 
        Log.d("Shared Preferences Language ID", Integer.toString(userinfo.getLang_id()));
        
        if (taskPage.equals("Review")) {
            targetClass = Review.class;
        } else if (taskPage.equals("Self evaluation")) {
            targetClass = SelfEvaluate.class;
        } else if (taskPage.equals("Type evaluation")) {
            targetClass = TypeEvaluate.class;
        } else if (taskPage.equals("Hearing")) {
            targetClass = Hearing.class;
        }

        HomePage.this.startActivityForResult(new Intent(HomePage.this, targetClass), 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homePage_Button_testPageGo:
                testPageButtonClick();
                break;
            //case R.id.homePage_Spinner_chooseLanguage:
            	//Log.d("whatever", Integer.toString(l2_dropdown.getSelectedItemPosition()));
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really?")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        HomePage.super.onBackPressed();
                    }
                }).create().show();
    }
}
