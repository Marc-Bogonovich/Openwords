package com.openwords.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.learningModule.Hearing;
import com.openwords.learningModule.Review;
import com.openwords.learningModule.SelfEvaluate;
import com.openwords.learningModule.StatsPage;
import com.openwords.learningModule.TutorialPage;
import com.openwords.learningModule.TypeEvaluate;
import com.openwords.learningModule.WordsPage;
import com.openwords.ui.common.ActionBarBuilderForActivity;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.util.log.LogUtil;

public class HomePage extends Activity implements OnClickListener {

    private Spinner begin, language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose

        setContentView(R.layout.activity_home_page);

        //build the action bar
        new ActionBarBuilderForActivity(this)
                .highlightSecondItem()
                .highlightFirstItem()
                .showBackButton(new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        Toast.makeText(HomePage.this, "Hi, the Back Button is clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .showTitle(R.id.actionbar_title, R.string.title_activity_homePage, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        Toast.makeText(HomePage.this, "Hi, the title is clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .showFirstItem(R.drawable.ic_setting_all, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        clickItemOther();
                    }
                })
                .showSecondItem(R.drawable.ic_setting_stat, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        Toast.makeText(HomePage.this, "Hi, I am going to open Stats", Toast.LENGTH_SHORT).show();
                    }
                });

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
        addItemsOnBegin();
        addItemsOnLanguage();
        Button testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
        testPageGo.setOnClickListener(this);
    }

    private void clickItemOther() {
        final DialogForSettingSelection dialog[] = new DialogForSettingSelection[1];
        dialog[0] = new DialogForSettingSelection(HomePage.this)
                .addItem("Home Page")
                .addItem("Word Page")
                .addItem("Statistics")
                .addItem("Menu")
                .addItem("Log Out")
                .build(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> av, View view, int position, long l) {
                        if (position == 0) {
                        } else if (position == 4) {
                            HomePage.this.finish();
                        }
                        dialog[0].cancel();
                    }
                }, 10, 60);
        dialog[0].show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.home_page, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.homePage_item_home:
//                Log.d("Icon click", "Home icon clicks");
//                return true;
//            case R.id.homePage_item_words:
//                Log.d("Icon click", "Word icon clicks");
//                wordsItemClick();
//                return true;
//            case R.id.homePage_item_stats:
//                Log.d("Icon click", "Stats icon clicks");
//                statsItemClick();
//                return true;
//            case R.id.homePage_item_profile:
//                Log.d("Icon click", "Profile icon clicks");
//                profileItemClick();
//                return true;
//            case R.id.homePage_item_settings:
//                Log.d("Icon click", "Setting icon clicks");
//                settingsItemClick();
//                return true;
//            case R.id.homePage_item_tutorial:
//                Log.d("Icon click", "Tutorial icon clicks");
//                tutorialItemClick();
//                return true;
//            case R.id.homePage_item_logout:
//                Log.d("Icon click", "Logout icon clicks");
//                logoutItemClick();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
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

    public void logoutItemClick() {

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
        // TODO Auto-generated method stub
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
