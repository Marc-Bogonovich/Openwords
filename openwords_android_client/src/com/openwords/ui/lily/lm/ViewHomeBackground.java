package com.openwords.ui.lily.lm;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
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
    private Activity activity;

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
        tween = new MyTweenComputer(0, 1, 500, this, "aniProgress");
        LogUtil.logDeubg(this, "ViewHomeBackground initialized");
    }

    private void makeColor(int color, int alpha, Activity activity) {
        this.activity = activity;
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
        if (f == 1) {
            LogUtil.logDeubg(this, "setAniProgress finished");
        }
        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, colorPaint);
    }

    public void touchAnimation() {
        tween.startAnimator();
    }

    private void resizeIcon() {
        if (activity != null) {
            ImageView icon = (ImageView) activity.findViewById(R.id.lily_button_home_icon);
            LayoutParams params = icon.getLayoutParams();
            int side = (int) (viewWidth / 5 * 3);
            params.width = side;
            params.height = side;
            icon.setLayoutParams(params);
            LogUtil.logDeubg(this, "resizeIcon()");
        }
    }

    public void config(Activity activity, int color, int alpha, boolean soundEffects, View.OnClickListener onclick) {
        makeColor(color, alpha, activity);
        setSoundEffectsEnabled(soundEffects);
        setOnClickListener(onclick);
    }
}
