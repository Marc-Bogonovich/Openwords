package com.openwords.ui.lily.multiword;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import com.openwords.R;

public class PageMultiwordDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_multiword);
    }

}
