package com.openwords.learningmodule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.LeafCard;
import com.openwords.util.TimeConvertor;
import com.openwords.util.log.LogUtil;
import java.util.List;

public class FragmentReview extends FragmentLearningModule {

    private final int cardIndex;
    private TextView problem, transcription, answer;
    private ImageView audioPlay;
    private LeafCard card;
    private LinearLayout breadcrumbs;
    private View myFragmentView;
    private List<LeafCard> cardsPool;
    private ActivityLM lmActivity;

    public FragmentReview(int cardIndex, List<LeafCard> cardsPool, ActivityLM lmActivity) {
        this.cardIndex = cardIndex;
        this.cardsPool = cardsPool;
        this.lmActivity = lmActivity;
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

        myFragmentView = inflater.inflate(R.layout.fragment_review, container, false);
        card = cardsPool.get(cardIndex);

        problem = (TextView) myFragmentView.findViewById(R.id.review_TextView_question);
        transcription = (TextView) myFragmentView.findViewById(R.id.review_TextView_transcription);
        answer = (TextView) myFragmentView.findViewById(R.id.review_TextView_answer);

        audioPlay = (ImageView) myFragmentView.findViewById(R.id.review_ImageView_audioPlay);
        //makeBreadCrumbs(); //According to Marc's requirement
        problem.setText(card.getWordLang2());
        answer.setText(card.getWordLang1());
        transcription.setText(card.getTranscription());
        card.setLastTime(TimeConvertor.getUnixTime());

        updateAudioIcon(audioPlay, card.getWordTwoId());

        myFragmentView.findViewById(R.id.review_View_actionBarBlank).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lmActivity.goToNextCard();
            }
        });
        myFragmentView.findViewById(R.id.review_LinearLayout_content).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lmActivity.goToNextCard();
            }
        });
        myFragmentView.findViewById(R.id.review_View_actionBarBlank).setSoundEffectsEnabled(false);
        myFragmentView.findViewById(R.id.review_LinearLayout_content).setSoundEffectsEnabled(false);

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
