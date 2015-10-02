package com.openwords.ui.lily.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.model.SetItem;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.main.LanguagePage;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import java.util.List;

public class PageHome extends Activity {

    private LinearLayout root;
    private TextView buttonSetList, buttonNewSet, buttonResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_lily_home_page);
        root = (LinearLayout) findViewById(R.id.act_home_root);
        root.setBackgroundColor(getResources().getColor(R.color.main_app_color));
        buttonSetList = (TextView) findViewById(R.id.act_home_button_1);
        buttonNewSet = (TextView) findViewById(R.id.act_home_button_2);
        buttonResume = (TextView) findViewById(R.id.act_home_button_3);
        buttonSetList.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonNewSet.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonResume.setTextColor(getResources().getColor(R.color.main_app_color));

        buttonSetList.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(PageHome.this, PageSetsList.class));
            }
        });
        buttonNewSet.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                DataPool.currentSet.name = null;
                DataPool.currentSetItems.clear();
                DataPool.currentSetItems.add(new SetItem(0, "(Native Lang)", "(Learning Lang)", true, true));
                startActivity(new Intent(PageHome.this, PageSetContent.class));
            }
        });
        buttonResume.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                MyQuickToast.showShort(PageHome.this, "Not supported yet");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
        final List<Language> languages = Language.getLearningLanguagesInfo(LocalSettings.getBaseLanguageId());
        if (languages.size() > 0) {
            Language currentLearn = languages.get(0);
            if (currentLearn.displayName.isEmpty()) {
                MyQuickToast.showLong(this, "Current learning language is " + currentLearn.name);
            } else {
                MyQuickToast.showLong(this, "Current learning language is " + currentLearn.displayName);
            }

        } else {
            startActivity(new Intent(PageHome.this, LanguagePage.class));
        }
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                PageHome.super.onBackPressed();
            }
        });
    }
}
