package com.openwords.ui.lily.lm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.openwords.R;
import com.openwords.util.log.LogUtil;

public class ViewHomeBackground extends View {

    private Paint colorPaint, iconPaint;
    private float viewWidth, viewHeight, left, top, right, bottom, iconX, iconY;
    private int color, alpha;
    private MyTweenComputer tween;
    private OnPressEnough onPress;
    private static Bitmap icon, drawIcon;
    private static final float iconScale = 0.9f;

    public ViewHomeBackground(Context context) {
        super(context);
        init();
    }

    public ViewHomeBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ViewHomeBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        loadImages();
        addOnLayoutChangeListener(new OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateDimension(v.getWidth(), v.getHeight());
            }
        });
        tween = new MyTweenComputer(0, 1, 800, this, "aniProgress");
        LogUtil.logDeubg(this, "ViewHomeBackground initialized");
    }

    private void loadImages() {
        if (icon == null) {
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_home);
        }
    }

    private void makeColor(int color, int alpha) {
        this.color = color;
        this.alpha = alpha;
        makePaints();
    }

    private void updateDimension(int viewWidth, int viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        left = 0;
        top = 0;
        right = viewWidth;
        bottom = viewHeight;
        //LogUtil.logDeubg(this, "updateDimension: " + viewWidth + " " + viewHeight);
        resizeIcon((int) (viewHeight * iconScale));
    }

    private void makePaints() {
        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.FILL);
        colorPaint.setColor(color);
        colorPaint.setAlpha(alpha);
    }

    public void setAniProgress(int time) {
        float f = tween.timeProceed(time);
        if (f <= 0.5f) {
            top = viewHeight * f;
            bottom = viewHeight - viewHeight * f;
        } else {
            top = (viewHeight - viewHeight * f);
            bottom = viewHeight * f;
        }
        invalidate();
        if (f == 1) {
            LogUtil.logDeubg(this, "setAniProgress finished");
            if (onPress != null) {
                onPress.onPressFinished();
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (colorPaint != null) {
            canvas.drawRect(left, top, right, bottom, colorPaint);
        }
        if (drawIcon != null) {
            canvas.drawBitmap(drawIcon, iconX, iconY, iconPaint);
        }
    }

    public void touchAnimation() {
        tween.startAnimator();
    }

    private void resizeIcon(int side) {
        drawIcon = Bitmap.createScaledBitmap(icon, side, side, true);
        iconX = right - side + side / 10;
        iconY = bottom - side + side / 6;
        LogUtil.logDeubg(this, "resizeIcon(): viewHeight " + side);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.logDeubg(this, "down");
                tween.startAnimator();
                break;

            case MotionEvent.ACTION_UP:
                LogUtil.logDeubg(this, "up");
                tween.cancelAnimator();
                top = 0;
                bottom = viewHeight;
                invalidate();
                break;
        }
        return true;
    }

    public void config(int color, int alpha, boolean soundEffects, OnPressEnough onPress) {
        makeColor(color, alpha);
        setSoundEffectsEnabled(soundEffects);
        this.onPress = onPress;
    }

    public interface OnPressEnough {

        public void onPressFinished();
    }
}
