package com.openwords.ui.lily.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;

public class PageDictionary extends Activity {

    private LinearLayout root;
    private TextView buttonStudy, buttonManage, buttonDict, buttonTest, langText;
    private ImageView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.lily_page_dictionary);
        root = (LinearLayout) findViewById(R.id.page_dict_root);
        root.setBackgroundColor(getResources().getColor(R.color.main_app_color));

    }

}
