package com.openwords.view.actionbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.util.preference.OpenwordsSharedPreferences;

public class SettingsPage extends Activity {

    private Button buttonTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        //BackIcons.builder(this);

        buttonTest = (Button) findViewById(R.id.settingsPage_button_test);
        buttonTest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                boolean hide = OpenwordsSharedPreferences.getHidePortal();
                OpenwordsSharedPreferences.setHidePortal(!hide);
                Toast.makeText(SettingsPage.this, "Hide Portal is set to " + hide, Toast.LENGTH_LONG).show();
            }
        });
    }
}
