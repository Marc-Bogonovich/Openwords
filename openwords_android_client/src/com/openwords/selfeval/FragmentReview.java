package com.openwords.selfeval;

import android.annotation.SuppressLint;
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

@SuppressLint("ValidFragment")
public class FragmentReview extends Fragment {

    private final int cardIndex;
    private TextView problem, transcription, answer;
    private ImageView audioPlay;

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
        final LeafCardSelfEval card = ActivityReview.getCardsPool().get(this.cardIndex);

        problem = (TextView) myFragmentView.findViewById(R.id.review_TextView_Question);
        transcription = (TextView) myFragmentView.findViewById(R.id.review_TextView_Transcription);
        answer = (TextView) myFragmentView.findViewById(R.id.review_TextView_Answer);
     
        audioPlay = (ImageView) myFragmentView.findViewById(R.id.review_ImageView_AudioPlay);

        problem.setText(card.getWordLang2());
        answer.setText(card.getWordLang1());
        transcription.setText(card.getTranscription());



        audioPlay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Speak.getInstance(null).speak(card.getWordLang2());
            }
        });

        return myFragmentView;
    }
}
