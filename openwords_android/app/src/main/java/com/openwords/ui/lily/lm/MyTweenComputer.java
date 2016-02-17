package com.openwords.ui.lily.lm;

import android.animation.ObjectAnimator;

/**
 *
 * @author hanaldo
 */
public class MyTweenComputer {

    public static float linear(float time, float beginning, float change, float duration) {
        return time / duration * change + beginning;
    }

    public static float easeOutExpo(float time, float beginning, float change, float duration) {
        return (time == duration) ? beginning + change : change * (-(float) Math.pow(2, -10 * time / duration) + 1) + beginning;
    }

    public static float easeOutBounce(float time, float beginning, float change, float duration) {
        if ((time /= duration) < (1 / 2.75f)) {
            return change * (7.5625f * time * time) + beginning;
        } else if (time < (2 / 2.75f)) {
            return change * (7.5625f * (time -= (1.5f / 2.75f)) * time + .75f) + beginning;
        } else if (time < (2.5 / 2.75)) {
            return change * (7.5625f * (time -= (2.25f / 2.75f)) * time + .9375f) + beginning;
        } else {
            return change * (7.5625f * (time -= (2.625f / 2.75f)) * time + .984375f) + beginning;
        }
    }

    private final float beginning;
    private final float change;
    private final int duration;
    private final ObjectAnimator animator;

    public MyTweenComputer(float beginning, float change, int duration, Object target, String propertyName) {
        this.beginning = beginning;
        this.change = change;
        this.duration = duration;
        animator = ObjectAnimator.ofInt(target, propertyName, 0, duration);
        animator.setDuration(duration);
        //animator.setAutoCancel(true);//API level 18
    }

    public void startAnimator() {
        animator.start();
    }

    public void cancelAnimator() {
        animator.cancel();
    }

    public float timeProceed(int time) {
        float value = easeOutExpo(time, beginning, change, duration);
        return value;
    }

}
