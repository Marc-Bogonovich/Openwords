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
import android.util.Log;
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
import com.openwords.model.LeafCardSelfEvalAdapter;
import com.openwords.model.UserPerformanceDirty;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.view.HomePage;
import com.openwords.view.actionbar.WordsPage;

public class FragmentPCReview extends Fragment {
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
        LogUtil.logDeubg(this, "onCreate");
        OpenwordsSharedPreferences.setReviewProgress(null);
        activity = getActivity();
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
        
        newWords.setOnClickListener(new OnClickListener() {
	            public void onClick(View view) {
		             getActivity().finish();
		             saveRecord();
		             Intent i = new Intent(activity, WordsPage.class);
		             i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		             activity.startActivity(i);
	            }
	    });
        
        nextPlate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
	             getActivity().finish();
	             saveRecord();
	             List<LeafCard> cards = new LeafCardAdapter(getActivity()).getList(SIZE);
                 ActivityReview.setCardsPool(cards);
                 startActivity(new Intent(getActivity(), ActivityReview.class));
            }
    });
        exit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
	             getActivity().finish();
	             saveRecord();
            }
    });

        refresh();

        return myFragmentView;
    }
    
    //when view pager get the second last page, this page, as last page, will be pre-loaded.
    //if place save data back to database into oncreate or other automatically executed function, writing will be run twice
    //encapsulate it into a function, and it would be called for any operation (i.e. click the button)
    private void saveRecord() {
    	Log.d("FragmentPCReview","Save Record");
        for (LeafCard card : ActivityReview.getCardsPool()) {
        	//(int connection_id, int user_id, int type, long last_time, int performance, int user_exclude, Context c)
        	new UserPerformanceDirty(card.getConnectionId(),user_id,0,card.getLastTime(),3,0,getActivity().getApplicationContext()).save();
        }
        
        new Thread(new Runnable(){
        	public void run()
        	{InitDatabase.updateLocalPerformanceSummary(getActivity().getApplicationContext());}
        }).start();
    }

    private void refresh() {
        LogUtil.logDeubg(this, "refresh");
        int totalCards = 0;
        totalCards = ActivityReview.getCardsPool().size();
        
        
        
        vocabSize.setText("329"); //wait for varun figure out how to count how many word the user know
        performance.setText(totalCards + "/" + totalCards);
        skip.setText("");
        birthday.setText("");
        birthdayDetail.setText("");
        evaluation.setText("");
        
        
    }
}
