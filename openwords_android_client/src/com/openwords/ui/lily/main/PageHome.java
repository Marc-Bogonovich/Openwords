package com.openwords.ui.lily.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.util.log.LogUtil;

public class PageHome extends Activity {

    private LinearLayout root;
    private TextView textButton1, textButton2, textButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_lily_home_page);
        root = (LinearLayout) findViewById(R.id.act_home_root);
        root.setBackgroundColor(Color.parseColor("#70B5A4"));
        textButton1 = (TextView) findViewById(R.id.act_home_button_1);
        textButton2 = (TextView) findViewById(R.id.act_home_button_2);
        textButton3 = (TextView) findViewById(R.id.act_home_button_3);
        textButton1.setTextColor(Color.parseColor("#70B5A4"));
        textButton2.setTextColor(Color.parseColor("#70B5A4"));
        textButton3.setTextColor(Color.parseColor("#70B5A4"));
        textButton2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startActivity(new Intent(PageHome.this, PageModifyWordSet.class));
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
