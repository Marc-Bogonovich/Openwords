package com.openwords.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.openwords.R;
import com.openwords.ui.common.ActionBarBuilderForActivity;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.util.log.LogUtil;
import com.openwords.view.actionbar.ProfilePage;
import com.openwords.view.actionbar.SettingsPage;
import com.openwords.view.actionbar.StatsPage;
import com.openwords.view.actionbar.TutorialPage;
import com.openwords.view.actionbar.WordsPage;
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
        new ActionBarBuilderForActivity(this)
                .highlightFourthItem()
                //                .showBackButton(new ActionBarBuilderForActivity.ActionBarItemClickAction() {
                //
                //                    public void clicked() {
                //                        Toast.makeText(HomePage.this, "Hi, the Back Button is clicked", Toast.LENGTH_SHORT).show();
                //                    }
                //                })
                //                .showTitle(R.id.actionbar_title, R.string.title_activity_homePage, new ActionBarBuilderForActivity.ActionBarItemClickAction() {
                //
                //                    public void clicked() {
                //                        Toast.makeText(HomePage.this, "Hi, the title is clicked", Toast.LENGTH_SHORT).show();
                //                    }
                //                })
                .showFirstItem(R.drawable.ic_actionbar_others, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        clickItemOther();
                    }
                })
                .showSecondItem(R.drawable.ic_actionbar_stats, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        statsItemClick();
                    }
                })
                .showThirdItem(R.drawable.ic_actionbar_words, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        wordsItemClick();
                    }
                })
                .showFourthItem(R.drawable.ic_actionbar_home, null);

        addItemsOnBegin();
        addItemsOnLanguage();
        Button testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
        testPageGo.setOnClickListener(this);
    }

    private void clickItemOther() {
        final DialogForSettingSelection dialog[] = new DialogForSettingSelection[1];
        dialog[0] = new DialogForSettingSelection(HomePage.this)
                .addItem("Profile")
                .addItem("Setting")
                .addItem("Tutorial")
                .addItem("Log Out")
                .build(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> av, View view, int position, long l) {
                        if (position == 0) {
                        } else if (position == 3) {
                            HomePage.this.finish();
                        }
                        dialog[0].cancel();
                    }
                }, 10, 60);
        dialog[0].show();
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

    public void wordsItemClick() {
        HomePage.this.startActivity(new Intent(HomePage.this, WordsPage.class));
    }

    public void statsItemClick() {

        HomePage.this.startActivity(new Intent(HomePage.this, StatsPage.class));
    }

    public void profileItemClick() {
        HomePage.this.startActivity(new Intent(HomePage.this, ProfilePage.class));
    }

    public void settingsItemClick() {
        HomePage.this.startActivity(new Intent(HomePage.this, SettingsPage.class));
    }

    public void tutorialItemClick() {
        HomePage.this.startActivity(new Intent(HomePage.this, TutorialPage.class));
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
