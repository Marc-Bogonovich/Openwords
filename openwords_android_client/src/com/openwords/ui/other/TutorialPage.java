package com.openwords.ui.other;

import android.app.Activity;
import android.os.Bundle;
import com.openwords.R;

public class TutorialPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_page);
        BackIcons.enable(this);
    }
}
