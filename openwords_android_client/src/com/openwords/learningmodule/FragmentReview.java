package com.openwords.learningmodule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.openwords.R;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.tts.Speak;
import com.openwords.util.log.LogUtil;

@SuppressLint("ValidFragment")
public class FragmentReview extends Fragment {

    private final int cardIndex;
    private TextView problem, transcription, answer;
    private ImageView audioPlay;
    private LeafCard card;
    public FragmentReview(int cardIndex) {
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

        View myFragmentView = inflater.inflate(R.layout.fragment_review, container, false);
        Log.e("size",Integer.toString(ActivityReview.getCardsPool().size()));
        card = ActivityReview.getCardsPool().get(this.cardIndex);

        problem = (TextView) myFragmentView.findViewById(R.id.review_TextView_question);
        transcription = (TextView) myFragmentView.findViewById(R.id.review_TextView_transcription);
        answer = (TextView) myFragmentView.findViewById(R.id.review_TextView_answer);
     
        audioPlay = (ImageView) myFragmentView.findViewById(R.id.review_ImageView_audioPlay);

        problem.setText(card.getWordLang2());
        answer.setText(card.getWordLang1());
        transcription.setText(card.getTranscription());



        audioPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Speak.getInstance(null).speak(card.getWordLang2());
            }
        });
        
        myFragmentView.findViewById(R.id.review_View_actionBarBlank).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ActivityReview.getInstance().getPager().setCurrentItem(cardIndex + 1, true);
            }
        });
        myFragmentView.findViewById(R.id.review_LinearLayout_content).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ActivityReview.getInstance().getPager().setCurrentItem(cardIndex + 1, true);
            }
        });

        return myFragmentView;
    }
}