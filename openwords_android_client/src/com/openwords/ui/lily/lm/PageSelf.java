package com.openwords.ui.lily.lm;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;

public class PageSelf extends FragmentLearningModule {

    private final int cardIndex;
    private final ActivityLearning lmActivity;
    private View myFragmentView;
    private ViewSoundBackground soundButton;
    private MyMaxTextView tran, problem, answer;
    private ImageView buttonOption, touch, sad, happy, happyBig, sadBig;
    private boolean canTouch, optionReady;
    private Performance perf;
    private SetItem item;
    private MyTweenComputer tween;
    private float optionDistance;
    private DialogForSettingSelection settingDialog;

    public PageSelf(int cardIndex, ActivityLearning lmActivity) {
        this.cardIndex = cardIndex;
        this.lmActivity = lmActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate for card: " + cardIndex);
        tween = new MyTweenComputer(0.0f, 1f, 800, this, "phaseOut");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);
        canTouch = true;
        optionReady = false;
        myFragmentView = inflater.inflate(R.layout.lily_page_lm_self, container, false);

        item = DataPool.currentSetItems.get(cardIndex);
        perf = DataPool.currentPerformance.get(cardIndex);
        if (perf == null) {
            MyQuickToast.showShort(lmActivity, "No performance data");
            return null;
        }

        buttonOption = (ImageView) myFragmentView.findViewById(R.id.page_self_image1);
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

        tran = (MyMaxTextView) myFragmentView.findViewById(R.id.page_self_text_tran);
        tran.config(DataPool.Color_Main, 255, "", 48);

        problem = (MyMaxTextView) myFragmentView.findViewById(R.id.page_self_text_problem);
        problem.config(DataPool.Color_Main, 255, item.wordTwo, 48);
        addClarificationTrigger(lmActivity, new View[]{problem}, 50, item.wordOneCommon);

        answer = (MyMaxTextView) myFragmentView.findViewById(R.id.page_self_text_answer);
        answer.config(DataPool.Color_Main, 255, item.wordOne, 48);

        sad = (ImageView) myFragmentView.findViewById(R.id.page_self_image2);
        touch = (ImageView) myFragmentView.findViewById(R.id.page_self_image3);
        happy = (ImageView) myFragmentView.findViewById(R.id.page_self_image4);
        sadBig = (ImageView) myFragmentView.findViewById(R.id.page_self_image5);
        happyBig = (ImageView) myFragmentView.findViewById(R.id.page_self_image6);

        sad.setVisibility(View.INVISIBLE);
        sadBig.setVisibility(View.GONE);
        happy.setVisibility(View.INVISIBLE);
        happyBig.setVisibility(View.GONE);
        answer.setVisibility(View.INVISIBLE);

        if (perf.performance.equals("good")) {
            touch.setImageResource(R.drawable.ic_lm_happy);
            touch.setColorFilter(null);
        } else if (perf.performance.equals("bad")) {
            touch.setImageResource(R.drawable.ic_lm_sad);
            touch.setColorFilter(null);
        } else {
            touch.setColorFilter(DataPool.Color_Main, PorterDuff.Mode.MULTIPLY);
        }

        touch.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent me) {
                if (!canTouch) {
                    return true;
                }
                int[] location = new int[2];
                float fingerX, fingerY;
                switch (me.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogUtil.logDeubg(this, "down");
                        optionReady = false;
                        showOption(touch, sad, sadBig, happy, happyBig, answer);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        LogUtil.logDeubg(this, "cancel");
                        optionReady = false;
                        hideOption(touch, sad, sadBig, happy, happyBig, answer);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!optionReady) {
                            break;
                        }
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
                        tween.cancelAnimator();
                        LogUtil.logDeubg(this, "up");
                        fingerX = me.getRawX();
                        fingerY = me.getRawY();
                        hideOption(touch, sad, sadBig, happy, happyBig, answer);
                        if (!optionReady) {
                            break;
                        }
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
        perf.performance = "bad";
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
        perf.performance = "good";
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                canTouch = true;
                lmActivity.goToNextCard();
            }
        }, 500);
    }

    private void showOption(View mid, View left, View leftBig, View right, View rightBig, View answer) {
        tween.cancelAnimator();
        mid.setVisibility(View.INVISIBLE);
        left.setVisibility(View.VISIBLE);
        leftBig.setVisibility(View.GONE);
        right.setVisibility(View.VISIBLE);
        rightBig.setVisibility(View.GONE);
        answer.setVisibility(View.VISIBLE);
        optionDistance = mid.getLeft() - left.getLeft();
        tween.startAnimator();
    }

    private void hideOption(View mid, View left, View leftBig, View right, View rightBig, View answer) {
        tween.cancelAnimator();
        mid.setVisibility(View.VISIBLE);
        left.setVisibility(View.INVISIBLE);
        leftBig.setVisibility(View.GONE);
        right.setVisibility(View.INVISIBLE);
        rightBig.setVisibility(View.GONE);
        answer.setVisibility(View.INVISIBLE);
    }

    public void setPhaseOut(int time) {
        float f = tween.timeProceed(time);
        sad.setAlpha(f);
        float x1 = (1f - f) * optionDistance;
        sad.setTranslationX(x1);

        happy.setAlpha(f);
        float x2 = (1f - f) * -optionDistance;
        happy.setTranslationX(x2);

        answer.setAlpha(f);
        if (f == 1) {
            optionReady = true;
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
