package com.openwords.ui.lily.lm;

import android.content.res.AssetFileDescriptor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.openwords.R;
import com.openwords.learningmodule.ActivityLearning;
import com.openwords.learningmodule.FragmentLearningModule;
import com.openwords.model.DataPool;
import com.openwords.sound.SoundPlayer;
import com.openwords.util.log.LogUtil;
import java.io.IOException;

public class PageSelf extends FragmentLearningModule {

    private final int cardIndex;
    private final ActivityLearning lmActivity;
    private View myFragmentView;
    private ViewSoundBackground soundButton;
    private MyMaxTextView tran, problem, answer;
    private ImageView buttonOption, touch, sad, happy, happyBig, sadBig;
    private boolean canTouch;

    public PageSelf(int cardIndex, ActivityLearning lmActivity) {
        this.cardIndex = cardIndex;
        this.lmActivity = lmActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate for card: " + cardIndex);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);
        canTouch = true;
        myFragmentView = inflater.inflate(R.layout.lily_page_lm_self, container, false);

        buttonOption = (ImageView) myFragmentView.findViewById(R.id.page_self_image1);
        buttonOption.setColorFilter(DataPool.Color_Main, PorterDuff.Mode.MULTIPLY);

        soundButton = (ViewSoundBackground) myFragmentView.findViewById(R.id.lily_button_sound_bg);
        soundButton.config(DataPool.Color_Main, 255, false, new View.OnClickListener() {

            public void onClick(View view) {
                soundButton.touchAnimation();
                try {
                    AssetFileDescriptor test = getActivity().getAssets().openFd("pang.mp3");
                    SoundPlayer.playSound(test);
                } catch (IOException ex) {
                    LogUtil.logWarning(this, ex);
                }
            }
        });

        tran = (MyMaxTextView) myFragmentView.findViewById(R.id.page_self_text_tran);
        tran.config(DataPool.Color_Main, 255, "Transcription", 48);

        problem = (MyMaxTextView) myFragmentView.findViewById(R.id.page_self_text_problem);
        problem.config(DataPool.Color_Main, 255, "Problem", 48);

        answer = (MyMaxTextView) myFragmentView.findViewById(R.id.page_self_text_answer);
        answer.config(DataPool.Color_Main, 255, "Answer", 48);

        sad = (ImageView) myFragmentView.findViewById(R.id.page_self_image2);
        touch = (ImageView) myFragmentView.findViewById(R.id.page_self_image3);
        happy = (ImageView) myFragmentView.findViewById(R.id.page_self_image4);
        sadBig = (ImageView) myFragmentView.findViewById(R.id.page_self_image5);
        happyBig = (ImageView) myFragmentView.findViewById(R.id.page_self_image6);
        touch.setColorFilter(DataPool.Color_Main, PorterDuff.Mode.MULTIPLY);

        sad.setVisibility(View.GONE);
        sadBig.setVisibility(View.GONE);
        happy.setVisibility(View.GONE);
        happyBig.setVisibility(View.GONE);
        answer.setVisibility(View.INVISIBLE);

        touch.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent me) {
                if (!canTouch) {
                    return true;
                }
                int[] location = new int[2];
                float fingerX, fingerY;
                switch (me.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showOption(touch, sad, sadBig, happy, happyBig, answer);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        hideOption(touch, sad, sadBig, happy, happyBig, answer);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        fingerX = me.getRawX();
                        fingerY = me.getRawY();
                        if (isFingerInElement(location, happy, fingerX, fingerY)) {
                            rightOn(sad, sadBig, happy, happyBig);
                            break;
                        } else {
                            allOff(sad, sadBig, happy, happyBig);
                        }
                        if (isFingerInElement(location, sad, fingerX, fingerY)) {
                            leftOn(sad, sadBig, happy, happyBig);
                            break;
                        } else {
                            allOff(sad, sadBig, happy, happyBig);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        fingerX = me.getRawX();
                        fingerY = me.getRawY();
                        hideOption(touch, sad, sadBig, happy, happyBig, answer);
                        if (isFingerInElement(location, happy, fingerX, fingerY)) {
                            canTouch = false;
                            answer.setVisibility(View.VISIBLE);
                            rightConfirm(touch);
                        } else if (isFingerInElement(location, sad, fingerX, fingerY)) {
                            canTouch = false;
                            answer.setVisibility(View.VISIBLE);
                            leftConfirm(touch);
                        }
                        break;
                }
                return true;
            }
        });

        return myFragmentView;
    }

    private boolean isFingerInElement(int[] location, View view, float fingerX, float fingerY) {
        view.getLocationOnScreen(location);
        float w = view.getWidth();
        float h = view.getHeight();

        return fingerX >= location[0]
                && fingerX <= location[0] + w
                && fingerY >= location[1]
                && location[0] <= location[0] + h;
    }

    private void leftOn(View left, View leftBig, View right, View rightBig) {
        left.setVisibility(View.GONE);
        leftBig.setVisibility(View.VISIBLE);
        right.setVisibility(View.VISIBLE);
        rightBig.setVisibility(View.GONE);
    }

    private void rightOn(View left, View leftBig, View right, View rightBig) {
        left.setVisibility(View.VISIBLE);
        leftBig.setVisibility(View.GONE);
        right.setVisibility(View.GONE);
        rightBig.setVisibility(View.VISIBLE);
    }

    private void allOff(View left, View leftBig, View right, View rightBig) {
        left.setVisibility(View.VISIBLE);
        leftBig.setVisibility(View.GONE);
        right.setVisibility(View.VISIBLE);
        rightBig.setVisibility(View.GONE);
    }

    private void leftConfirm(ImageView mid) {
        mid.setImageResource(R.drawable.ic_lm_sad);
        mid.setColorFilter(null);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                canTouch = true;
                lmActivity.goToNextCard();
            }
        }, 500);
    }

    private void rightConfirm(ImageView mid) {
        mid.setImageResource(R.drawable.ic_lm_happy);
        mid.setColorFilter(null);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                canTouch = true;
                lmActivity.goToNextCard();
            }
        }, 500);
    }

    private void showOption(View mid, View left, View leftBig, View right, View rightBig, View answer) {
        mid.setVisibility(View.INVISIBLE);
        left.setVisibility(View.VISIBLE);
        leftBig.setVisibility(View.GONE);
        right.setVisibility(View.VISIBLE);
        rightBig.setVisibility(View.GONE);
        answer.setVisibility(View.VISIBLE);
    }

    private void hideOption(View mid, View left, View leftBig, View right, View rightBig, View answer) {
        mid.setVisibility(View.VISIBLE);
        left.setVisibility(View.INVISIBLE);
        leftBig.setVisibility(View.GONE);
        right.setVisibility(View.INVISIBLE);
        rightBig.setVisibility(View.GONE);
        answer.setVisibility(View.INVISIBLE);
    }
}
