package com.openwords.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.other.ActionBarBuilder;

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

//        userID = OpenwordsSharedPreferences.getUserInfo().getUserId();
//        languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
//        wordNumTotal = getWordCountOfLanguagefromServer();
//        wordNumLearnt = UserPerformance.findNumberOfWordsLearnt(userID, languageID);
//        text_age = (TextView) findViewById(R.id.statsPage_TextView_age);
//
//        text_age.setText("0 " + "years , " + "1 " + "months ");
//
//        languageName = OpenwordsSharedPreferences.getUserInfo().getLang_Name();
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

  
}
