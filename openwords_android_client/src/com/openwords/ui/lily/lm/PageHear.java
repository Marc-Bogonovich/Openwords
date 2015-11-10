package com.openwords.ui.lily.lm;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.FragmentLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.Performance;
import com.openwords.model.SetItem;
import com.openwords.model.WordAudio;
import com.openwords.sound.SoundPlayer;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import java.util.Random;

public class PageHear extends FragmentLearningModule {

    public static boolean FirstSoundDone;
    public final static Random rand = new Random();

    private final int cardIndex;
    private final ActivityLearning lmActivity;
    private View myFragmentView;
    private ViewSoundBackground soundButton;
    private AutoResizeTextView tran, problem, answer1, answer2;
    private ImageView buttonOption;
    private SetItem item;
    private Performance perf;
    private boolean answerIsLeft;
    private boolean answerIsSelected;

    public PageHear(int cardIndex, ActivityLearning lmActivity) {
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
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);
        myFragmentView = inflater.inflate(R.layout.lily_page_lm_hear, container, false);

        item = DataPool.currentSetItems.get(cardIndex);
        perf = DataPool.currentPerformance.get(cardIndex);
        if (perf == null) {
            MyQuickToast.showShort(lmActivity, "No performance data");
            return null;
        }

        buttonOption = (ImageView) myFragmentView.findViewById(R.id.page_hear_image1);
        buttonOption.setColorFilter(DataPool.Color_Main, PorterDuff.Mode.MULTIPLY);
        updateOptionButton(buttonOption, lmActivity);

        soundButton = (ViewSoundBackground) myFragmentView.findViewById(R.id.lily_button_sound_bg);
        updateAudioIcon(soundButton, item.wordTwoId);

        tran = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_hear_text_tran);
        if (item.twoTranscription == null) {
            item.twoTranscription = "";
        }
        tran.setText(item.twoTranscription);

        problem = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_hear_text_problem);
        problem.setText(item.wordTwo);
        problem.setVisibility(View.INVISIBLE);

        answerIsSelected = false;
        answer1 = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_hear_text_answer1);
        answer2 = (AutoResizeTextView) myFragmentView.findViewById(R.id.page_hear_text_answer2);

        addClarificationTrigger(lmActivity, new View[]{problem}, 50, item.wordOneCommon);
        setTimer(lmActivity, myFragmentView.findViewById(R.id.lm_frag_advance), 2000);

        answer1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (answerIsSelected) {
                    return;
                }
                answerIsSelected = true;
                answer1.setTextColor(DataPool.Color_Main);
                answer2.setTextColor(Color.parseColor("#cccccc"));
                if (answerIsLeft) {
                    perf.performance = "good";
                } else {
                    perf.performance = "bad";
                }
                problem.setVisibility(View.VISIBLE);
                fireTimer();
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (answerIsSelected) {
                    return;
                }
                answerIsSelected = true;
                answer2.setTextColor(DataPool.Color_Main);
                answer1.setTextColor(Color.parseColor("#cccccc"));
                if (answerIsLeft) {
                    perf.performance = "bad";
                } else {
                    perf.performance = "good";
                }
                problem.setVisibility(View.VISIBLE);
                fireTimer();
            }
        });
        shuffleAnswers();

        if (cardIndex == 0 && !FirstSoundDone) {
            playAudio();
            FirstSoundDone = true;
        }
        return myFragmentView;
    }

    private void shuffleAnswers() {
        int max = 1;
        int min = 0;

        int r = rand.nextInt(max - min + 1) + min;
        answerIsLeft = r == 0;

        max = DataPool.currentSetItems.size() - 1;
        int index = rand.nextInt(max - min + 1) + min;
        if (index == cardIndex) {
            if (index < max) {
                index += 1;
            } else {
                index = 0;
            }
        }

        if (answerIsLeft) {
            answer1.setText(item.wordTwo + "\n" + item.wordOne);
            answer2.setText(DataPool.currentSetItems.get(index).wordTwo
                    + "\n" + DataPool.currentSetItems.get(index).wordOne);
        } else {
            answer1.setText(DataPool.currentSetItems.get(index).wordTwo
                    + "\n" + DataPool.currentSetItems.get(index).wordOne);
            answer2.setText(item.wordTwo + "\n" + item.wordOne);
        }
    }

    public synchronized void playAudio() {
        if (item.wordTwoId > 0) {
            WordAudio audio = WordAudio.getAudio(item.wordTwoId);
            if (audio == null) {
                return;
            }
            soundButton.touchAnimation();
            LogUtil.logDeubg(this, "Play: " + audio.fileName);
            SoundPlayer.playMusic(LocalFileSystem.getAudioFullPath(audio.fileName), true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.logDeubg(this, "onDestroy for card: " + cardIndex);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.logDeubg(this, "onDetach for card: " + cardIndex);
    }
}
