package com.openwords.view.actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.JSONParser;
import com.openwords.model.UserPerformance;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class StatsPage extends Activity {

    private static String url_get_word_count = "http://www.openwords.org/ServerPages/WordsDB/getNumberOfWordsInLanguage.php";
    private TextView text_age;
    private TextView text_lang;
    private TextView text_wordprogress;
    private ProgressBar progressBar;
    private int mProgressStatus = 0;
    private ActionBarBuilder actionBar;
    String languageName;
    int userID;
    int languageID;
    int wordNumLearnt;
    int wordNumTotal;

    private Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose

        setContentView(R.layout.activity_stats_page);

        userID = OpenwordsSharedPreferences.getUserInfo().getUserId();
        languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
        wordNumTotal = getWordCountOfLanguagefromServer();
        wordNumLearnt = UserPerformance.findNumberOfWordsLearnt(userID, languageID);
        text_age = (TextView) findViewById(R.id.statsPage_TextView_age);

        text_age.setText("0 " + "years , " + "1 " + "months ");

        languageName = OpenwordsSharedPreferences.getUserInfo().getLang_Name();
        text_lang = (TextView) findViewById(R.id.statsPage_TextView_languageName);
        text_lang.setText(languageName);

        text_wordprogress = (TextView) findViewById(R.id.statsPage_TextView_wordProcess);
        text_wordprogress.setText(wordNumLearnt + "/" + wordNumTotal);

        //build ActionBar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Stats_Page);

        progressBar = (ProgressBar) findViewById(R.id.statsPage_TextView_wordProcessBar);

        new Thread(new Runnable() {
            public void run() {
                if (wordNumTotal != 0) {
                    mProgressStatus = wordNumLearnt * 100 / wordNumTotal;
                } else {
                    mProgressStatus = 0;
                }
                //Update the progress bar
                progressHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setProgress(mProgressStatus);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.checkSetting();
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                StatsPage.super.onBackPressed();
            }
        });
    }

    //****************** methods for getting language word count from server**********
    public int getWordCountOfLanguagefromServer() {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("language", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getLang_id())));

            JSONParser jsonParse = new JSONParser();
            JSONObject jObj = jsonParse.makeHttpRequest(url_get_word_count, "POST", params);
            if (jObj.getInt("success") == 1) {
                return jObj.getInt("count");
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
