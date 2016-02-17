package com.openwords.ui.lily.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;

public class DialogSetOverview extends Dialog {

    private final LinearLayout root;
    private final TextView textPurpose, textUser, textTime, textPopular, textFollow,
            titlePurpose, titleUser, titleTime, titlePopular;
    private SetDetails details;

    public DialogSetOverview(Context context, SetDetails details) {
        super(context);
        this.details = details;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.lily_page_set_overview);

        root = (LinearLayout) findViewById(R.id.page_so_root);
        textPurpose = (TextView) findViewById(R.id.page_so_text1);
        textUser = (TextView) findViewById(R.id.page_so_text2);
        textTime = (TextView) findViewById(R.id.page_so_text3);
        textPopular = (TextView) findViewById(R.id.page_so_text4);
        titlePurpose = (TextView) findViewById(R.id.page_so_text11);
        titleUser = (TextView) findViewById(R.id.page_so_text21);
        titleTime = (TextView) findViewById(R.id.page_so_text31);
        titlePopular = (TextView) findViewById(R.id.page_so_text41);

        textPurpose.setText(details.purpose);
        textUser.setText(details.user);
        textTime.setText(details.time);
        textPopular.setText(details.popular);
        textFollow = null;

        titlePurpose.setText("Purpose");
        titleUser.setText("Creator");
        titleTime.setText("Last update time");
        titlePopular.setText("Popular words");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
