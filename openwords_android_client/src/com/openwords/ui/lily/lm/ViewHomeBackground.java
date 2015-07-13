package com.openwords.ui.lily.lm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.openwords.R;
import com.openwords.util.log.LogUtil;

public class ViewHomeBackground extends View {

    private Paint colorPaint;
    private float viewWidth, viewHeight, left, top, right, bottom;
    private int color, alpha;
    private MyTweenComputer tween;
    private View root;
    private OnPressEnough onPress;

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
        addOnLayoutChangeListener(new OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateDimension(v.getWidth(), v.getHeight());
            }
        });
        tween = new MyTweenComputer(0, 1, 800, this, "aniProgress");
        LogUtil.logDeubg(this, "ViewHomeBackground initialized");
    }

    private void makeColor(int color, int alpha, View root) {
        this.root = root;
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
        resizeIcon();
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
    }

    public void touchAnimation() {
        tween.startAnimator();
    }

    private void resizeIcon() {
        if (root != null) {
            ImageView icon = (ImageView) root.findViewById(R.id.lily_button_home_icon);
            LayoutParams params = icon.getLayoutParams();
            int side = (int) (viewWidth / 5 * 3);
            params.width = side;
            params.height = side;
            icon.setLayoutParams(params);
            LogUtil.logDeubg(this, "resizeIcon()");
        }
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

    public void config(View root, int color, int alpha, boolean soundEffects, OnPressEnough onPress) {
        makeColor(color, alpha, root);
        setSoundEffectsEnabled(soundEffects);
        this.onPress = onPress;
    }

    public interface OnPressEnough {

        public void onPressFinished();
    }
}
