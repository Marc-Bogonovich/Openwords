package com.openwords.ui.lily.lm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.openwords.R;
import com.openwords.util.log.LogUtil;

public class ViewSoundBackground extends View {

    private Paint colorPaint;
    private float centerX, centerY, radius, initRadius;
    private int color, alpha;
    private MyTweenComputer tween;
    private View root;

    public ViewSoundBackground(Context context) {
        super(context);
        init();
    }

    public ViewSoundBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewSoundBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addOnLayoutChangeListener(new OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateDimension(v.getWidth(), v.getHeight());
            }
        });
        tween = new MyTweenComputer(0.5f, 0.5f, 500, this, "aniRadius");
        LogUtil.logDeubg(this, "ViewSoundBackground initialized");
    }

    private void makeColor(int color, int alpha, View root) {
        this.root = root;
        this.color = color;
        this.alpha = alpha;
        makePaints();
    }

    private void updateDimension(int viewWidth, int viewHeight) {
        if (viewWidth > viewHeight) {
            initRadius = viewHeight / 2;
            radius = initRadius;
            centerX = viewWidth / 2;
            centerY = viewHeight / 2;
        } else {
            initRadius = viewWidth / 2;
            radius = initRadius;
            centerX = viewWidth / 2;
            centerY = viewHeight / 2;
        }
        //LogUtil.logDeubg(this, "updateDimension: " + viewWidth + " " + viewHeight);
        resizeIcon();
    }

    private void makePaints() {
        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.FILL);
        colorPaint.setColor(color);
        colorPaint.setAlpha(alpha);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (colorPaint != null) {
            canvas.drawCircle(centerX, centerY, radius, colorPaint);
        }
    }

    public void setAniRadius(int time) {
        float f = tween.timeProceed(time);
        radius = initRadius * f;
        if (f == 1) {
            LogUtil.logDeubg(this, "setAniRadius finished");
        }
        this.invalidate();
    }

    public void touchAnimation() {
        tween.startAnimator();
    }

    private void resizeIcon() {
        if (root != null) {
            ImageView icon = (ImageView) root.findViewById(R.id.lily_button_sound_icon);
            ViewGroup.LayoutParams params = icon.getLayoutParams();
            int side = (int) initRadius;
            params.width = side;
            params.height = side;
            icon.setLayoutParams(params);
            LogUtil.logDeubg(this, "resizeIcon()");
        }
    }

    public void config(View root, int color, int alpha, boolean soundEffects, View.OnClickListener onclick) {
        makeColor(color, alpha, root);
        setSoundEffectsEnabled(soundEffects);
        setOnClickListener(onclick);
    }
}
