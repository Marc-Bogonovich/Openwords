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
import com.openwords.model.LeafCardHearing;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.model.LeafCardTypeEval;
import com.openwords.model.UserInfo;
import com.openwords.util.HomePageTool;
import com.openwords.util.LanguagePageTool;
import com.openwords.util.WordSelectionAlg;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.view.actionbar.ActionBarBuilder;

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
                        UserInfo user = OpenwordsSharedPreferences.getUserInfo();
                        user.setLang_id(homelang_id);
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
        /*
        // Set the last user id here
        int last_userid;
        last_userid = userinfo.getUserId();
        userinfo.setLast_userid(last_userid);
        Log.d("Last UID", Integer.toString(userinfo.getLast_userid()));
        */
        
        // Set the language id for the shared preferences here
        //userinfo.setLang_id(homelang_id); 
        Log.d("Shared Preferences Language ID", Integer.toString(userinfo.getLang_id()));
        
        if (taskPage.equals("Review")) {
            //targetClass = Review.class;
        	InitDatabase.checkAndRefreshPerf(this, 0);
        	//new InsertData(HomePage.this);
        	List<Integer> list = new WordSelectionAlg(HomePage.this).pickup(3);
        	for(Integer i : list) Log.e("List", i+"");
        	final Progress progress = OpenwordsSharedPreferences.getReviewProgress();
            if (progress == null) {
                List<LeafCard> cards = new LinkedList<LeafCard>();
                cards.add(new LeafCardSelfEval("人", "person", "ren"));
                cards.add(new LeafCardSelfEval("猫", "cat", "mao"));
                cards.add(new LeafCardSelfEval("地球", "earth", "di qiu"));
                cards.add(new LeafCardSelfEval("时间", "time", "shi jian"));
                cards.add(new LeafCardSelfEval("世界", "world", "shi jie"));
                cards.add(new LeafCardSelfEval("电脑", "computer", "dian nao"));
                cards.add(new LeafCardSelfEval("软件", "software", "ruan jian"));
                ActivityReview.setCardsPool(cards);
                startActivity(new Intent(HomePage.this, ActivityReview.class));
            } else {
                new AlertDialog.Builder(HomePage.this)
                        .setTitle("Continue?")
                        .setMessage("You have a saved progress, do you want to continue?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                List<LeafCard> cards = new LinkedList<LeafCard>();
                                cards.add(new LeafCardSelfEval("人", "person", "ren"));
                                cards.add(new LeafCardSelfEval("猫", "cat", "mao"));
                                cards.add(new LeafCardSelfEval("地球", "earth", "di qiu"));
                                cards.add(new LeafCardSelfEval("时间", "time", "shi jian"));
                                cards.add(new LeafCardSelfEval("世界", "world", "shi jie"));
                                cards.add(new LeafCardSelfEval("电脑", "computer", "dian nao"));
                                cards.add(new LeafCardSelfEval("软件", "software", "ruan jian"));
                                ActivityReview.setCardsPool(cards);
                                startActivity(new Intent(HomePage.this, ActivityReview.class));
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            	ActivityReview.setCardsPool(progress.getCardsPool());
                            	Log.e("aa","after");
                            	
                            	ActivityReview.setCurrentCard(progress.getCurrentCard());
                                startActivity(new Intent(HomePage.this, ActivityReview.class));
                            }
                        }).create().show();
            }
        } else if (taskPage.equals("Self evaluation")) {
            //targetClass = SelfEvaluate.class;
        	InitDatabase.checkAndRefreshPerf(this, 1);
        	
        	final SelfEvalProgress progress = OpenwordsSharedPreferences.getSelfEvaluationProgress();
        	List<LeafCardSelfEval> cards = new LinkedList<LeafCardSelfEval>();
            cards.add(new LeafCardSelfEval("人", "person", "ren"));
            cards.add(new LeafCardSelfEval("猫", "cat", "mao"));
            cards.add(new LeafCardSelfEval("地球", "earth", "di qiu"));
            cards.add(new LeafCardSelfEval("时间", "time", "shi jian"));
            cards.add(new LeafCardSelfEval("世界", "world", "shi jie"));
            cards.add(new LeafCardSelfEval("电脑", "computer", "dian nao"));
            cards.add(new LeafCardSelfEval("软件", "software", "ruan jian"));
            ActivitySelfEval.setCardsPool(cards);
        	if (progress == null) {  
                startActivity(new Intent(HomePage.this, ActivitySelfEval.class));
            } else {
            	ActivitySelfEval.setCurrentCard(progress.getCurrentCard());
                startActivity(new Intent(HomePage.this, ActivitySelfEval.class)); 
//                new AlertDialog.Builder(HomePage.this)
//                        .setTitle("Continue?")
//                        .setMessage("You have a saved progress, do you want to continue? Current card: "+progress.getCurrentCard())
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                List<LeafCardSelfEval> cards = new LinkedList<LeafCardSelfEval>();
//                                cards.add(new LeafCardSelfEval("人", "person", "ren"));
//                                cards.add(new LeafCardSelfEval("猫", "cat", "mao"));
//                                cards.add(new LeafCardSelfEval("地球", "earth", "di qiu"));
//                                cards.add(new LeafCardSelfEval("时间", "time", "shi jian"));
//                                cards.add(new LeafCardSelfEval("世界", "world", "shi jie"));
//                                cards.add(new LeafCardSelfEval("电脑", "computer", "dian nao"));
//                                cards.add(new LeafCardSelfEval("软件", "software", "ruan jian"));
//                                ActivitySelfEval.setCardsPool(cards);
//                                startActivity(new Intent(HomePage.this, ActivitySelfEval.class));
//                            }
//                        })
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                ActivitySelfEval.setCardsPool(progress.getCardsPool());
//                                ActivitySelfEval.setCurrentCard(progress.getCurrentCard());
//                                startActivity(new Intent(HomePage.this, ActivitySelfEval.class));
//                            }
//                        }).create().show();
            }
        } else if (taskPage.equals("Type evaluation")) {
            //targetClass = TypeEvaluate.class;
        	InitDatabase.checkAndRefreshPerf(this, 2);
        	
        	final TypeEvalProgress progress = OpenwordsSharedPreferences.getTypeEvaluationProgress();
            List<LeafCardTypeEval> cards = new LinkedList<LeafCardTypeEval>();
            cards.add(new LeafCardTypeEval("人", "person", "ren"));
            cards.add(new LeafCardTypeEval("猫", "cat", "mao"));
            cards.add(new LeafCardTypeEval("地球", "earth", "di qiu"));
            cards.add(new LeafCardTypeEval("时间", "time", "shi jian"));
            cards.add(new LeafCardTypeEval("世界", "world", "shi jie"));
            cards.add(new LeafCardTypeEval("电脑", "computer", "dian nao"));
            cards.add(new LeafCardTypeEval("软件", "software", "ruan jian"));
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
//                                cards.add(new LeafCardTypeEval("人", "person", "ren"));
//                                cards.add(new LeafCardTypeEval("猫", "cat", "mao"));
//                                cards.add(new LeafCardTypeEval("地球", "earth", "di qiu"));
//                                cards.add(new LeafCardTypeEval("时间", "time", "shi jian"));
//                                cards.add(new LeafCardTypeEval("世界", "world", "shi jie"));
//                                cards.add(new LeafCardTypeEval("电脑", "computer", "dian nao"));
//                                cards.add(new LeafCardTypeEval("软件", "software", "ruan jian"));
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
        	InitDatabase.checkAndRefreshPerf(this, 3);
        	
        	final HearingProgress progress = OpenwordsSharedPreferences.getHearingProgress();
            List<LeafCardHearing> cards = new LinkedList<LeafCardHearing>();
            cards.add(new LeafCardHearing("人", "person", "ren"));
            cards.add(new LeafCardHearing("猫", "cat", "mao"));
            cards.add(new LeafCardHearing("地球", "earth", "di qiu"));
            cards.add(new LeafCardHearing("时间", "time", "shi jian"));
            cards.add(new LeafCardHearing("世界", "world", "shi jie"));
            cards.add(new LeafCardHearing("电脑", "computer", "dian nao"));
            cards.add(new LeafCardHearing("软件", "software", "ruan jian"));
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
//                                cards.add(new LeafCardHearing("人", "person", "ren"));
//                                cards.add(new LeafCardHearing("猫", "cat", "mao"));
//                                cards.add(new LeafCardHearing("地球", "earth", "di qiu"));
//                                cards.add(new LeafCardHearing("时间", "time", "shi jian"));
//                                cards.add(new LeafCardHearing("世界", "world", "shi jie"));
//                                cards.add(new LeafCardHearing("电脑", "computer", "dian nao"));
//                                cards.add(new LeafCardHearing("软件", "software", "ruan jian"));
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
