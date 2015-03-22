package com.openwords.learningmodule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Performance;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;

public class FragmentCardSelfEval extends FragmentLearningModule {

    private final int cardIndex;
    private TextView problem, transcription, answer;
    private Button showAnswer;
    private ImageView correct, incorrect, audioPlay;
    private View myFragmentView;
    private ActivityLearning lmActivity;

    public FragmentCardSelfEval(int cardIndex, ActivityLearning lmActivity) {
        this.cardIndex = cardIndex;
        this.lmActivity = lmActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);

        myFragmentView = inflater.inflate(R.layout.fragment_self_eval, container, false);
        WordConnection wc = DataPool.getWordConnection(cardIndex);
        Word w1 = Word.getWord(wc.wordOneId);
        Word w2 = Word.getWord(wc.wordTwoId);
        final Performance perf = DataPool.getPerformance(wc.connectionId);
        if (perf == null) {
            MyQuickToast.showShort(getActivity(), "No performance data: " + wc.connectionId);
            return null;
        }

        problem = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_question);
        transcription = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_transcription);
        answer = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_answer);
        showAnswer = (Button) myFragmentView.findViewById(R.id.selfEvaluate_Button_showAnswer);
        correct = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_known);
        incorrect = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_unknown);
        audioPlay = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_audioPlay);

        problem.setText(w2.wordMetaInfo.nativeForm);
        answer.setText(w1.wordMetaInfo.nativeForm);

        //updateAudioIcon(audioPlay, card.getWordTwoId());
        addClarificationTrigger(lmActivity, new View[]{answer, problem}, answer, w1.getMeta().commonTranslation);
        showAnswer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                answer.setVisibility(View.VISIBLE);
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                perf.performance = "good";
                perf.tempVersion = perf.version + 1;
                perf.save();
                correct.setImageResource(R.drawable.button_self_evaluate_correct_selected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
                lmActivity.goToNextCard();
            }
        });

        incorrect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                perf.performance = "bad";
                perf.tempVersion = perf.version + 1;
                perf.save();
                correct.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
                lmActivity.goToNextCard();
            }
        });

        if (perf.performance.contains("new")) {
            correct.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
            incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
        } else {
            if (perf.performance.contains("good")) {
                correct.setImageResource(R.drawable.button_self_evaluate_correct_selected);
            } else if (perf.performance.contains("bad")) {
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
            }
        }
        return myFragmentView;
    }
}
