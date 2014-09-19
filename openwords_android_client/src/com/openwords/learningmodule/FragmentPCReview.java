package com.openwords.learningmodule;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.InitDatabase;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardReviewAdapter;
import com.openwords.model.UserPerformanceDirty;
import com.openwords.ui.main.HomePage;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import static com.openwords.util.preference.OpenwordsSharedPreferences.REVIEW_PROGRESS;
import com.openwords.view.actionbar.WordsPage;
import java.util.List;

public class FragmentPCReview extends Fragment {

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
    private Button newWords, nextPlate, exit;
    private List<LeafCard> cardsPool;

    public FragmentPCReview(List<LeafCard> cardsPool) {
        this.cardsPool = cardsPool;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        OpenwordsSharedPreferences.setLMProgress(REVIEW_PROGRESS, null);
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
                activity.finish();
                saveRecord();
                Intent i = new Intent(activity, WordsPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(i);
                HomePage.instance.finish();
            }
        });

        nextPlate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                activity.finish();
                saveRecord();
                final List<LeafCard> cards = new LeafCardReviewAdapter().getList(SIZE);
                ActivityInstantiationCallbackBundle.setBundle(LearningModuleType.LM_Review,
                        R.layout.activity_rev,
                        R.id.act_review_pager,
                        new FragmentMaker() {

                            public Fragment makePageFragment(int index) {
                                return new FragmentReview(index, cards, getActivityInstance());
                            }

                            public Fragment makePCFragment() {
                                return new FragmentPCReview(cards);
                            }
                        },
                        false,
                        cards,
                        0,
                        true,
                        activity,
                        new RefreshPCCallback() {

                            public void refresh() {
                                FragmentPCReview.refreshDetails();
                            }
                        });
                startActivity(new Intent(activity, ActivityLM.class));
            }
        });
        exit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                activity.finish();
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
        Log.d("FragmentPCReview", "Save Record");
        for (LeafCard card : cardsPool) {
            //(int connection_id, int user_id, int type, long last_time, int performance,int l1Id,int l2Id int user_exclude, Context c)
            new UserPerformanceDirty(card.getConnectionId(), user_id, 0, card.getLastTime(), 3, 1,
                    OpenwordsSharedPreferences.getUserInfo().getLang_id(), 0, getActivity().getApplicationContext()).save();
        }

        InitDatabase.updateLocalPerformanceSummary(getActivity().getApplicationContext());
        //set current card index to 0. Why here? I don't know. Maybe the function above has some side-effect 
        //ActivityReview.setCurrentCard(0);
    }

    private void refresh() {
        LogUtil.logDeubg(this, "refresh");
        int totalCards = 0;
        totalCards = cardsPool.size();

        vocabSize.setText("329"); //wait for varun figure out how to count how many word the user know
        performance.setText(totalCards + "/" + totalCards);
        skip.setText("");
        birthday.setText("");
        birthdayDetail.setText("");
        evaluation.setText("");

    }
}
