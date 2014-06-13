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
import com.openwords.model.LeafCardSelfEval;
import com.openwords.tts.Speak;
import com.openwords.util.log.LogUtil;

public class FragmentSelfEval extends Fragment {

    private final int cardIndex;
    private TextView problem, transcription, answer;
    private Button showAnswer;
    private ImageView correct, incorrect, audioPlay;

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
        final LeafCardSelfEval card = ActivitySelfEval.getCardsPool().get(this.cardIndex);

        problem = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_Problem);
        transcription = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_Transcription);
        answer = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_Answer);
        showAnswer = (Button) myFragmentView.findViewById(R.id.selfEvaluate_Button_ShowAnswer);
        correct = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_Correct);
        incorrect = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_Incorrect);
        audioPlay = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_AudioPlay);

        problem.setText(card.getWordLang2());
        answer.setText(card.getWordLang1());
        transcription.setText(card.getTranscription());
        showAnswer.setText("Show Me");

        showAnswer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                answer.setVisibility(View.VISIBLE);
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                card.setUserChoice(Boolean.TRUE);
                correct.setImageResource(R.drawable.button_self_evaluate_correct_selected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
                ActivitySelfEval.getInstance().getPager().setCurrentItem(cardIndex + 1, true);
            }
        });

        incorrect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                card.setUserChoice(Boolean.FALSE);
                correct.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
                ActivitySelfEval.getInstance().getPager().setCurrentItem(cardIndex + 1, true);
            }
        });

        audioPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Speak.getInstance(null).speak(card.getWordLang2());
            }
        });

        if (card.getUserChoice() == null) {
            correct.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
            incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
        } else {
            if (card.getUserChoice() == true) {
                correct.setImageResource(R.drawable.button_self_evaluate_correct_selected);
            } else if (card.getUserChoice() == false) {
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
            }
        }
        return myFragmentView;
    }
}
