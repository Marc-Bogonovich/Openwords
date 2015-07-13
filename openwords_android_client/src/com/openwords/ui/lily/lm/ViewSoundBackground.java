package com.openwords.ui.lily.lm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.openwords.R;
import com.openwords.util.log.LogUtil;

public class ViewSoundBackground extends View {

    private Paint colorPaint;
    private float centerX, centerY, radius, initRadius, iconX, iconY;
    private int color, alpha;
    private MyTweenComputer tween;
    private static Bitmap icon;
    private static final float iconScale = 0.5f;

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
        loadImages();
        addOnLayoutChangeListener(new OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateDimension(v.getWidth(), v.getHeight());
            }
        });
        tween = new MyTweenComputer(0.5f, 0.5f, 500, this, "aniRadius");
        LogUtil.logDeubg(this, "ViewSoundBackground initialized");
    }

    private void loadImages() {
        Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        if (icon == null) {
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sound, options);
        }
    }

    private void makeColor(int color, int alpha) {
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
            resizeIcon((int) (viewHeight * iconScale));
        } else {
            initRadius = viewWidth / 2;
            radius = initRadius;
            centerX = viewWidth / 2;
            centerY = viewHeight / 2;
            resizeIcon((int) (viewWidth * iconScale));
        }
        //LogUtil.logDeubg(this, "updateDimension: " + viewWidth + " " + viewHeight);
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
        if (icon != null) {
            canvas.drawBitmap(icon, iconX, iconY, null);
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

    private void resizeIcon(int side) {
        icon = Bitmap.createScaledBitmap(icon, side, side, true);
        iconX = centerX - side / 2;
        iconY = centerY - side / 2;
        LogUtil.logDeubg(this, "resizeIcon()");
    }

    public void config(int color, int alpha, boolean soundEffects, View.OnClickListener onclick) {
        makeColor(color, alpha);
        setSoundEffectsEnabled(soundEffects);
        setOnClickListener(onclick);
    }
}
