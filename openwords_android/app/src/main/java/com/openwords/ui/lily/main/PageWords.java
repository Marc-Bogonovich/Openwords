package com.openwords.ui.lily.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.InterfaceLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.ResultSentencePack;
import com.openwords.model.SentenceConnection;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import java.util.List;

public class PageWords extends Activity {

    private LinearLayout root;
    private TextView buttonStudy, buttonManage, buttonDict, buttonTest, langText;
    private ImageView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.lily_page_words);
        root = (LinearLayout) findViewById(R.id.act_home_root);
        root.setBackgroundColor(getResources().getColor(R.color.main_app_color));
        buttonStudy = (TextView) findViewById(R.id.act_home_button_1);
        buttonManage = (TextView) findViewById(R.id.act_home_button_2);
        buttonDict = (TextView) findViewById(R.id.act_home_button_3);
        buttonTest = (TextView) findViewById(R.id.page_home_text_1);
        setting = (ImageView) findViewById(R.id.page_home_image1);
        langText = (TextView) findViewById(R.id.page_home_text_2);
        buttonStudy.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonManage.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonDict.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonStudy.setText(LocalizationManager.getButtonPractice());
        buttonManage.setText(LocalizationManager.getButtonManageSets());
        buttonDict.setText(LocalizationManager.getButtonResume());
        buttonTest.setText(LocalizationManager.getButtonSentence());

        buttonStudy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                PageSetsList.mode = PageSetsList.Mode_Study;
                startActivity(new Intent(PageWords.this, PageSetsList.class));
            }
        });
        buttonManage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (DataPool.OffLine) {
                    MyQuickToast.showShort(PageWords.this, "Cannot manage set in offline mode.");
                    return;
                }
                PageSetsList.mode = PageSetsList.Mode_Manage;
                startActivity(new Intent(PageWords.this, PageSetsList.class));
            }
        });
        buttonDict.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(PageWords.this, PageDictionary.class));
            }
        });

        buttonTest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SentenceConnection.loadSentencePack(LocalSettings.getBaseLanguageId(), LocalSettings.getLearningLanguage().langId, 5, new ResultSentencePack() {

                    public void result(List<SentenceConnection> connections) {
                        DataPool.currentSentences.clear();
                        DataPool.currentSentences.addAll(connections);
                        DataPool.LmType = InterfaceLearningModule.Learning_Type_Sentence;
                        startActivity(new Intent(PageWords.this, ActivityLearning.class));
                    }

                    public void error(String error) {
                        MyQuickToast.showShort(PageWords.this, error);
                    }
                });
            }
        });

        langText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Dialog d = new DialogLearnLang(PageWords.this, new DialogLearnLang.LanguagePicked() {

                    public void done() {
                        Language currentLearn = LocalSettings.getLearningLanguage();
                        if (currentLearn.displayName.isEmpty()) {
                            langText.setText(currentLearn.name);
                        } else {
                            langText.setText(currentLearn.displayName);
                        }
                    }
                });
                d.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
        Language currentLearn = LocalSettings.getLearningLanguage();
        if (currentLearn.displayName.isEmpty()) {
            langText.setText(currentLearn.name);
        } else {
            langText.setText(currentLearn.displayName);
        }
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                PageWords.super.onBackPressed();
            }
        });
    }
}