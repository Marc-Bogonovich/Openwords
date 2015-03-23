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
import android.widget.ScrollView;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Performance;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.ui.graphics.AnimationTimerBar;
import com.openwords.util.WordComparsion;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentCardTypeEval extends FragmentLearningModule {
    
    private final int cardIndex;
    private EditText userInput;
    private TextView answer, question, transcription;
    private ImageView checkButton, audioPlay, status;
    private final double CUTOFF = 0.75f;
    private View myFragmentView;
    private ScrollView container2;
    private ActivityLearning lmActivity;
    private View advanceTimerBar;
    private Animation advanceTimerAnimation;
    private boolean answerChecked;
    private Timer advanceTimer;
    
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
        setChoiceView(transcription, question, answer, status, userInput, perf, w1, w2);

        //updateAudioIcon(audioPlay, card.getWordTwoId());
        //addClarificationTrigger(lmActivity, new View[]{answer, question}, answer, card.getWordTwoId());
        userInput.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                container2.scrollTo(0, myFragmentView.findViewById(R.id.typeEvaluate_ViewFlipper_frame).getBottom());
            }
            
            public void afterTextChanged(Editable s) {
                checkUserInputAnswer(false, perf, w1, w2);
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        
        checkButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                checkUserInputAnswer(true, perf, w1, w2);
            }
        });
        
        return myFragmentView;
    }
    
    private void checkUserInputAnswer(boolean checkButtonPressed, Performance perf, Word w1, Word w2) {
        boolean checkingDone = false;
        status.setImageResource(R.drawable.ic_learning_module_null);
        answer.setVisibility(View.INVISIBLE);
        
        String userInputString = userInput.getText().toString().trim();
        if (userInputString.isEmpty()) {
            return;
        }
        String correctString = w1.word.trim();
        
        double similarity = WordComparsion.similarity(userInputString, correctString);
        if (checkButtonPressed) {
            if (similarity >= CUTOFF) {
                status.setImageResource(R.drawable.ic_learning_module_close);
                perf.performance = "nearly";
                perf.tempVersion = perf.version + 1;
                perf.save();
                answer.setVisibility(View.VISIBLE);
            } else {
                status.setImageResource(R.drawable.ic_learning_module_incorrect);
                perf.performance = "bad";
                perf.tempVersion = perf.version + 1;
                perf.save();
                answer.setVisibility(View.VISIBLE);
            }
        }
        
        if (userInputString.length() == correctString.length()) {
            if (userInputString.equalsIgnoreCase(correctString)) {
                status.setImageResource(R.drawable.ic_learning_module_correct);
                perf.performance = "good";
                perf.tempVersion = perf.version + 1;
                perf.save();
                answer.setVisibility(View.VISIBLE);
                checkingDone = true;
            }
        }
        
        if (!checkingDone) {
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
                        lmActivity.goToNextCard();
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
    
    private void setChoiceView(TextView transriptionView, TextView questionView, TextView answerView,
            ImageView answerStatus, EditText userInput,
            Performance perf, Word w1, Word w2) {
        transriptionView.setText("");
        questionView.setText(w2.getMeta().nativeForm);
        answerView.setText(w1.getMeta().nativeForm);
        
        if (perf.performance.equals("new")) {
            answerStatus.setImageResource(R.drawable.ic_learning_module_null);
            answerView.setVisibility(View.INVISIBLE);
        } else if (perf.performance.equals("good")) {
            answerStatus.setImageResource(R.drawable.ic_learning_module_correct);
            answerView.setVisibility(View.VISIBLE);
        } else if (perf.performance.equals("nearly")) {
            answerStatus.setImageResource(R.drawable.ic_learning_module_close);
            answerView.setVisibility(View.VISIBLE);
        } else {
            answerStatus.setImageResource(R.drawable.ic_learning_module_incorrect);
            answerView.setVisibility(View.VISIBLE);
        }
        userInput.setText("");
    }
}
