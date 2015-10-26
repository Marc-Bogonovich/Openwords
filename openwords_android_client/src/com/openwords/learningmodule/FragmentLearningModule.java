package com.openwords.learningmodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.DataPool;
import com.openwords.model.Performance;
import com.openwords.model.Word;
import com.openwords.model.WordAudio;
import com.openwords.sound.SoundPlayer;
import com.openwords.ui.graphics.AnimationTimerBar;
import com.openwords.ui.lily.lm.ViewSoundBackground;
import com.openwords.util.WordComparsion;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;
import java.util.Timer;
import java.util.TimerTask;

public abstract class FragmentLearningModule extends Fragment {

    private final double CUTOFF = 0.75f;
    private View advanceTimerBar;
    private Animation advanceTimerAnimation;
    private boolean timerIsFired;
    private Timer advanceTimer;

    private TextView questionView, answerView;
    private ImageView answerStatusIcon;
    private Performance perf;
    private Word w1;
    private Word w2;
    private ActivityLearning lmActivity;

    private int duration;

    public void updateAudioIcon(ImageView audioPlay, long wordId) {
        final WordAudio audio = WordAudio.getAudio(wordId);
        if (audio == null) {
            audioPlay.setImageResource(R.drawable.ic_self_evaluate_audio_null);
        } else {
            audioPlay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    LogUtil.logDeubg(this, "Play: " + audio.fileName);
                    SoundPlayer.playMusic(LocalFileSystem.getAudioFullPath(audio.fileName), true);
                }
            });
            audioPlay.setSoundEffectsEnabled(false);
        }
    }

    public void updateAudioIcon(final ViewSoundBackground soundView, long wordId) {
        final WordAudio audio = WordAudio.getAudio(wordId);
        if (audio == null) {
            soundView.config(DataPool.Color_Main, 150, false, new View.OnClickListener() {

                public void onClick(View view) {
                }
            });
        } else {
            soundView.config(DataPool.Color_Main, 255, false, new View.OnClickListener() {

                public void onClick(View view) {
                    soundView.touchAnimation();
                    LogUtil.logDeubg(this, "Play: " + audio.fileName);
                    SoundPlayer.playMusic(LocalFileSystem.getAudioFullPath(audio.fileName), true);
                }
            });
        }
    }

    public void addClarificationTrigger(final Activity activity, View[] actions, final View near, final String message) {
        for (View v : actions) {
            v.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    try {
                        showInfo(activity, near, message);
                    } catch (Exception e) {
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void addClarificationTrigger(final Activity activity, View[] actions, final int y, final String message) {
        for (View v : actions) {
            v.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    try {
                        showInfo(activity, y, message);
                    } catch (Exception e) {
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showInfo(Activity activity, int y, String message) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(message)
                .create();
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layout = dialog.getWindow().getAttributes();
        layout.gravity = Gravity.CENTER;
        layout.y = y;
        dialog.show();
    }

    private void showInfo(Activity activity, View near, String message) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(message)
                .create();
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layout = dialog.getWindow().getAttributes();
        layout.gravity = Gravity.CENTER;
        layout.y = near.getMeasuredHeight();

        dialog.show();
    }

    public void setTimer(ActivityLearning lmActivity, View advanceTimerBar, int duration) {
        this.lmActivity = lmActivity;
        this.advanceTimerBar = advanceTimerBar;
        this.duration = duration;
        advanceTimerAnimation = new AnimationTimerBar(0, 100, advanceTimerBar);
        advanceTimerAnimation.setDuration(duration);
    }

    public void formViewElementsForTypingUI(ActivityLearning lmActivity, final ScrollView scrollContainer,
            View advanceTimerBar,
            final EditText userInputView, ImageView checkButton,
            TextView questionView, TextView answerView, ImageView answerStatusIcon,
            Performance perf, Word w1, Word w2) {

        this.lmActivity = lmActivity;
        this.questionView = questionView;
        this.answerView = answerView;
        this.answerStatusIcon = answerStatusIcon;
        this.perf = perf;
        this.w1 = w1;
        this.w2 = w2;
        this.advanceTimerBar = advanceTimerBar;
        advanceTimerAnimation = new AnimationTimerBar(0, 100, advanceTimerBar);
        advanceTimerAnimation.setDuration(3000);

        updateChoiceView();

        userInputView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollContainer.fullScroll(View.FOCUS_DOWN);
            }

            public void afterTextChanged(Editable s) {
                checkUserInputAnswer(false, userInputView.getText().toString().trim());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkUserInputAnswer(true, userInputView.getText().toString().trim());
            }
        });
    }

    public void updateChoiceView() {
        questionView.setText(w2.getMeta().nativeForm);
        answerView.setText(w1.getMeta().nativeForm);
        answerView.setVisibility(View.INVISIBLE);

        if (perf.performance.contains("new")) {
            answerStatusIcon.setImageResource(R.drawable.ic_learning_module_null);
        } else if (perf.performance.contains("good")) {
            answerStatusIcon.setImageResource(R.drawable.ic_learning_module_correct);
        } else if (perf.performance.contains("nearly")) {
            answerStatusIcon.setImageResource(R.drawable.ic_learning_module_close);
        } else {
            answerStatusIcon.setImageResource(R.drawable.ic_learning_module_incorrect);
        }
    }

    private synchronized void checkUserInputAnswer(boolean checkButtonPressed, String userInputString) {
        boolean checkingDone = false;
        answerStatusIcon.setImageResource(R.drawable.ic_learning_module_null);
        answerView.setVisibility(View.INVISIBLE);

        if (userInputString.isEmpty()) {
            return;
        }
        String correctString = w1.word.trim();

        double similarity = WordComparsion.similarity(userInputString, correctString);
        if (checkButtonPressed) {
            if (similarity >= CUTOFF) {
                answerStatusIcon.setImageResource(R.drawable.ic_learning_module_close);
                perf.performance = "nearly";
                perf.save();
                answerView.setVisibility(View.VISIBLE);
            } else {
                answerStatusIcon.setImageResource(R.drawable.ic_learning_module_incorrect);
                perf.performance = "bad";
                perf.save();
                answerView.setVisibility(View.VISIBLE);
            }
        }

        if (userInputString.length() == correctString.length()) {
            if (userInputString.equalsIgnoreCase(correctString)) {
                answerStatusIcon.setImageResource(R.drawable.ic_learning_module_correct);
                perf.performance = "good";
                perf.save();
                answerView.setVisibility(View.VISIBLE);
                checkingDone = true;
            }
        }

        if (checkingDone) {
            fireTimer();
        }
    }

    public synchronized void fireTimer() {
        if (!timerIsFired) {
            timerIsFired = true;
            final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .create();
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                public void onCancel(DialogInterface di) {
                    advanceTimer.cancel();
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
            }, duration);
        }
    }
}
