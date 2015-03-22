package com.openwords.learningmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardTypeEval;
import com.openwords.ui.graphics.AnimationTimerBar;
import com.openwords.util.TimeConvertor;
import com.openwords.util.WordComparsion;
import com.openwords.util.log.LogUtil;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentCardTypeEval extends FragmentLearningModule {

    private final int cardIndex;
    private EditText userInput;
    private TextView answer;
    private TextView question;
    private TextView transcription;
    private ImageView checkButton;
    private ImageView audioPlay;
    private ImageView status;
    private final double CUTOFF = 0.75f;
    private LeafCardTypeEval card;
    private LinearLayout breadcrumbs;
    private View myFragmentView;
    private ScrollView container2;
    private List<LeafCard> cardsPool;
    //private ActivityLM lmActivity;
    private View advanceTimerBar;
    private Animation advanceTimerAnimation;
    private boolean answerChecked;
    private Timer advanceTimer;

    public FragmentCardTypeEval(int cardIndex, List<LeafCard> cardsPool) {
        this.cardIndex = cardIndex;
        this.cardsPool = cardsPool;
        //this.lmActivity = lmActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate for card: " + cardIndex);
        answerChecked = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);

        myFragmentView = inflater.inflate(R.layout.fragment_type_eval, container, false);
        card = (LeafCardTypeEval) cardsPool.get(cardIndex);

        advanceTimerBar = myFragmentView.findViewById(R.id.lm_frag_advance);
        advanceTimerAnimation = new AnimationTimerBar(0, 100, advanceTimerBar);
        advanceTimerAnimation.setDuration(3000);

        answer = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_answer);
        question = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_question);
        transcription = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_transcription);
        userInput = (EditText) myFragmentView.findViewById(R.id.typeEvaluate_EditText_input);
        checkButton = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_checkButton);
        status = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_status);
        audioPlay = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_audioPlay);
        container2 = (ScrollView) myFragmentView.findViewById(R.id.typeEvaluate_ScrollView_Container);
        setInterfaceView();
        //makeBreadCrumbs();
        card.setLastTime(TimeConvertor.getUnixTime());
        answer.setVisibility(View.INVISIBLE);

        updateAudioIcon(audioPlay, card.getWordTwoId());
        //addClarificationTrigger(lmActivity, new View[]{answer, question}, answer, card.getWordTwoId());

        userInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                container2.scrollTo(0, myFragmentView.findViewById(R.id.typeEvaluate_ViewFlipper_frame).getBottom());
            }

            public void afterTextChanged(Editable s) {
                checkUserInputAnswer(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkUserInputAnswer(true);
            }
        });

        return myFragmentView;
    }

    private void checkUserInputAnswer(boolean doFinalCheck) {
        status.setImageResource(R.drawable.ic_learning_module_null);
        answer.setVisibility(View.INVISIBLE);
        int userChoice = 0;
        //user choice 0--null 1--wrong 2--close 3--correct
        String userInputString = userInput.getText().toString().trim();
        if (userInputString.isEmpty()) {
            return;
        }
        String correctString = card.getWordLang1().trim();

        double similarity = WordComparsion.similarity(userInputString, card.getWordLang1());
        if (doFinalCheck) {
            if (similarity >= CUTOFF) {
                status.setImageResource(R.drawable.ic_learning_module_close);
                userChoice = 2;
                card.setUserInput(userInputString);
                answer.setVisibility(View.VISIBLE);
            } else {
                status.setImageResource(R.drawable.ic_learning_module_incorrect);
                userChoice = 1;
                card.setUserInput(userInputString);
                answer.setVisibility(View.VISIBLE);
            }
        }
        if (userInputString.equalsIgnoreCase(correctString)) {
            status.setImageResource(R.drawable.ic_learning_module_correct);
            card.setUserInput(userInputString);
            userChoice = 3;
            answer.setVisibility(View.VISIBLE);
            doFinalCheck = true;
        }
        card.setUserChoice(userChoice);

        if (!doFinalCheck) {
            return;
        }

        if (!answerChecked) {
            final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .create();
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                public void onCancel(DialogInterface di) {
                    advanceTimer.cancel();
                    answerChecked = true;
                    advanceTimerBar.setVisibility(View.INVISIBLE);
                    advanceTimerBar.clearAnimation();
                }
            });
            dialog.show();

            advanceTimerBar.clearAnimation();
            advanceTimerBar.startAnimation(advanceTimerAnimation);
            advanceTimer = new Timer();
            final Handler next = new Handler(new Handler.Callback() {

                public boolean handleMessage(Message msg) {
                    if (msg.what == 0) {
                        dialog.cancel();
                        //lmActivity.goToNextCard();
                    }
                    return true;
                }
            });
            advanceTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    next.sendEmptyMessage(0);
                }
            }, 3000);
        }
    }

    private void setInterfaceView() {
        Integer userChoice = card.getUserChoice();
        transcription.setText(card.getTranscription());
        question.setText(card.getWordLang2());
        answer.setText(card.getWordLang1());
        if (userChoice.equals(0)) {
            status.setImageResource(R.drawable.ic_learning_module_null);
            answer.setVisibility(View.INVISIBLE);
        } else if (userChoice.equals(3)) {
            status.setImageResource(R.drawable.ic_learning_module_correct);
            answer.setVisibility(View.INVISIBLE);
        } else if (userChoice.equals(2)) {
            status.setImageResource(R.drawable.ic_learning_module_close);
            answer.setVisibility(View.VISIBLE);
        } else {
            status.setImageResource(R.drawable.ic_learning_module_incorrect);
            answer.setVisibility(View.VISIBLE);
        }
        userInput.setText("");
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
