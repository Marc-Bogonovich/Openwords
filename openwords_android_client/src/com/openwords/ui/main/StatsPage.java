package com.openwords.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.UserLanguage;
import com.openwords.services.implementations.ServiceGetUserLanguages;
import com.openwords.services.implementations.ServiceGetUserPerformanceSum;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.other.ActionBarBuilder;
import com.openwords.util.ui.MyQuickToast;
import java.util.List;

public class StatsPage extends Activity {

    private TextView textAge;
    private TextView textLang;
    private TextView textWordprogress;
    private ProgressBar progressBar;
    private int mProgressStatus = 0;
    private ActionBarBuilder actionBar;

    private Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_stats_page);

        textAge = (TextView) findViewById(R.id.statsPage_TextView_age);
        textAge.setText("0 " + "years , " + "1 " + "months ");

        textLang = (TextView) findViewById(R.id.statsPage_TextView_languageName);
        textWordprogress = (TextView) findViewById(R.id.statsPage_TextView_wordProcess);

        //build ActionBar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Stats_Page);

//        progressBar = (ProgressBar) findViewById(R.id.statsPage_TextView_wordProcessBar);
//
//        new Thread(new Runnable() {
//            public void run() {
//                if (wordNumTotal != 0) {
//                    mProgressStatus = wordNumLearnt * 100 / wordNumTotal;
//                } else {
//                    mProgressStatus = 0;
//                }
//                //Update the progress bar
//                progressHandler.post(new Runnable() {
//                    public void run() {
//                        progressBar.setProgress(mProgressStatus);
//                    }
//                });
//            }
//        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.checkSetting();
        new ServiceGetUserLanguages().doRequest(LocalSettings.getUserId(), LocalSettings.getBaseLanguageId(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        List<UserLanguage> langs = (List<UserLanguage>) resultObject;
                        Language lang = Language.getLanguageInfo(langs.get(0).learningLang);
                        textLang.setText(lang.name);

                        new ServiceGetUserPerformanceSum().doRequest(LocalSettings.getUserId(), LocalSettings.getBaseLanguageId(), lang.langId,
                                new HttpResultHandler() {

                                    public void hasResult(Object resultObject) {
                                        ServiceGetUserPerformanceSum.Result r = (ServiceGetUserPerformanceSum.Result) resultObject;
                                        textWordprogress.setText(r.totalGood + "/" + r.totalWordsInLanguage);
                                    }

                                    public void noResult(String errorMessage) {
                                        MyQuickToast.showShort(StatsPage.this, errorMessage);
                                    }
                                });
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(StatsPage.this, errorMessage);
                    }
                });
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
