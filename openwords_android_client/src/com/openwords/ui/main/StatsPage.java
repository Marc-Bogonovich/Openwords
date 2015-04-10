package com.openwords.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.UserLanguage;
import com.openwords.services.implementations.ServiceGetUserLanguages;
import com.openwords.services.implementations.ServiceGetUserPerformanceSum;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.other.ActionBarBuilder;
import com.openwords.util.gson.MyGson;
import com.openwords.util.ui.MyQuickToast;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class StatsPage extends Activity {

    private TextView textAge, textLang, textWordprogress;
    private ProgressBar progressBar;
    private ActionBarBuilder actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_stats_page);
        //build ActionBar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Stats_Page);

        textAge = (TextView) findViewById(R.id.statsPage_TextView_age);
        textAge.setText("0 " + "years , " + "0 " + "months ");

        textLang = (TextView) findViewById(R.id.statsPage_TextView_languageName);
        textWordprogress = (TextView) findViewById(R.id.statsPage_TextView_wordProcess);
        progressBar = (ProgressBar) findViewById(R.id.statsPage_TextView_wordProcessBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.checkSetting();
        new ServiceGetUserLanguages().doRequest(LocalSettings.getUserId(), LocalSettings.getBaseLanguageId(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        List<UserLanguage> langs = (List<UserLanguage>) resultObject;
                        refreshStatsView(langs);
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(StatsPage.this, errorMessage);
                        refreshStatsView(null);
                    }
                });
    }

    private void refreshStatsView(List<UserLanguage> langs) {
        final Language[] lang = new Language[1];
        for (UserLanguage ul : langs) {
            if (ul.learningLang == DataPool.LmLearningLang) {
                lang[0] = Language.getLanguageInfo(ul.learningLang);
            }
        }
        textLang.setText(lang[0].name);

        new ServiceGetUserPerformanceSum().doRequest(LocalSettings.getUserId(), LocalSettings.getBaseLanguageId(), lang[0].langId,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        ServiceGetUserPerformanceSum.Result r = (ServiceGetUserPerformanceSum.Result) resultObject;

                        Type collectionType = new TypeToken<Map<Integer, ServiceGetUserPerformanceSum.Result>>() {
                        }.getType();
                        Map<Integer, ServiceGetUserPerformanceSum.Result> oldRecords
                        = (Map<Integer, ServiceGetUserPerformanceSum.Result>) MyGson.fromJson(LocalSettings.getPreviousStatsData(), collectionType);

                        oldRecords.put(lang[0].langId, r);
                        LocalSettings.setPreviousStatsData(MyGson.toJson(oldRecords));

                        refreshStatsUI(r);
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(StatsPage.this, errorMessage);

                        Type collectionType = new TypeToken<Map<Integer, ServiceGetUserPerformanceSum.Result>>() {
                        }.getType();
                        Map<Integer, ServiceGetUserPerformanceSum.Result> oldRecords
                        = (Map<Integer, ServiceGetUserPerformanceSum.Result>) MyGson.fromJson(LocalSettings.getPreviousStatsData(), collectionType);

                        refreshStatsUI(oldRecords.get(lang[0].langId));
                    }
                });
    }

    private void refreshStatsUI(ServiceGetUserPerformanceSum.Result result) {
        textWordprogress.setText(result.totalGood + "/" + result.totalWordsInLanguage);
        double per = (double) result.totalGood * 100 / result.totalWordsInLanguage;
        progressBar.setProgress((int) per);
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
