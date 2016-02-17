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

public class FragmentCardTypeEval extends FragmentLearningModule {

    private final int cardIndex;
    private EditText userInput;
    private TextView answer, question, transcription;
    private ImageView checkButton, audioPlay, status;
    private View myFragmentView;
    private ScrollView scrollContainer;
    private ActivityLearning lmActivity;

    public FragmentCardTypeEval(int cardIndex, ActivityLearning lmActivity) {
        this.cardIndex = cardIndex;
        this.lmActivity = lmActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);

        myFragmentView = inflater.inflate(R.layout.fragment_type_eval, container, false);
        WordConnection wc = DataPool.getWordConnection(cardIndex);
        final Word w1 = Word.getWord(wc.wordOneId);
        final Word w2 = Word.getWord(wc.wordTwoId);
        final Performance perf = DataPool.getPerformance(wc.connectionId);
        if (perf == null) {
            MyQuickToast.showShort(getActivity(), "No performance data: " + wc.connectionId);
            return null;
        }

        answer = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_answer);
        question = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_question);
        transcription = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_transcription);
        userInput = (EditText) myFragmentView.findViewById(R.id.typeEvaluate_EditText_input);
        checkButton = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_checkButton);
        status = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_status);
        audioPlay = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_audioPlay);
        scrollContainer = (ScrollView) myFragmentView.findViewById(R.id.typeEvaluate_ScrollView_Container);

        updateAudioIcon(audioPlay, w2.wordId);
        addClarificationTrigger(lmActivity, new View[]{answer, question}, answer, w1.getMeta().commonTranslation);
        formViewElementsForTypingUI(lmActivity, scrollContainer, myFragmentView.findViewById(R.id.lm_frag_advance),
                userInput, checkButton, question, answer, status, perf, w1, w2);

        return myFragmentView;
    }

}
