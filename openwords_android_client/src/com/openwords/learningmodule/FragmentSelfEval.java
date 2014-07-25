package com.openwords.learningmodule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openwords.R;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.tts.Speak;
import com.openwords.util.TimeConvertor;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;

public class FragmentSelfEval extends Fragment {

    private final int cardIndex;
    private TextView problem, transcription, answer;
    private Button showAnswer;
    private ImageView correct, incorrect, audioPlay;
    private LeafCardSelfEval card;
    private LinearLayout breadcrumbs;
    private View myFragmentView;

    public FragmentSelfEval(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);

        myFragmentView = inflater.inflate(R.layout.fragment_self_eval, container, false);
        card = ActivitySelfEval.getCardsPool().get(this.cardIndex);

        problem = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_question);
        transcription = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_transcription);
        answer = (TextView) myFragmentView.findViewById(R.id.selfEvaluate_TextView_answer);
        showAnswer = (Button) myFragmentView.findViewById(R.id.selfEvaluate_Button_showAnswer);
        correct = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_known);
        incorrect = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_unknown);
        audioPlay = (ImageView) myFragmentView.findViewById(R.id.selfEvaluate_ImageView_audioPlay);
        //makeBreadCrumbs();
        problem.setText(card.getWordLang2());
        answer.setText(card.getWordLang1());
        transcription.setText(card.getTranscription());
        card.setLastTime(TimeConvertor.getUnixTime());

        showAnswer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                answer.setVisibility(View.VISIBLE);
                transcription.setVisibility(View.VISIBLE);
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                card.setUserChoice(Boolean.TRUE);
                correct.setImageResource(R.drawable.button_self_evaluate_correct_selected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
                ActivitySelfEval.getInstance().goToNextCard();
            }
        });

        incorrect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                card.setUserChoice(Boolean.FALSE);
                correct.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
                incorrect.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
                ActivitySelfEval.getInstance().goToNextCard();
            }
        });

        audioPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Speak.getInstance(null).speak(card.getWordLang2());
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
    
//    private void makeBreadCrumbs() {
//    	breadcrumbs = (LinearLayout) myFragmentView.findViewById(R.id.review_LinearLayout_breadcrumbs);
//    	int size = OpenwordsSharedPreferences.getLeafCardSize();
//    	for(int i=0;i<size;i++) {
//    		ImageView crumb = new ImageView(this.getActivity().getApplicationContext());
//    		
//    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//    		            0,
//    		            LinearLayout.LayoutParams.WRAP_CONTENT, 1);
//    		
//    		crumb.setImageResource(R.drawable.ic_learning_module_breadcrumb_normal);
//    		if(i==cardIndex) crumb.setImageResource(R.drawable.ic_learning_module_breadcrumb_large);
//    		breadcrumbs.addView(crumb, i , params);
//    	}
//    }
}
