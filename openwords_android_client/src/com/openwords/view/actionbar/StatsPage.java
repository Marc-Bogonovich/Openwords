package com.openwords.view.actionbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ProgressBar;
import com.openwords.R;
import com.openwords.util.preference.OpenwordsSharedPreferences;

import android.os.Handler;

public class StatsPage extends Activity {

    private TextView text_age;
    private TextView text_lang;
    private TextView text_wordprogress;
    private ProgressBar progressBar;
    private int mProgressStatus = 0;
    private ActionBarBuilder actionBar;
    String languageName;

    private Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose

        setContentView(R.layout.activity_stats_page);

        text_age = (TextView) findViewById(R.id.statsPage_TextView_age);

        text_age.setText("0 " + "years , " + "1 " + "months ");

        languageName = OpenwordsSharedPreferences.getUserInfo().getLang_Name();
        text_lang = (TextView) findViewById(R.id.statsPage_TextView_languageName);
        text_lang.setText(languageName);
        
        text_wordprogress = (TextView) findViewById(R.id.statsPage_TextView_wordProcess);
        text_wordprogress.setText("0/0");

        //build ActionBar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Stats_Page);

        progressBar = (ProgressBar) findViewById(R.id.statsPage_TextView_wordProcessBar);

        new Thread(new Runnable() {
            public void run() {
                mProgressStatus = 50;
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
        new AlertDialog.Builder(this)
                .setTitle("Really?")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        StatsPage.super.onBackPressed();
                    }
                }).create().show();
    }
}
