package com.openwords.learningmodule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.UserLearningLanguages;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;

public class FragmentPCReview extends Fragment {

    private static Handler RefreshHandler;

    public static void refreshDetails() {
        if (RefreshHandler != null) {
            RefreshHandler.sendEmptyMessage(0);
        }
    }
    private TextView vocabSize, performance, skip, birthday, birthdayDetail, evaluation;
    private Button nextPlate, exit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        RefreshHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    refresh();
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView");

        View myFragmentView = inflater.inflate(R.layout.fragment_plate_completion, container, false);
        vocabSize = (TextView) myFragmentView.findViewById(R.id.plc_TextView_VocabSize);
        performance = (TextView) myFragmentView.findViewById(R.id.plc_TextView_Performance);
        skip = (TextView) myFragmentView.findViewById(R.id.plc_TextView_Skipped);
        birthday = (TextView) myFragmentView.findViewById(R.id.plc_TextView_Birthday);
        birthdayDetail = (TextView) myFragmentView.findViewById(R.id.plc_TextView_BirthdayDetail);
        evaluation = (TextView) myFragmentView.findViewById(R.id.plc_TextView_HumanEvaluation);
        //newWords = (Button) myFragmentView.findViewById(R.id.plc_button_NewWords);
        nextPlate = (Button) myFragmentView.findViewById(R.id.plc_button_NextPlate);
        nextPlate.setText(LocalizationManager.getTextPCNext());
        exit = (Button) myFragmentView.findViewById(R.id.plc_button_Exit);
        exit.setText(LocalizationManager.getTextPCEnd());

        nextPlate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                advanceToNextPageData();
                getActivity().finish();
                startActivity(new Intent(getActivity(), ActivityLearning.class));
            }
        });
        exit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                advanceToNextPageData();
                getActivity().finish();
            }
        });

        refresh();
        return myFragmentView;
    }

    private void advanceToNextPageData() {
        UserLearningLanguages languageInfo = UserLearningLanguages.getUserLanguageInfo(LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang);
        if (languageInfo == null) {
            MyQuickToast.showShort(getActivity(), "Error: no language information specified");
            getActivity().finish();
            return;
        }
        languageInfo.page += 1;
        languageInfo.save();
    }

    private void refresh() {
        LogUtil.logDeubg(this, "refresh");
        int totalCards = 0;
        totalCards = 10;

        vocabSize.setText("329");
        performance.setText(totalCards + "/" + totalCards);
        skip.setText("");
        birthday.setText("");
        birthdayDetail.setText("");
        evaluation.setText("");
    }
}
