package com.openwords.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Contacts.Intents.Insert;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.openwords.R;
import com.openwords.learningmodule.ActivityHearing;
import com.openwords.learningmodule.ActivityReview;
import com.openwords.learningmodule.ActivitySelfEval;
import com.openwords.learningmodule.ActivityTypeEval;
import com.openwords.learningmodule.HearingProgress;
import com.openwords.learningmodule.Progress;
import com.openwords.learningmodule.SelfEvalProgress;
import com.openwords.learningmodule.TypeEvalProgress;
import com.openwords.model.InsertData;
import com.openwords.model.InitDatabase;
import com.openwords.model.InsertData;
import com.openwords.model.JSONParser;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardAdapter;
import com.openwords.model.LeafCardHearing;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.model.LeafCardSelfEvalAdapter;
import com.openwords.model.LeafCardTypeEval;
import com.openwords.model.UserInfo;
import com.openwords.model.UserPerformance;
import com.openwords.model.UserWords;
import com.openwords.model.WordTranscription;
import com.openwords.util.HomePageTool;
import com.openwords.util.LanguagePageTool;
import com.openwords.util.WordSelectionAlg;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.view.actionbar.ActionBarBuilder;
import com.openwords.view.actionbar.NextWords;
import com.openwords.view.actionbar.WordsPage;

public class HomePage extends Activity implements OnClickListener {

    private static Spinner begin, l2_dropdown;
    public static ArrayList<HomePageTool> dropdown_list = null;
    public static ArrayList<String> strArr=null;
    public static int pos=-1;
    public int homelang_id;
    private ProgressDialog pDialog = null;
    // Add another array of type HomePage Tool that holds the name and id of each chosen language
    // private static ArrayList<HomePageTool> drop_down = null;
    private static String url_dropdown = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/homePageChooseLanguage.php";
    private UserInfo userinfo;
    private int SIZE = 10;
    
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
                        if(dropdown_list.get(position).getId()==999)
                        {
                        	//Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                        	HomePage.this.startActivity(new Intent(HomePage.this, LanguagePage.class));
                        }
                        homelang_id = dropdown_list.get(position).getId();
                        UserInfo user = OpenwordsSharedPreferences.getUserInfo();
                        user.setLang_id(homelang_id);
                        Toast.makeText(HomePage.this, "Current language id" + homelang_id, Toast.LENGTH_SHORT).show();
                        OpenwordsSharedPreferences.setUserInfo(user);
                        
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
        Log.d("Shared Preferences Language ID", Integer.toString(userinfo.getLang_id()));
        
