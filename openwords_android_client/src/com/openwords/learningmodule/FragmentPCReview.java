package com.openwords.learningmodule;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.openwords.R;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.model.UserPerformanceDirty;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;

public class FragmentPCReview extends Fragment {

    private static Handler RefreshHandler;
    private int user_id;

    public static void refreshDetails() {
        if (RefreshHandler != null) {
            RefreshHandler.sendEmptyMessage(0);
        }
    }

    private TextView vocabSize, performance, skip, birthday, birthdayDetail, evaluation;
    private Button newWords, nextPlate, exit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        user_id = OpenwordsSharedPreferences.getUserInfo().getUserId();
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
        newWords = (Button) myFragmentView.findViewById(R.id.plc_button_NewWords);
        nextPlate = (Button) myFragmentView.findViewById(R.id.plc_button_NextPlate);
        exit = (Button) myFragmentView.findViewById(R.id.plc_button_Exit);

        refresh();

        return myFragmentView;
    }

    private void refresh() {
        LogUtil.logDeubg(this, "refresh");
        int totalCards = 0;
        totalCards = ActivityReview.getCardsPool().size();

        for (LeafCard card : ActivityReview.getCardsPool()) {
        	new UserPerformanceDirty(card.getConnectionId(),user_id,0,card.getLastTime(),0,0,getActivity().getApplicationContext()).save();
        }
        vocabSize.setText("329"); //wait for varun figure out how to count how many word the user know
        performance.setText(totalCards + "/" + totalCards);
        skip.setText("");
        birthday.setText("");
        birthdayDetail.setText("");
        evaluation.setText("");
        
        
    }
}