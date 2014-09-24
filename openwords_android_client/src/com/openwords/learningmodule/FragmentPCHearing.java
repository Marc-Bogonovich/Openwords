package com.openwords.learningmodule;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.InitDatabase;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardHearing;
import com.openwords.model.LeafCardHearingAdapter;
import com.openwords.model.UserPerformanceDirty;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import static com.openwords.util.preference.OpenwordsSharedPreferences.HEARING_PROGRESS;
import java.util.List;

public class FragmentPCHearing extends Fragment {

    private static Handler RefreshHandler;

    public static void refreshDetails() {
        if (RefreshHandler != null) {
            RefreshHandler.sendEmptyMessage(0);
        }
    }
    private Activity activity;
    private int user_id;
    private int SIZE = 10;

    private TextView vocabSize, performance, skip, birthday, birthdayDetail, evaluation;
    private Button nextPlate, exit;
    private List<LeafCard> cardsPool;

    public FragmentPCHearing(List<LeafCard> cardsPool) {
        this.cardsPool = cardsPool;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        OpenwordsSharedPreferences.setLMProgress(HEARING_PROGRESS, null);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        //hide the keyboard
        super.onActivityCreated(savedInstanceState);
        this.getActivity();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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

        /*
         newWords.setOnClickListener(new OnClickListener() {
         public void onClick(View view) {
         getActivity().finish();
         saveRecord();
         Intent i = new Intent(activity, WordsPage.class);
         i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
         activity.startActivity(i);
         HomePage.instance.finish();
         }
         });
         */
        nextPlate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                getActivity().finish();
                saveRecord();
                final List<LeafCard> cards = new LeafCardHearingAdapter().getList(SIZE);
                ActivityInstantiationCallbackBundle.setBundle(LearningModuleType.LM_HearingEvaluation,
                        R.layout.activity_hear,
                        R.id.act_hearing_pager,
                        false,
                        cards,
                        0,
                        true,
                        activity);
                startActivity(new Intent(activity, ActivityLM.class));
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
        Log.d("FragmentPCHearing", "Save Record");
        for (LeafCard c : cardsPool) {
            LeafCardHearing card = (LeafCardHearing) c;
            new UserPerformanceDirty(card.getConnectionId(), user_id, 3, card.getLastTime(), card.getUserChoice(), 1,
                    OpenwordsSharedPreferences.getUserInfo().getLang_id(), 0, getActivity().getApplicationContext()).save();
        }

        new Thread(new Runnable() {
            public void run() {
                InitDatabase.updateLocalPerformanceSummary(getActivity().getApplicationContext());
            }
        }).start();
        //set current card index to 0. Why here? I don't know. Maybe the function above has some side-effect 
        //ActivityHearing.setCurrentCard(0);
    }

    //calculate the total number of correctness
    private void refresh() {
        LogUtil.logDeubg(this, "refresh");
        int totalCards, totalCorrect = 0, totalSkipped = 0;
        totalCards = cardsPool.size();

        for (LeafCard c : cardsPool) {
            LeafCardHearing card = (LeafCardHearing) c;
            //type -- module index : review -- 0, self -- 1, type -- 2, hearing -- 3
            //performance : 0 -- null, 1 -- wrong, 2 -- close, 3 -- right
            if (card.getUserChoice() == 0) {
                totalSkipped++;
            } else {
                if (card.getUserChoice() == 3) {
                    totalCorrect++;
                }
            }
        }

        vocabSize.setText("0 + " + totalCorrect);
        performance.setText(totalCorrect + "/" + totalCards);
        skip.setText(totalSkipped + " Skipped");
        birthday.setText("___ Birthday");
        birthdayDetail.setText("");
        evaluation.setText("");
    }
}
