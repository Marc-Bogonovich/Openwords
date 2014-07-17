package com.openwords.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.openwords.R;
import com.openwords.learningmodule.ActivityHearing;
import com.openwords.learningmodule.ActivityReview;
import com.openwords.learningmodule.ActivitySelfEval;
import com.openwords.learningmodule.ActivityTypeEval;
import com.openwords.learningmodule.ProgressHearing;
import com.openwords.learningmodule.ProgressReview;
import com.openwords.learningmodule.ProgressSelfEval;
import com.openwords.learningmodule.ProgressTypeEval;
import com.openwords.model.InitDatabase;
import com.openwords.model.JSONParser;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardReviewAdapter;
import com.openwords.model.LeafCardHearing;
import com.openwords.model.LeafCardHearingAdapter;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.model.LeafCardSelfEvalAdapter;
import com.openwords.model.LeafCardTypeEval;
import com.openwords.model.LeafCardTypeEvalAdapter;
import com.openwords.model.UserInfo;
import com.openwords.model.UserWords;
import com.openwords.model.WordTranscription;
import com.openwords.util.HomePageTool;
import com.openwords.util.TimeConvertor;
import com.openwords.util.WordsPageTool;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.view.actionbar.ActionBarBuilder;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomePage extends Activity implements OnClickListener {

	private static JSONArray jArrMain;
	private static String url_write_downloaded_words_to_server = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/setUserWords.php";
	private static String nextwords_url = "http://geographycontest.ipage.com/OpenwordsOrg/WordsDB/wordsPageGetWordList.php";
	private static Spinner begin, l2_dropdown;
    public static ArrayList<HomePageTool> dropdown_list = null;
    public static ArrayList<String> strArr = null;
    public static int pos = -1;
    public int homelang_id;
    public String homelang_name;
    private ProgressDialog pDialog = null;
    // Add another array of type HomePage Tool that holds the name and id of each chosen language
    // private static ArrayList<HomePageTool> drop_down = null;
    private static String url_dropdown = "http://geographycontest.ipage.com/OpenwordsOrg/OpenwordsDB/homePageChooseLanguage.php";
    private UserInfo userinfo;
    private int SIZE = 10;
    private ActionBarBuilder actionBar;
    
    
    //-----------
    private static int language_position=-1;
    //-----------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.e("HomePage","onCreate");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose

        setContentView(R.layout.activity_home_page);

        //building the action bar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Home_Page);

        // Creating the drop down object
        l2_dropdown = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);

        userinfo = OpenwordsSharedPreferences.getUserInfo();
        Log.d("UserID in LangPage", Integer.toString(userinfo.getUserId()));
        
        pDialog = ProgressDialog.show(HomePage.this, "",
                "Load information from the server", true);

        runOnUiThread(new Runnable() {//Please stop spawning tasks on UI Thread, use AsyncTask instead, unless you don't want user to touch anything before the page is completely rendered, then please prompt a progress dialog
            public void run() {
                readFromServer();
                pDialog.dismiss();
            }
        });


        addItemsOnBegin();
        Button testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
        testPageGo.setOnClickListener(HomePage.this);

        //------creating string array-----
        strArr = new ArrayList<String>();
        for (int i = 0; i < dropdown_list.size(); i++) {
            strArr.add(dropdown_list.get(i).getName());
            if(dropdown_list.get(i).getId()==OpenwordsSharedPreferences.getUserInfo().getLang_id())
            {
            	language_position=i;
            }
            
        }
        //insert item into chooseLanguage spinner
        ArrayAdapter<String> dropdownadapter = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_list_item_1, android.R.id.text1, strArr);
        l2_dropdown.setAdapter(dropdownadapter);
        l2_dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("whatever", Integer.toString(position));
                pos = position;
                Log.d("ID", Integer.toString(dropdown_list.get(position).getId()));
                if (dropdown_list.get(position).getId() == -999) {
                    //Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                   
                	HomePage.this.startActivity(new Intent(HomePage.this, LanguagePage.class));
                }
                else
                {
                homelang_id = dropdown_list.get(position).getId();
                homelang_name = dropdown_list.get(position).getName(); //new
                UserInfo user = OpenwordsSharedPreferences.getUserInfo();
                user.setLang_id(homelang_id);
                user.setLang_Name(homelang_name); //new
                Toast.makeText(HomePage.this, "Chosen language id: " + homelang_id, Toast.LENGTH_SHORT).show();
                OpenwordsSharedPreferences.setUserInfo(user);
                Log.d("saved Lang", ""+OpenwordsSharedPreferences.getUserInfo().getLang_id()
                		+OpenwordsSharedPreferences.getUserInfo().getLang_Name()); //new
                //-----getting first x words if not present----
                new GetFirstWords().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.d("whatever", "_nothing");
            }
        }
        );
        //Log.d("ID",Integer.toString(dropdown_list.get(pos).getId()));  
        
        if(language_position != -1)
        	l2_dropdown.setSelection(language_position);
        

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
        try {
            List<NameValuePair> params1 = new ArrayList<NameValuePair>(2);
            params1.add(new BasicNameValuePair("userid", Integer.toString(userinfo.getUserId())));
            Log.d("User", "Passed Validation");
            JSONParser jsonParse = new JSONParser();
            JSONObject jObj = jsonParse.makeHttpRequest(url_dropdown, "POST", params1);
            Log.d("Obj", jObj.toString());
            JSONArray jArr = jObj.getJSONArray("language");

            for (int i = 0; i < jArr.length(); i++) {  // **line 2**
                JSONObject childJSONObject = jArr.getJSONObject(i);

                dropdown.add(new HomePageTool(childJSONObject.getString("l2name"), childJSONObject.getInt("l2id")));

                //Log.d("Loop", childJSONObject.getString("l2name"));
                //Log.d("Loop", childJSONObject.getString("l2name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Insert add more button
        dropdown.add(new HomePageTool("Add more languages", -999));
        dropdown_list = dropdown;
    }
    
    
    //-------- Getting first 10 words from server for a language -----------------
    public void getFirstWordsFromServer()
    {
            ArrayList<WordsPageTool> words_list = new ArrayList<WordsPageTool>();
            try 
            {
                    List<NameValuePair> params = new ArrayList<NameValuePair>(3);
                    params.add(new BasicNameValuePair("user", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getUserId())));
                    params.add(new BasicNameValuePair("langOne", "1"));
                    params.add(new BasicNameValuePair("langTwo", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getLang_id())));
                    Log.d("User", "47");
                    JSONParser jsonParse = new JSONParser();
                    JSONObject jObj = jsonParse.makeHttpRequest(nextwords_url, "POST", params);
                    Log.d("Obj", jObj.toString());
                    if(jObj.getInt("success")==1)
                    {
                    JSONArray jArr = jObj.getJSONArray("data");
                    String abc = Integer.toString(jArr.length());
                    Log.d("Array", abc);
                    jArrMain = jArr;
                    }
                    
            }
                    catch(Exception e)
                    {e.printStackTrace();}     
    }
    //---------------------------------------------------------------------------------------------
    //------------update downloaded word on server----------------------------------------------
    public void updateWordsOnServer(String conIds, long dTime)
	{
		try
		{
			int user = OpenwordsSharedPreferences.getUserInfo().getUserId();
			int langTwo = OpenwordsSharedPreferences.getUserInfo().getLang_id();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("conid", conIds));
            params.add(new BasicNameValuePair("dtime", Long.toString(dTime)));
            params.add(new BasicNameValuePair("user",Integer.toString(user)));
            params.add(new BasicNameValuePair("lTwo",Integer.toString(langTwo)));
            JSONParser jsonParse = new JSONParser();
            JSONObject jObj = jsonParse.makeHttpRequest(url_write_downloaded_words_to_server, "POST", params);
            
            if(jObj.getInt("success")==1)
            	Log.d("Message From Server", jObj.getString("message"));
            
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        
		
	}
    
    
    
    public void testPageButtonClick() {
        String taskPage = begin.getSelectedItem().toString();
        LogUtil.logDeubg(this, "Task: " + taskPage);
        Log.d("Shared Preferences Language ID", Integer.toString(userinfo.getLang_id()));
        pDialog = ProgressDialog.show(HomePage.this, "",
                "Assembling leaf cards", true);
        if (taskPage.equals("Review")) {
//        	InitDatabase.checkAndRefreshPerf(this, 0);
//        	UserPerformance.deleteAll(UserPerformance.class);
//        	WordTranscription.deleteAll(WordTranscription.class);
//        	UserWords.deleteAll(UserWords.class);
//        	new InsertData(HomePage.this);
        	
        	//UserWords.setStaleToFresh(5);
        	
        	//List<UserWords> uw=UserWords.listAll(UserWords.class);
        	//Log.d("*Data In User Words******", uw.get(0).connectionId+uw.get(0).wordLOne+uw.get(0).fresh);

            new Thread(new Runnable() {
                List<LeafCard> cards;

                public void run() {
                    //InitDatabase.checkAndRefreshPerf(HomePage.this, 0, 1);
                    final ProgressReview progress = OpenwordsSharedPreferences.getReviewProgress();
                    if (progress == null || progress.getLanguageID()!=OpenwordsSharedPreferences.getUserInfo().getLang_id()) {

                        cards = new LeafCardReviewAdapter().getList(SIZE);
                        if (cards.size() <= 0) {
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
            new Thread(new Runnable() {
                List<LeafCardSelfEval> cards;

                public void run() {
                    //InitDatabase.checkAndRefreshPerf(HomePage.this, 1, 1);
                    final ProgressSelfEval progress = OpenwordsSharedPreferences.getSelfEvaluationProgress();
                    if (progress == null || progress.getLanguageID()!=OpenwordsSharedPreferences.getUserInfo().getLang_id()) {

                        cards = new LeafCardSelfEvalAdapter().getList(SIZE);
                        if (cards.size() <= 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(HomePage.this, "Please select word first", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            ActivitySelfEval.setCardsPool(cards);
                            startActivity(new Intent(HomePage.this, ActivitySelfEval.class));
                        }
                    } else {
                        ActivitySelfEval.setCardsPool(progress.getCardsPool());
                        ActivitySelfEval.setCurrentCard(progress.getCurrentCard());
                        startActivity(new Intent(HomePage.this, ActivitySelfEval.class));
                    }
                    pDialog.dismiss();
                }
            }).start();

//        	final SelfEvalProgress progress = OpenwordsSharedPreferences.getSelfEvaluationProgress();
//        	List<LeafCardSelfEval> cards =  new LeafCardSelfEvalAdapter(HomePage.this).getList(SIZE);
//            ActivitySelfEval.setCardsPool(cards);
//        	if (progress == null) {  
//                startActivity(new Intent(HomePage.this, ActivitySelfEval.class));
//            } else {
//            	ActivitySelfEval.setCurrentCard(progress.getCurrentCard());
//                startActivity(new Intent(HomePage.this, ActivitySelfEval.class)); 
//            }
        } else if (taskPage.equals("Type evaluation")) {
            new Thread(new Runnable() {
                List<LeafCardTypeEval> cards;

                public void run() {
                    //InitDatabase.checkAndRefreshPerf(HomePage.this, 2, 1);
                    final ProgressTypeEval progress = OpenwordsSharedPreferences.getTypeEvaluationProgress();
                    if (progress == null || progress.getLanguageID()!=OpenwordsSharedPreferences.getUserInfo().getLang_id()) {

                        cards = new LeafCardTypeEvalAdapter().getList(SIZE);
                        if (cards.size() <= 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(HomePage.this, "Please select word first", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            ActivityTypeEval.setCardsPool(cards);
                            startActivity(new Intent(HomePage.this, ActivityTypeEval.class));
                        }
                    } else {
                        ActivityTypeEval.setCardsPool(progress.getCardsPool());
                        ActivityTypeEval.setCurrentCard(progress.getCurrentCard());
                        startActivity(new Intent(HomePage.this, ActivityTypeEval.class));
                    }
                    pDialog.dismiss();
                }
            }).start();
        } else if (taskPage.equals("Hearing")) {

            new Thread(new Runnable() {
                List<LeafCardHearing> cards;

                public void run() {
                    //InitDatabase.checkAndRefreshPerf(HomePage.this, 3, 1);
                    final ProgressHearing progress = OpenwordsSharedPreferences.getHearingProgress();
                    if (progress == null || progress.getLanguageID()!=OpenwordsSharedPreferences.getUserInfo().getLang_id()) {

                        cards = new LeafCardHearingAdapter().getList(SIZE);
                        if (cards.size() <= 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(HomePage.this, "No word with audio", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            ActivityHearing.setCardsPool(cards);
                            startActivity(new Intent(HomePage.this, ActivityHearing.class));
                        }
                    } else {
                        ActivityHearing.setCardsPool(progress.getCardsPool());
                        ActivityHearing.setCurrentCard(progress.getCurrentCard());
                        startActivity(new Intent(HomePage.this, ActivityHearing.class));
                    }
                    pDialog.dismiss();
                }
            }).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.checkSetting();
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
   
 //----------- Async Task to get first 10 words   
    private class GetFirstWords extends AsyncTask<Void, Void, Void> {
		 @Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
			 Log.d("******in asynctasck", "yes");
			 int langId = OpenwordsSharedPreferences.getUserInfo().getLang_id();
			 List<UserWords> existList = UserWords.findByLanguage(langId);
			 if(existList.size()==0)
			 {
				 getFirstWordsFromServer();
				 
				 String conIds="";
				 for(int i=0;i<jArrMain.length();i++)
				 {
					 try {
							JSONObject c = jArrMain.getJSONObject(i);
							 
							UserWords newUw = new UserWords(c.getInt("connection_id"),c.getInt("wordl1"),
										 c.getString("wordl1_text"),c.getInt("wordl2"),c.getString("wordl2_text"),
										 c.getInt("l2id"),c.getString("l2name"),c.getString("audio"),1);
							newUw.save();
							
							WordTranscription t = new WordTranscription(c.getInt("wordl2"),c.getString("trans"));
							t.save();
							
							if(conIds=="")
							{
								conIds=conIds+c.getInt("connection_id");
							}
							else
							{
								conIds=conIds+"|"+c.getInt("connection_id");
							}
							
							Log.d("word", c.getString("wordl1_text"));
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				 }
				 //update on server
				 updateWordsOnServer(conIds, TimeConvertor.getUnixTime());
			 }
				return null;
			}
	
	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }
	
	     protected void onPostExecute(Long result) {
	         //showDialog("Refreshed");
	    	 //Log.d("Refresh Complete", "yes");
	     }

		
	 }
    
}


	

	
