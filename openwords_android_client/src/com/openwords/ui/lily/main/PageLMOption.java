package com.openwords.ui.lily.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.InterfaceLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.SetInfo;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;

public class PageLMOption extends Activity {

    private GridView buttons;
    private ListAdapterWordSets listAdapter;
    private TextView title, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_lily_lm_option_page);

        title = (TextView) findViewById(R.id.act_lmoption_text1);
        title.setText(LocalizationManager.getTitlePractice());
        info = (TextView) findViewById(R.id.act_lmoption_text2);
        info.setText(LocalizationManager.getInfoPractice());
        buttons = (GridView) findViewById(R.id.act_lmoption_grid1);

        List<SetInfo> setsAsButtons = new LinkedList<SetInfo>();
        setsAsButtons.add(new SetInfo(LocalizationManager.getNameReview()));
        setsAsButtons.add(new SetInfo(LocalizationManager.getNameSelf()));
        setsAsButtons.add(new SetInfo(LocalizationManager.getNameHearing()));
        setsAsButtons.add(new SetInfo(LocalizationManager.getNameType()));
        setsAsButtons.add(new SetInfo(LocalizationManager.getNameSentence()));

        listAdapter = new ListAdapterWordSets(this, setsAsButtons, LocalSettings.getUserId());
        buttons.setAdapter(listAdapter);
        buttons.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    DataPool.LmType = InterfaceLearningModule.Learning_Type_Review;
                    startActivity(new Intent(PageLMOption.this, ActivityLearning.class));
                    PageLMOption.this.finish();
                } else if (position == 1) {
                    DataPool.LmType = InterfaceLearningModule.Learning_Type_Self;
                    startActivity(new Intent(PageLMOption.this, ActivityLearning.class));
                    PageLMOption.this.finish();
                } else if (position == 2) {
                    DataPool.LmType = InterfaceLearningModule.Learning_Type_Hearing;
                    startActivity(new Intent(PageLMOption.this, ActivityLearning.class));
                    PageLMOption.this.finish();
                } //                else if (position == 4) {
                //                    DataPool.LmType = InterfaceLearningModule.Learning_Type_Sentence;
                //                    startActivity(new Intent(PageLMOption.this, ActivityLearning.class));
                //                    PageLMOption.this.finish();
                //                } 
                else {
                    MyQuickToast.showShort(PageLMOption.this, LocalizationManager.getErroModuleSupport());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
