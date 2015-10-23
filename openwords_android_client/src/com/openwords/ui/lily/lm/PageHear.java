package com.openwords.ui.lily.lm;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.FragmentLearningModule;
import com.openwords.model.DataPool;
import com.openwords.model.Performance;
import com.openwords.model.SetItem;
import com.openwords.model.WordAudio;
import com.openwords.sound.SoundPlayer;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.util.file.LocalFileSystem;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import java.util.Random;

public class PageHear extends FragmentLearningModule {

    public static boolean FirstSoundDone = false;
    public final static Random rand = new Random();

    private final int cardIndex;
    private final ActivityLearning lmActivity;
    private View myFragmentView;
    private ViewSoundBackground soundButton;
    private MyMaxTextView tran, problem, answer, answer2;
    private ImageView buttonOption;
    private SetItem item;
    private Performance perf;
    private DialogForSettingSelection settingDialog;
    private boolean answerIsLeft;
    private MyMaxTextView.TextIsOutCallback outCall;

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
        super.onCreate(savedInstanceState);
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
        buttonOption.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (settingDialog != null) {
                    settingDialog.cancel();
                }
                settingDialog = new DialogForSettingSelection(lmActivity)
                        .addItem("Comment")
                        .addItem("Stop")
                        .build(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0:
                                        MyQuickToast.showShort(lmActivity, "Comment is not supported yet.");
                                        break;
                                    case 1:
                                        lmActivity.finish();
                                        break;
                                }
                                settingDialog.cancel();
                                settingDialog = null;
                            }
                        }, (int) buttonOption.getX(), (int) buttonOption.getY());
                settingDialog.show();
            }
        });

        soundButton = (ViewSoundBackground) myFragmentView.findViewById(R.id.lily_button_sound_bg);
        updateAudioIcon(soundButton, item.wordTwoId);

        outCall = new MyMaxTextView.TextIsOutCallback() {

            public void tell() {
                someTextOut();
            }
        };

        tran = (MyMaxTextView) myFragmentView.findViewById(R.id.page_hear_text_tran);
        if (item.twoTranscription == null) {
            item.twoTranscription = "";
        }
        tran.config(DataPool.Color_Main, 255, item.twoTranscription, outCall);

        problem = (MyMaxTextView) myFragmentView.findViewById(R.id.page_hear_text_problem);
        problem.config(DataPool.Color_Main, 255, item.wordOne, outCall);
        problem.setVisibility(View.INVISIBLE);

        answer = (MyMaxTextView) myFragmentView.findViewById(R.id.page_hear_text_answer);
        answer2 = (MyMaxTextView) myFragmentView.findViewById(R.id.page_hear_text_answer2);

        addClarificationTrigger(lmActivity, new View[]{problem}, 50, item.wordOneCommon);

        answer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                answer.updateColor(DataPool.Color_Main, 255);
                answer2.updateColor(Color.parseColor("#cccccc"), 100);
                if (answerIsLeft) {
                    perf.performance = "good";
                } else {
                    perf.performance = "bad";
                }
                problem.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        lmActivity.goToNextCard();
                    }
                }, 1000);
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                answer2.updateColor(DataPool.Color_Main, 255);
                answer.updateColor(Color.parseColor("#cccccc"), 100);
                if (answerIsLeft) {
                    perf.performance = "bad";
                } else {
                    perf.performance = "good";
                }
                problem.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        lmActivity.goToNextCard();
                    }
                }, 1000);
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
            MyQuickToast.showShort(lmActivity, "clashed");
            if (index < max) {
                index += 1;
            } else {
                index = 0;
            }
        }

        if (answerIsLeft) {
            answer.config(DataPool.Color_Main, 255, item.wordTwo, outCall);
            answer2.config(DataPool.Color_Main, 255, DataPool.currentSetItems.get(index).wordTwo, outCall);
        } else {
            answer.config(DataPool.Color_Main, 255, DataPool.currentSetItems.get(index).wordTwo, outCall);
            answer2.config(DataPool.Color_Main, 255, item.wordTwo, outCall);
        }
    }

    private synchronized void someTextOut() {
        LogUtil.logDeubg(this, "someTextOut()");
        String clarification = "";
        if (tran.getTextModel().textOut) {
            clarification = item.twoTranscription;
        }
        if (problem.getTextModel().textOut) {
            clarification += "\n\n" + item.wordTwo;
        }
        if (answer.getTextModel().textOut) {
            clarification += "\n\n" + item.wordOne;
        }
        clarification += "\n\n" + item.wordOneCommon;
        addClarificationTrigger(lmActivity, new View[]{problem}, 50, clarification);
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
