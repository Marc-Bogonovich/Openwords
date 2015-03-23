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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.util.TimeConvertor;
import com.openwords.util.WordComparsion;
import com.openwords.util.log.LogUtil;
import java.util.List;

public class FragmentCardHearing extends FragmentLearningModule {

    private final int cardIndex;
    private TextView question, transcription, answer;
    private ImageView checkButton, indicator, audioPlayButton;
    private EditText userInput;
    private final double CUTOFF = 0.75f;
    private LinearLayout breadcrumbs;
    private View myFragmentView;
    private ScrollView container2;
    //private ActivityLM lmActivity;

    public FragmentCardHearing(int cardIndex, List cardsPool) {
        this.cardIndex = cardIndex;
        //this.lmActivity = lmActivity;
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

        myFragmentView = inflater.inflate(R.layout.fragment_hearing, container, false);
        //card = (LeafCardHearing) cardsPool.get(cardIndex);

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
        setInterfaceView();
        //makeBreadCrumbs(); according to Marc's requirement
        userInput.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {
                container2.scrollTo(0, myFragmentView.findViewById(R.id.hearing_ViewFlipper_frame).getBottom());
            }

            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                int userChoice;

                if (userInput != null) {
                    indicator.setImageResource(R.drawable.ic_learning_module_null);
                    //user choice 0--null 1--wrong 2--close 3--correct
                    String userInputString = userInput.getText().toString().trim();
                    //String correctString = card.getWordLang1().trim();

                    //double similarity = WordComparsion.similarity(userInputString, card.getWordLang1());
//                    if (userInputString.equalsIgnoreCase(correctString)) {
//                        indicator.setImageResource(R.drawable.ic_learning_module_correct);
//                        card.setUserInput(userInputString);
//                        userChoice = 3;
//                        Handler mHandler = new Handler();
//                        mHandler.postDelayed(new Runnable() {
//                            public void run() {
//                                //lmActivity.goToNextCard();
//                            }
//                        }, 3000);
//                    } else {
//                        answer.setVisibility(View.INVISIBLE);
//                        indicator.setImageResource(R.drawable.ic_learning_module_null);
//                        userChoice = 0;
//                        //answer.setVisibility(View.VISIBLE);
//                    }
//                    card.setUserChoice(userChoice);

                }
            }

            public void afterTextChanged(Editable edtbl) {

            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer userChoice = 0;

                if (userInput != null) {
                    //user choice 0--null 1--wrong 2--close 3--correct
                    String userInputString = userInput.getText().toString().trim();

//                    double similarity = Math.max(WordComparsion.similarity(userInputString, card.getWordLang1()),
//                            WordComparsion.similarity(userInputString, card.getWordLang2()));
//                    if (userInputString.equalsIgnoreCase(card.getWordLang1())
//                            || userInputString.equalsIgnoreCase(card.getWordLang2())) { // if user type lang1 or lang2
//                        indicator.setImageResource(R.drawable.ic_learning_module_correct);
//                        card.setUserInput(userInputString);
//                        userChoice = 3;
//                    } else if (similarity >= CUTOFF) { //user input is close enough
//                        indicator.setImageResource(R.drawable.ic_learning_module_close);
//                        userChoice = 2;
//                        card.setUserInput(userInputString);
//                        //if want the status icon becomes null when move forward/backward, change the value of userChoice
//                    } else { //user input is wrong completely
//                        indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
//                        userChoice = 1;
//                        card.setUserInput(userInputString);
//                    }
                } else { //user input is null
                    indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
                    userChoice = 0;

                }
                question.setVisibility(View.VISIBLE);
                answer.setVisibility(View.VISIBLE);
                transcription.setVisibility(View.VISIBLE);
                //card.setUserChoice(userChoice);

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        //lmActivity.goToNextCard();
                    }
                }, 3000);

            }
        });

        return myFragmentView;
    }

    private void setInterfaceView() {
//        Integer userChoice = card.getUserChoice();
//        transcription.setText(card.getTranscription());
//        question.setText(card.getWordLang2());
//        answer.setText(card.getWordLang1());
//        // 0 -- null 1-- wrong 2-- close 3--correct
//        card.setLastTime(TimeConvertor.getUnixTime());
//        if (userChoice.equals(0)) {
//            indicator.setImageResource(R.drawable.ic_learning_module_null);
//            userInput.setText("");
//            transcription.setVisibility(View.INVISIBLE);
//            answer.setVisibility(View.INVISIBLE);
//            question.setVisibility(View.INVISIBLE);
//        } else if (userChoice.equals(3)) {
//            indicator.setImageResource(R.drawable.ic_learning_module_correct);
//            userInput.setText(card.getUserInput());
//            answer.setVisibility(View.VISIBLE);
//            transcription.setVisibility(View.VISIBLE);
//            question.setVisibility(View.VISIBLE);
//        } else if (userChoice.equals(2)) {
//            indicator.setImageResource(R.drawable.ic_learning_module_close);
//            userInput.setText(card.getUserInput());
//            answer.setVisibility(View.VISIBLE);
//            transcription.setVisibility(View.VISIBLE);
//            question.setVisibility(View.VISIBLE);
//        } else {
//            indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
//            userInput.setText(card.getUserInput());
//            answer.setVisibility(View.VISIBLE);
//            question.setVisibility(View.VISIBLE);
//            transcription.setVisibility(View.VISIBLE);
//        }
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
