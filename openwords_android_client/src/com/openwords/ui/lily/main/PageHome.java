package com.openwords.ui.lily.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;

public class PageHome extends Activity {

    private LinearLayout root;
    private TextView buttonLM, buttonSet, buttonResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_lily_home_page);
        root = (LinearLayout) findViewById(R.id.act_home_root);
        root.setBackgroundColor(getResources().getColor(R.color.main_app_color));
        buttonLM = (TextView) findViewById(R.id.act_home_button_1);
        buttonSet = (TextView) findViewById(R.id.act_home_button_2);
        buttonResume = (TextView) findViewById(R.id.act_home_button_3);
        buttonLM.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonSet.setTextColor(getResources().getColor(R.color.main_app_color));
        buttonResume.setTextColor(getResources().getColor(R.color.main_app_color));

        buttonLM.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(PageHome.this, PageSetsList.class));
            }
        });
        buttonSet.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
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