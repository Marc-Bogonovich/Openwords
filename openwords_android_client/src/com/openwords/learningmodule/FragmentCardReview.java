package com.openwords.learningmodule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.util.log.LogUtil;

public class FragmentCardReview extends FragmentLearningModule {

    private final int cardIndex;
    private TextView problem, transcription, answer;
    private ImageView audioPlay;
    private LinearLayout breadcrumbs;
    private View myFragmentView;
    private ActivityLearning lmActivity;

    public FragmentCardReview(int cardIndex, ActivityLearning lmActivity) {
        this.cardIndex = cardIndex;
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
        WordConnection wc = DataPool.getWordConnection(cardIndex);
        Word w1 = Word.getWord(wc.wordOneId);
        Word w2 = Word.getWord(wc.wordTwoId);

        problem = (TextView) myFragmentView.findViewById(R.id.review_TextView_question);
        transcription = (TextView) myFragmentView.findViewById(R.id.review_TextView_transcription);
        answer = (TextView) myFragmentView.findViewById(R.id.review_TextView_answer);

        audioPlay = (ImageView) myFragmentView.findViewById(R.id.review_ImageView_audioPlay);
        //makeBreadCrumbs(); //According to Marc's requirement
        problem.setText(w1.word);
        answer.setText(w2.word);
        //transcription.setText("test transcription");

        //updateAudioIcon(audioPlay, card.getWordTwoId());
        addClarificationTrigger(lmActivity, new View[]{answer, problem}, answer, w1.getMeta().commonTranslation);
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
