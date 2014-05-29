package com.openwords.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.openwords.R;
import com.openwords.util.log.LogUtil;
import com.openwords.view.actionbar.ActionBarIcons;
import com.openwords.view.learningModule.Hearing;
import com.openwords.view.learningModule.Review;
import com.openwords.view.learningModule.SelfEvaluate;
import com.openwords.view.learningModule.TypeEvaluate;

public class HomePage extends Activity implements OnClickListener {

    private Spinner begin, language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose

        setContentView(R.layout.activity_home_page);

        //build the action bar
        ActionBarIcons.builder(this);

        addItemsOnBegin();
        addItemsOnLanguage();
        Button testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
        testPageGo.setOnClickListener(this);
    }

    public void addItemsOnBegin() {
        begin = (Spinner) findViewById(R.id.homePage_Spinner_begin);
        ArrayAdapter<CharSequence> beginAdapter = ArrayAdapter.createFromResource(this, R.array.homePage_spinner_begin_array, android.R.layout.simple_spinner_item);
        beginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        begin.setAdapter(beginAdapter);
    }

    public void addItemsOnLanguage() {
        language = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this, R.array.homePage_Spinner_language_array, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(languageAdapter);
    }

    

    public void testPageButtonClick() {
        String taskPage = begin.getSelectedItem().toString();
        LogUtil.logDeubg(this, "Task: " + taskPage);
        Class targetClass = HomePage.class;
        if (taskPage.equals("Review")) {
            targetClass = Review.class;
        } else if (taskPage.equals("Self evaluation")) {
            targetClass = SelfEvaluate.class;
        } else if (taskPage.equals("Type evaluation")) {
            targetClass = TypeEvaluate.class;
        } else if (taskPage.equals("Hearing")) {
            targetClass = Hearing.class;
        }

        HomePage.this.startActivity(new Intent(HomePage.this, targetClass));
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
}
