package com.openwords.learningmodule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Performance;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;

public class FragmentCardHearing extends FragmentLearningModule {

    private final int cardIndex;
    private TextView question, transcription, answer;
    private ImageView checkButton, indicator, audioPlayButton;
    private EditText userInput;
    private View myFragmentView;
    private ScrollView container2;
    private ActivityLearning lmActivity;

    public FragmentCardHearing(int cardIndex, ActivityLearning lmActivity) {
        this.cardIndex = cardIndex;
        this.lmActivity = lmActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);

        myFragmentView = inflater.inflate(R.layout.fragment_hearing, container, false);
        WordConnection wc = DataPool.getWordConnection(cardIndex);
        final Word w1 = Word.getWord(wc.wordOneId);
        final Word w2 = Word.getWord(wc.wordTwoId);
        final Performance perf = DataPool.getPerformance(wc.connectionId);
        if (perf == null) {
            MyQuickToast.showShort(getActivity(), "No performance data: " + wc.connectionId);
            return null;
        }

        answer = (TextView) myFragmentView.findViewById(R.id.hearing_TextView_answer);
        question = (TextView) myFragmentView.findViewById(R.id.hearing_TextView_question);
        transcription = (TextView) myFragmentView.findViewById(R.id.hearing_TextView_transcription);
        userInput = (EditText) myFragmentView.findViewById(R.id.hearing_EditText_input);
        checkButton = (ImageView) myFragmentView.findViewById(R.id.hearing_ImageView_checkButton);
        indicator = (ImageView) myFragmentView.findViewById(R.id.hearing_ImageView_indicator);
        audioPlayButton = (ImageView) myFragmentView.findViewById(R.id.hearing_ImageView_audioPlay);
        container2 = (ScrollView) myFragmentView.findViewById(R.id.hearingEvaluate_ScrollView_Container);

        //updateAudioIcon(audioPlayButton, card.getWordTwoId());
        //addClarificationTrigger(lmActivity, new View[]{indicator}, answer, card.getWordTwoId());
        formViewElementsForTypingUI(lmActivity, container2, myFragmentView.findViewById(R.id.lm_frag_advance),
                userInput, checkButton, question, answer, indicator, perf, w1, w2);

        return myFragmentView;
    }

}
