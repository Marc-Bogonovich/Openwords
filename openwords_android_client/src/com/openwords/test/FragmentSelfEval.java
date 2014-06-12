package com.openwords.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.Plate;
import com.openwords.model.PlatePerformanceType;
import com.openwords.tts.Speak;
import com.openwords.util.log.LogUtil;

public class FragmentSelfEval extends Fragment {

    private final int cardIndex;
    private TextView problem, transcription, answer;
    private Button showAnswer;
    private ImageView correct, incorrect, showCorrect, showIncorrect, audioPlay;

    public FragmentSelfEval(int cardIndex) {
        this.cardIndex = cardIndex;
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

        View myFragmentView = inflater.inflate(R.layout.fragment_self_eval, container, false);
        final Plate card = ActivitySelfEval.getProblemPool().get(this.cardIndex);

        problem = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_Problem);
        transcription = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_Transcription);
        answer = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_Answer);
        showAnswer = (Button) myFragmentView.findViewById(R.id.selfEvaluate_Button_ShowAnswer);
        correct = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_Correct);
        incorrect = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_Incorrect);
        showCorrect = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_Correct_Show);
        showIncorrect = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_Incorrect_Show);
        audioPlay = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_AudioPlay);

        problem.setText(card.getProblemOne());
        transcription.setText(card.getProblemTwo());
        switch (card.getTestType()) {
            case Test_Type_Review:
                answer.setText(card.getAnswerOne());
                break;
            case Test_Type_Self_Evaluate:
                answer.setText(card.getAnswerTwo());
                break;
        }
        showAnswer.setText("Show Me");

        showAnswer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                switch (card.getTestType()) {
                    case Test_Type_Review:
                        showCorrect.setVisibility(View.VISIBLE);
                        break;
                    case Test_Type_Self_Evaluate:
                        showIncorrect.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                card.setPerformance(PlatePerformanceType.Performance_Correct);
                correct.setImageResource(R.drawable.button_self_evaluate_correct_selected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
                card.save();
            }
        });

        incorrect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                card.setPerformance(PlatePerformanceType.Performance_Incorrect);
                correct.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
                card.save();
            }
        });

        audioPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Speak.getInstance(null).speak(card.getProblemOne());
            }
        });

        switch (card.getPerformance()) {
            case Performance_Correct:
                correct.setImageResource(R.drawable.button_self_evaluate_correct_selected);
                break;
            case Performance_Incorrect:
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
                break;
            case Performance_Null:
                correct.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
                break;
        }

        return myFragmentView;
    }
}
