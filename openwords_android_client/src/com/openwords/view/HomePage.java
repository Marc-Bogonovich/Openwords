package com.openwords.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.openwords.model.JSONParser;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardHearing;
import com.openwords.model.LeafCardHearingAdapter;
import com.openwords.model.LeafCardReviewAdapter;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.model.LeafCardSelfEvalAdapter;
import com.openwords.model.LeafCardTypeEval;
import com.openwords.model.LeafCardTypeEvalAdapter;
import com.openwords.model.UserInfo;
import com.openwords.model.UserWords;
import com.openwords.model.WordTranscription;
import com.openwords.services.GetLanguages;
import com.openwords.services.GetWords;
import com.openwords.services.ModelLanguage;
import com.openwords.services.ModelWordConnection;
import com.openwords.util.TimeConvertor;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.view.actionbar.ActionBarBuilder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class HomePage extends Activity implements OnClickListener {

    //private static JSONArray jArrMain;
    private static final String url_write_downloaded_words_to_server = "http://www.openwords.org/ServerPages/OpenwordsDB/setUserWords.php";
    private static Spinner begin, l2_dropdown;
    public static List<ModelLanguage> LanguageList = null;
    public static int pos = -1;
    //-----------
    private static int language_position = -1;
    //-----------
    private List<String> languageOptions;
    private ArrayAdapter<String> dropdownAdapter;
    public int homelang_id;
    public String homelang_name;
    private ProgressDialog pDialog = null;
    private UserInfo userinfo;
    private int SIZE = 10;
    private ActionBarBuilder actionBar;
    private final AtomicBoolean canRefresh = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_home_page);

        //building the action bar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Home_Page);

        // Creating the drop down object
        l2_dropdown = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);

        begin = (Spinner) findViewById(R.id.homePage_Spinner_begin);
        ArrayAdapter<CharSequence> beginAdapter = ArrayAdapter.createFromResource(this, R.array.homePage_spinner_begin_array, android.R.layout.simple_spinner_item);
        beginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        begin.setAdapter(beginAdapter);

        Button testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
        testPageGo.setOnClickListener(HomePage.this);

        userinfo = OpenwordsSharedPreferences.getUserInfo();

        //this asynchnous call should be made in the LoginPage after user successfully login, 
        //but right now LoginPage has too many arbitrary threads which are not allowing embedding this Async Http request yet, will do that later
        //refreshLanguageOptions();
        GetLanguages.request(Integer.toString(userinfo.getUserId()), 0, new GetLanguages.AsyncCallback() {

            public void callback(List<ModelLanguage> languages, Throwable error) {
                if (languages != null) {
                    languages.add(new ModelLanguage(-999, "Add more languages"));
                    LanguageList = languages;
                    fillLanguageOptions();
                } else {
                    Toast.makeText(HomePage.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refreshLanguageOptions() {
// This part of code doesn't work, since the LanguageList remains the same with previous
    	if(canRefresh.get()) {
            LogUtil.logDeubg(this, "refreshLanguageOptions");
            languageOptions.clear();
            for (ModelLanguage l : LanguageList) {
                languageOptions.add(l.getL2name());
            }
            dropdownAdapter.notifyDataSetChanged();
            canRefresh.set(false);
    	}
    }

    private void fillLanguageOptions() {
        if (LanguageList != null) {
            languageOptions = new LinkedList<String>();
            for (ModelLanguage l : LanguageList) {
                languageOptions.add(l.getL2name());
                LogUtil.logDeubg(this, "user " + userinfo.getUserId() + " has lang: " + l.getL2name());
                if (l.getL2id() == OpenwordsSharedPreferences.getUserInfo().getLang_id()) {
                	language_position = languageOptions.size() - 1;
                }
            }

            dropdownAdapter = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_list_item_1, android.R.id.text1, languageOptions);
            l2_dropdown.setAdapter(dropdownAdapter);
            l2_dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Log.d("whatever", Integer.toString(position));
                    pos = position;
                    Log.d("ID", Integer.toString(LanguageList.get(position).getL2id()));
                    if (LanguageList.get(position).getL2id() == -999) {
                    	canRefresh.set(true);
                        HomePage.this.startActivity(new Intent(HomePage.this, LanguagePage.class));
                    } else {
                        homelang_id = LanguageList.get(position).getL2id();
                        homelang_name = LanguageList.get(position).getL2name(); //new
                        UserInfo user = OpenwordsSharedPreferences.getUserInfo();
                        user.setLang_id(homelang_id);
                        user.setLang_Name(homelang_name); //new
                        OpenwordsSharedPreferences.setUserInfo(user);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomePage.this, "Selected Lang: "+OpenwordsSharedPreferences.getUserInfo().getLang_Name()+" ID: "+OpenwordsSharedPreferences.getUserInfo().getLang_id(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    
                        Log.d("saved Lang", "" + OpenwordsSharedPreferences.getUserInfo().getLang_id()
                                + OpenwordsSharedPreferences.getUserInfo().getLang_Name()); //new
                        //-----getting first x words if not present----
                        //new GetFirstWords().execute();
                        getFirstWords();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            if (language_position != -1) {
                l2_dropdown.setSelection(language_position);
            }

        }
    }

    //-------- Getting first 10 words from server for a language -----------------
    /*public void getFirstWordsFromServer() {
     ArrayList<WordsPageTool> words_list = new ArrayList<WordsPageTool>();
     try {
     List<NameValuePair> params = new ArrayList<NameValuePair>(3);
     params.add(new BasicNameValuePair("user", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getUserId())));
     params.add(new BasicNameValuePair("langOne", "1"));
     params.add(new BasicNameValuePair("langTwo", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getLang_id())));
     LogUtil.logDeubg(HomePage.this, params.toString());
     JSONParser jsonParse = new JSONParser();
     JSONObject jObj = jsonParse.makeHttpRequest(nextwords_url, "POST", params);
     Log.d("Obj", jObj.toString());
     if (jObj.getInt("success") == 1) {
     JSONArray jArr = jObj.getJSONArray("data");
     String abc = Integer.toString(jArr.length());
     Log.d("Array", abc);
     jArrMain = jArr;
     }

     } catch (Exception e) {
     e.printStackTrace();
     }
     }*/
    //---------------------------------------------------------------------------------------------
    //------------update downloaded word on server----------------------------------------------
    public void updateWordsOnServer(String conIds, long dTime) {
        LogUtil.logDeubg(HomePage.this, "updateWordsOnServer: " + conIds);
        try {
            int user = OpenwordsSharedPreferences.getUserInfo().getUserId();
            int langTwo = OpenwordsSharedPreferences.getUserInfo().getLang_id();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("conid", conIds));
            params.add(new BasicNameValuePair("dtime", Long.toString(dTime)));
            params.add(new BasicNameValuePair("user", Integer.toString(user)));
            params.add(new BasicNameValuePair("lTwo", Integer.toString(langTwo)));
            JSONParser jsonParse = new JSONParser();
            JSONObject jObj = jsonParse.makeHttpRequest(url_write_downloaded_words_to_server, "POST", params);

            if (jObj.getInt("success") == 1) {
                Log.d("Message From Server", jObj.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void testPageButtonClick() {
        String taskPage = begin.getSelectedItem().toString();
        LogUtil.logDeubg(this, "Task: " + taskPage);
        pDialog = ProgressDialog.show(HomePage.this, "",
                "Assembling leaf cards", true);
        if (taskPage.equals("Review")) {
            new Thread(new Runnable() {
                List<LeafCard> cards;

                public void run() {
                    final ProgressReview progress = OpenwordsSharedPreferences.getReviewProgress();
                    //check if there is a unfinished progress before AND the progress is using the same language
                    if (progress == null || progress.getLanguageID() != OpenwordsSharedPreferences.getUserInfo().getLang_id()) {

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
                    final ProgressSelfEval progress = OpenwordsSharedPreferences.getSelfEvaluationProgress();
                    if (progress == null || progress.getLanguageID() != OpenwordsSharedPreferences.getUserInfo().getLang_id()) {

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
        } else if (taskPage.equals("Type evaluation")) {
            new Thread(new Runnable() {
                List<LeafCardTypeEval> cards;

                public void run() {
                    final ProgressTypeEval progress = OpenwordsSharedPreferences.getTypeEvaluationProgress();
                    if (progress == null || progress.getLanguageID() != OpenwordsSharedPreferences.getUserInfo().getLang_id()) {

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
                    final ProgressHearing progress = OpenwordsSharedPreferences.getHearingProgress();
                    if (progress == null || progress.getLanguageID() != OpenwordsSharedPreferences.getUserInfo().getLang_id()) {

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
        LogUtil.logDeubg(this, "onResume");
        actionBar.checkSetting();
        SIZE = OpenwordsSharedPreferences.getLeafCardSize();
        refreshLanguageOptions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homePage_Button_testPageGo:
                testPageButtonClick();
                break;
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

    private void getFirstWords() {
        int langId = OpenwordsSharedPreferences.getUserInfo().getLang_id();
        LogUtil.logDeubg(HomePage.this, "getFirstWords langId: " + langId);
        List<UserWords> existList = UserWords.findByLanguage(langId);
        if (existList.isEmpty()) {
            LogUtil.logDeubg(HomePage.this, "existList isEmpty");
            //getFirstWordsFromServer();
            GetWords.request(Integer.toString(OpenwordsSharedPreferences.getUserInfo().getUserId()),
                    "1",
                    Integer.toString(OpenwordsSharedPreferences.getUserInfo().getLang_id()),
                    0,
                    new GetWords.AsyncCallback() {

                        public void callback(List<ModelWordConnection> data, Throwable error) {
                            if (data != null) {
                                String conIds = "";
                                for (ModelWordConnection w : data) {
                                    try {
                                        //JSONObject c = jArrMain.getJSONObject(i);

                                        UserWords newUw = new UserWords(w.getConnectionId(), w.getWordl1(),
                                                w.getWordl1Text(), w.getWordl2(), w.getWordl2Text(),
                                                w.getL2id(), w.getL2name(), w.getAudio(), 1);
                                        newUw.save();

                                        WordTranscription t = new WordTranscription(w.getWordl2(), w.getTrans());
                                        t.save();

                                        if (conIds.isEmpty()) {
                                            conIds += w.getConnectionId();
                                        } else {
                                            conIds = conIds + "|" + w.getConnectionId();
                                        }

                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                                Toast.makeText(HomePage.this, "Your Words are ready", Toast.LENGTH_SHORT).show();
                                //update on server
                                updateWordsOnServer(conIds, TimeConvertor.getUnixTime());
                            } else {
                                Toast.makeText(HomePage.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