        if (taskPage.equals("Review")) {
//        	InitDatabase.checkAndRefreshPerf(this, 0);
//        	UserPerformance.deleteAll(UserPerformance.class);
//        	WordTranscription.deleteAll(WordTranscription.class);
//        	UserWords.deleteAll(UserWords.class);
//        	new InsertData(HomePage.this);
    		pDialog = ProgressDialog.show(HomePage.this, "",
    				"Assemble the leaf cards", true);

    		new Thread(new Runnable() {
            	List<LeafCard> cards;
    			public void run() {
    	        	InitDatabase.checkAndRefreshPerf(HomePage.this, 0, 1);
    	        	final Progress progress = OpenwordsSharedPreferences.getReviewProgress();
    	            if (true || progress == null) {

    	                cards = new LeafCardAdapter(HomePage.this).getList(SIZE);
    	                if(cards.size()<=1){
        	                runOnUiThread(new Runnable() {                
        	                    @Override
        	                    public void run() {
        	                        Toast.makeText(HomePage.this, "Please select word first", Toast.LENGTH_SHORT).show();
        	                    }
        	                });
    	                } else {
    	                	ActivityReview.setCardsPool(cards);
        	                startActivity(new Intent(HomePage.this, ActivityReview.class));
    	                }
    	            } else {
    	            	ActivityReview.setCardsPool(progress.getCardsPool());
    	            	ActivityReview.setCurrentCard(progress.getCurrentCard());
    	                startActivity(new Intent(HomePage.this, ActivityReview.class));
    	            }
    	            pDialog.dismiss();

    			}
    		}).start();
        	
        	

        } else if (taskPage.equals("Self evaluation")) {
        	InitDatabase.checkAndRefreshPerf(this, 1, 1);
        	final SelfEvalProgress progress = OpenwordsSharedPreferences.getSelfEvaluationProgress();
        	List<LeafCardSelfEval> cards =  new LeafCardSelfEvalAdapter(HomePage.this).getList(SIZE);
            ActivitySelfEval.setCardsPool(cards);
        	if (progress == null) {  
                startActivity(new Intent(HomePage.this, ActivitySelfEval.class));
            } else {
            	ActivitySelfEval.setCurrentCard(progress.getCurrentCard());
                startActivity(new Intent(HomePage.this, ActivitySelfEval.class)); 
            }
        } else if (taskPage.equals("Type evaluation")) {
            //targetClass = TypeEvaluate.class;
        	InitDatabase.checkAndRefreshPerf(this, 2, 1);
        	final TypeEvalProgress progress = OpenwordsSharedPreferences.getTypeEvaluationProgress();
            List<LeafCardTypeEval> cards = new LinkedList<LeafCardTypeEval>();
            cards.add(new LeafCardTypeEval("��", "person", "ren"));
            cards.add(new LeafCardTypeEval("è", "cat", "mao"));
            cards.add(new LeafCardTypeEval("����", "earth", "di qiu"));
            cards.add(new LeafCardTypeEval("ʱ��", "time", "shi jian"));
            cards.add(new LeafCardTypeEval("����", "world", "shi jie"));
            cards.add(new LeafCardTypeEval("����", "computer", "dian nao"));
            cards.add(new LeafCardTypeEval("���", "software", "ruan jian"));
        	if (progress == null) {
                ActivityTypeEval.setCardsPool(cards);
                startActivity(new Intent(HomePage.this, ActivityTypeEval.class));
            } else {
            	ActivityTypeEval.setCardsPool(progress.getCardsPool());
                ActivityTypeEval.setCurrentCard(progress.getCurrentCard());
                startActivity(new Intent(HomePage.this, ActivityTypeEval.class));
//                new AlertDialog.Builder(HomePage.this)
//                        .setTitle("Continue?")
//                        .setMessage("You have a saved progress, do you want to continue?")
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                List<LeafCardTypeEval> cards = new LinkedList<LeafCardTypeEval>();
//                                cards.add(new LeafCardTypeEval("��", "person", "ren"));
//                                cards.add(new LeafCardTypeEval("è", "cat", "mao"));
//                                cards.add(new LeafCardTypeEval("����", "earth", "di qiu"));
//                                cards.add(new LeafCardTypeEval("ʱ��", "time", "shi jian"));
//                                cards.add(new LeafCardTypeEval("����", "world", "shi jie"));
//                                cards.add(new LeafCardTypeEval("����", "computer", "dian nao"));
//                                cards.add(new LeafCardTypeEval("���", "software", "ruan jian"));
//                                ActivityTypeEval.setCardsPool(cards);
//                                startActivity(new Intent(HomePage.this, ActivityTypeEval.class));
//                            }
//                        })
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                            	ActivityTypeEval.setCardsPool(progress.getCardsPool());
//                                ActivityTypeEval.setCurrentCard(progress.getCurrentCard());
//                                startActivity(new Intent(HomePage.this, ActivityTypeEval.class));
//                            }
//                        }).create().show();
            }
        } else if (taskPage.equals("Hearing")) {
        	InitDatabase.checkAndRefreshPerf(this, 3, 1);
        	new InsertData(HomePage.this);
        	final HearingProgress progress = OpenwordsSharedPreferences.getHearingProgress();
            List<LeafCardHearing> cards = new LinkedList<LeafCardHearing>();
            cards.add(new LeafCardHearing("��", "person", "ren"));
            cards.add(new LeafCardHearing("è", "cat", "mao"));
            cards.add(new LeafCardHearing("����", "earth", "di qiu"));
            cards.add(new LeafCardHearing("ʱ��", "time", "shi jian"));
            cards.add(new LeafCardHearing("����", "world", "shi jie"));
            cards.add(new LeafCardHearing("����", "computer", "dian nao"));
            cards.add(new LeafCardHearing("���", "software", "ruan jian"));
            ActivityHearing.setCardsPool(cards);
        	if (progress == null) {
                startActivity(new Intent(HomePage.this, ActivityHearing.class));
            } else {
            	ActivityHearing.setCurrentCard(progress.getCurrentCard());
                startActivity(new Intent(HomePage.this, ActivityHearing.class));     
//                new AlertDialog.Builder(HomePage.this)
//                        .setTitle("Continue?")
//                        .setMessage("You have a saved progress, do you want to continue?")
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                List<LeafCardHearing> cards = new LinkedList<LeafCardHearing>();
//                                cards.add(new LeafCardHearing("��", "person", "ren"));
//                                cards.add(new LeafCardHearing("è", "cat", "mao"));
//                                cards.add(new LeafCardHearing("����", "earth", "di qiu"));
//                                cards.add(new LeafCardHearing("ʱ��", "time", "shi jian"));
//                                cards.add(new LeafCardHearing("����", "world", "shi jie"));
//                                cards.add(new LeafCardHearing("����", "computer", "dian nao"));
//                                cards.add(new LeafCardHearing("���", "software", "ruan jian"));
//                                ActivityHearing.setCardsPool(cards);
//                                startActivity(new Intent(HomePage.this, ActivityHearing.class));
//                            }
//                        })
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                            	ActivityHearing.setCardsPool(progress.getCardsPool());
//                            	ActivityHearing.setCurrentCard(progress.getCurrentCard());
//                                startActivity(new Intent(HomePage.this, ActivityHearing.class));
//                            }
//                        }).create().show();
            }
        }
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
