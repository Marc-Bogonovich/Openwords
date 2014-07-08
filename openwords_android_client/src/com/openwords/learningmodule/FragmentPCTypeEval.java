package com.openwords.learningmodule;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.openwords.R;
import com.openwords.model.InitDatabase;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardAdapter;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.model.LeafCardTypeEval;
import com.openwords.model.LeafCardTypeEvalAdapter;
import com.openwords.model.UserPerformanceDirty;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.view.actionbar.WordsPage;

public class FragmentPCTypeEval extends Fragment {
	private Activity activity;
    private static Handler RefreshHandler;
    private int user_id;
    private int SIZE = 10;
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
        OpenwordsSharedPreferences.setTypeEvaluationProgress(null);
        activity = getActivity();
        user_id = OpenwordsSharedPreferences.getUserInfo().getUserId();
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
        newWords = (Button) myFragmentView.findViewById(R.id.plc_button_NewWords);
        nextPlate = (Button) myFragmentView.findViewById(R.id.plc_button_NextPlate);
        exit = (Button) myFragmentView.findViewById(R.id.plc_button_Exit);
        newWords.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
	             getActivity().finish();
	             Intent i = new Intent(activity, WordsPage.class);
	             i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	             activity.startActivity(i);
            }
    });
    
    nextPlate.setOnClickListener(new OnClickListener() {
        public void onClick(View view) {
             getActivity().finish();
             List<LeafCardTypeEval> cards = new LeafCardTypeEvalAdapter(getActivity()).getList(SIZE);
             ActivityTypeEval.setCardsPool(cards);
             startActivity(new Intent(getActivity(), ActivityTypeEval.class));
        }
});
    exit.setOnClickListener(new OnClickListener() {
        public void onClick(View view) {
             getActivity().finish();
        }
});
        refresh();

        return myFragmentView;
    }

    private void refresh() {
        LogUtil.logDeubg(this, "refresh");
        int totalCards, totalCorrect = 0, totalSkipped = 0;
        totalCards = ActivityTypeEval.getCardsPool().size();

        for (LeafCardTypeEval card : ActivityTypeEval.getCardsPool()) {
        	//type -- module index : review -- 0, self -- 1, type -- 2, hearing -- 3
        	//performance : 0 -- null, 1 -- wrong, 2 -- close, 3 -- right
        	new UserPerformanceDirty(card.getConnectionId(),user_id,2,card.getLastTime(),card.getUserChoice(),0,getActivity().getApplicationContext()).save();
            if (card.getUserChoice() == 0) {
                totalSkipped++;
            } else {
                if (card.getUserChoice()==3) {
                    totalCorrect++;
                }
            }
        }
        
        new Thread(new Runnable(){
        	public void run()
        	{InitDatabase.updateLocalPerformanceSummary(getActivity().getApplicationContext());}
        }).start();
        
        vocabSize.setText("0 + " + totalCorrect);
        performance.setText(totalCorrect + "/" + totalCards);
        skip.setText(totalSkipped + " Skipped");
        birthday.setText("__ Birthday");
        birthdayDetail.setText("");
        evaluation.setText("");
    }
}
