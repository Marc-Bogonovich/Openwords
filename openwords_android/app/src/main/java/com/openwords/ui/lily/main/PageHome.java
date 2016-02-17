package com.openwords.ui.lily.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.WindowManager;
import com.openwords.R;

public class PageHome extends Activity {

    private CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.lily_page_home);

        card = (CardView) findViewById(R.id.page_card_view);
    }

}
