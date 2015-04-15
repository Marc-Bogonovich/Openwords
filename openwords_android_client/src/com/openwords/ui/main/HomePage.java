package com.openwords.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.InterfaceLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.UserLanguage;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.other.ActionBarBuilder;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;

public class HomePage extends Activity {

    private Spinner learningModuleOption, languageOption;
    private ActionBarBuilder actionBar;
    private int learningType;
    private Button testPageGo;
    private Language nextLangToLearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_home_page);
        //building the action bar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Home_Page);

        languageOption = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);
        learningModuleOption = (Spinner) findViewById(R.id.homePage_Spinner_begin);
        learningModuleOption.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> av, View view, int i, long l) {
                switch (i) {
                    case 0:
                        learningType = InterfaceLearningModule.Learning_Type_Review;
                        break;
                    case 1:
                        learningType = InterfaceLearningModule.Learning_Type_Self;
                        break;
                    case 2:
                        learningType = InterfaceLearningModule.Learning_Type_Type;
                        break;
                    case 3:
                        learningType = InterfaceLearningModule.Learning_Type_Hearing;
                        break;
                    default:
                        learningType = 0;
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> av) {
                //do nothing
            }
        });

        testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
        testPageGo.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                testPageButtonClick();
            }
        });
    }

    private void fillLearningOptionUI() {
        if (nextLangToLearn == null) {
            return;
        }
        UserLanguage ul = UserLanguage.getUserLanguageInfo(LocalSettings.getBaseLanguageId(), nextLangToLearn.langId);
        if (ul == null) {
            MyQuickToast.showShort(this, "No language is selected");
            return;
        }
        DataPool.LmLearningLang = nextLangToLearn.langId;
        String stepInfo = " " + nextLangToLearn.displayName + " set " + ul.page;
        String[] options = new String[]{
            LocalizationManager.getTextOptionReview() + stepInfo,
            LocalizationManager.getTextOptionSelf() + stepInfo,
            LocalizationManager.getTextOptionType() + stepInfo,
            LocalizationManager.getTextOptionHearing() + stepInfo
        };
        learningModuleOption.setAdapter(new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, options));
    }

    private void refreshLanguageOptions() {
        LogUtil.logDeubg(this, "refreshLanguageOptions");
        nextLangToLearn = null;
        final List<Language> languages = Language.getLearningLanguagesInfo(LocalSettings.getBaseLanguageId());
        List<String> languageNames = new LinkedList<String>();
        if (!languages.isEmpty()) {
            languageOption.setOnTouchListener(null);
            for (Language lang : languages) {
                languageNames.add(lang.name);
            }
        } else {
            final boolean[] touched = new boolean[]{false};
            languageOption.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View view, MotionEvent me) {
                    if (touched[0]) {
                        return true;
                    }
                    touched[0] = true;
                    if (!DataPool.OffLine) {
                        startActivity(new Intent(HomePage.this, LanguagePage.class));
                    }
                    return true;
                }
            });
        }

        languageNames.add(LocalizationManager.getTextMoreLang());
        languageOption.setAdapter(new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_list_item_1, android.R.id.text1, languageNames));
        languageOption.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (languages.isEmpty()) {
                    return;
                }
                if (position >= languages.size()) {
                    startActivity(new Intent(HomePage.this, LanguagePage.class));
                    return;
                }
                nextLangToLearn = languages.get(position);
                LogUtil.logDeubg(this, "got " + nextLangToLearn.name + " " + nextLangToLearn.langId);
                fillLearningOptionUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                LogUtil.logDeubg(this, "onNothingSelected");
            }
        });
    }

    public void testPageButtonClick() {
        if (nextLangToLearn != null) {
            DataPool.LmType = learningType;
            DataPool.LmLearningLang = nextLangToLearn.langId;
            startActivity(new Intent(HomePage.this, ActivityLearning.class));
        } else {
            MyQuickToast.showShort(this, "You need to chose a language to learn");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
        actionBar.checkSetting();
        refreshLanguageOptions();
        testPageGo.setText(LocalizationManager.getTextGo());
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                HomePage.super.onBackPressed();
            }
        });
    }
}
