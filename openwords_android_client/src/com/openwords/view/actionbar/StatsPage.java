package com.openwords.view.actionbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.openwords.R;

public class StatsPage extends Activity {
	ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose

        setContentView(R.layout.activity_stats_page);
        //build ActionBar
        //ActionBarIcons.builder(this);
        new ActionBarBuilderForMainPages(this, ActionBarBuilderForMainPages.Stats_Page);
//        
//        progressBar = (ProgressBar) findViewById(R.id.statsPage_TextView_wordProcessBar);
//        progressBar.setProgress((int) 3);
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
