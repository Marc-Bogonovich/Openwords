package com.openwords.ui.lily.lm;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.FragmentLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.SetItem;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;

public class PageReview extends FragmentLearningModule {

    private final int cardIndex;
    private final ActivityLearning lmActivity;
    private View myFragmentView;
    private ViewSoundBackground soundButton;
    private AutoResizeTextView problem, tran, answer;
    private ImageView buttonOption;
    private SetItem item;
    private DialogForSettingSelection settingDialog;

    public PageReview(int cardIndex, ActivityLearning lmActivity) {
        this.cardIndex = cardIndex;
        this.lmActivity = lmActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate for card: " + cardIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);

        myFragmentView = inflater.inflate(R.layout.lily_page_lm_review, container, false);
        item = DataPool.currentSetItems.get(cardIndex);

        buttonOption = (ImageView) myFragmentView.findViewById(R.id.page_review_image1);
        buttonOption.setColorFilter(DataPool.Color_Main, PorterDuff.Mode.MULTIPLY);
        buttonOption.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (settingDialog != null) {
                    settingDialog.cancel();
                }
                settingDialog = new DialogForSettingSelection(lmActivity)
                        .addItem("Comment")
                        .addItem("Stop")
                        .build(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0:
                                        MyQuickToast.showShort(lmActivity, "Comment is not supported yet.");
                                        break;
                                    case 1:
                                        lmActivity.finish();
                                        break;
                                }
                                settingDialog.cancel();
                                settingDialog = null;
                            }
                        }, (int) buttonOption.getX(), (int) buttonOption.getY());
                settingDialog.show();
            }
        });

        soundButton = (ViewSoundBackground) myFragmentView.findViewById(R.id.lily_button_sound_bg);
        updateAudioIcon(soundButton, item.wordTwoId);

        tran = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_review_text_tran);
        if (item.twoTranscription == null) {
            item.twoTranscription = "";
        }
        tran.setText(item.twoTranscription);

        problem = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_review_text_problem);
        problem.setText(item.wordTwo);
        addClarificationTrigger(lmActivity, new View[]{problem}, 50, item.wordOneCommon);

        answer = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_review_text_answer);
        answer.setText(item.wordOne);

        return myFragmentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.logDeubg(this, "onDestroy for card: " + cardIndex);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.logDeubg(this, "onDetach for card: " + cardIndex);
    }
}