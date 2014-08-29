package com.openwords.view.actionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.openwords.R;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.main.HomePage;
import com.openwords.util.preference.OpenwordsSharedPreferences;

public class PortalPage extends Activity {

    private ActionBarBuilder actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_page);
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Portal_Page);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenwordsSharedPreferences.getHidePortal()) {
            finish();
            startActivity(new Intent(this, HomePage.class));
        }
        actionBar.checkSetting();
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                PortalPage.super.onBackPressed();
            }
        });
    }
}
