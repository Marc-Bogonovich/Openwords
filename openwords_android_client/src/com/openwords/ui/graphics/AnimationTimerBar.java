package com.openwords.ui.graphics;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class AnimationTimerBar extends Animation {

    private final float startWeight;
    private final float deltaWeight;
    private final View view;

    public AnimationTimerBar(float startWeight, float endWeight, View v) {
        this.startWeight = startWeight;
        deltaWeight = endWeight - startWeight;
        view = v;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.weight = startWeight + deltaWeight * interpolatedTime;
        view.setLayoutParams(lp);
    }

    @Override
    public Animation clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
