package com.openwords.learningmodule;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.openwords.util.WordComparsion;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;

public class FragmentCardHearing extends FragmentLearningModule {

    private final int cardIndex;
    private TextView question, transcription, answer;
    private ImageView checkButton, indicator, audioPlayButton;
    private EditText userInput;
    private final double CUTOFF = 0.75f;
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
        setChoiceView(perf, w2);

        question.setText(w2.getMeta().nativeForm);
        answer.setText(w1.getMeta().nativeForm);

        userInput.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {
                container2.scrollTo(0, myFragmentView.findViewById(R.id.hearing_ViewFlipper_frame).getBottom());
            }

            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                checkUserInput(false, perf, w1, w2);
            }

            public void afterTextChanged(Editable edtbl) {
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInput(true, perf, w1, w2);
                question.setVisibility(View.VISIBLE);
                answer.setVisibility(View.VISIBLE);
                transcription.setVisibility(View.VISIBLE);

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        lmActivity.goToNextCard();
                    }
                }, 3000);

            }
        });

        return myFragmentView;
    }

    private void checkUserInput(boolean checkButtonPressed, Performance perf, Word w1, Word w2) {
        if (checkButtonPressed) {
            if (userInput != null) {
                String userInputString = userInput.getText().toString().trim();

                double similarity = Math.max(WordComparsion.similarity(userInputString, w1.word),
                        WordComparsion.similarity(userInputString, w2.word));
                if (userInputString.equalsIgnoreCase(w1.word)
                        || userInputString.equalsIgnoreCase(w2.word)) { // if user type lang1 or lang2
                    indicator.setImageResource(R.drawable.ic_learning_module_correct);
                    perf.performance = "good";
                    perf.tempVersion = perf.version + 1;
                    perf.save();
                } else if (similarity >= CUTOFF) { //user input is close enough
                    indicator.setImageResource(R.drawable.ic_learning_module_close);
                    perf.performance = "nearly";
                    perf.tempVersion = perf.version + 1;
                    perf.save();
                } else {
                    indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
                    perf.performance = "bad";
                    perf.tempVersion = perf.version + 1;
                    perf.save();
                }
            } else {
                indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
                perf.performance = "new";
                perf.tempVersion = perf.version + 1;
                perf.save();
            }
        } else {
            if (userInput != null) {
                indicator.setImageResource(R.drawable.ic_learning_module_null);
                String userInputString = userInput.getText().toString().trim();
                String correctString = w1.word.trim();

                if (userInputString.length() == correctString.length()) {
                    if (userInputString.equalsIgnoreCase(correctString)) {
                        indicator.setImageResource(R.drawable.ic_learning_module_correct);
                        perf.performance = "good";
                        perf.tempVersion = perf.version + 1;
                        perf.save();

                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                lmActivity.goToNextCard();
                            }
                        }, 3000);
                    } else {
                        answer.setVisibility(View.INVISIBLE);
                        indicator.setImageResource(R.drawable.ic_learning_module_null);
                    }
                }
            }
        }
    }

    private void setChoiceView(Performance perf, Word w2) {
        if (perf.performance.equals("new")) {
            indicator.setImageResource(R.drawable.ic_learning_module_null);
            userInput.setText("");
            transcription.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.INVISIBLE);
            question.setVisibility(View.INVISIBLE);
        } else if (perf.performance.equals("good")) {
            indicator.setImageResource(R.drawable.ic_learning_module_correct);
            userInput.setText(w2.word);
            answer.setVisibility(View.VISIBLE);
            transcription.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);
        } else if (perf.performance.equals("nearly")) {
            indicator.setImageResource(R.drawable.ic_learning_module_close);
            userInput.setText("");
            answer.setVisibility(View.VISIBLE);
            transcription.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);
        } else {
            indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
            userInput.setText("");
            answer.setVisibility(View.VISIBLE);
            question.setVisibility(View.VISIBLE);
            transcription.setVisibility(View.VISIBLE);
        }
    }
}
