package com.openwords.ui.lily.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.InterfaceLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.SetInfo;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;

public class PageLMOption extends Activity {

    private GridView buttons;
    private ListAdapterWordSets listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_lily_lm_option_page);

        buttons = (GridView) findViewById(R.id.act_lmoption_grid1);

        List<SetInfo> setsAsButtons = new LinkedList<SetInfo>();
        setsAsButtons.add(new SetInfo("Review"));
        setsAsButtons.add(new SetInfo("Self-Evaluate"));
        setsAsButtons.add(new SetInfo("Hearing"));
        setsAsButtons.add(new SetInfo("Type-Evaluate"));
        setsAsButtons.add(new SetInfo("Sentence"));

        listAdapter = new ListAdapterWordSets(this, setsAsButtons, LocalSettings.getUserId());
        buttons.setAdapter(listAdapter);
        buttons.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    DataPool.LmType = InterfaceLearningModule.Learning_Type_Self;
                    startActivity(new Intent(PageLMOption.this, ActivityLearning.class));
                } else {
                    MyQuickToast.showShort(PageLMOption.this, "Sorry, this learning module is not supported yet.");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}