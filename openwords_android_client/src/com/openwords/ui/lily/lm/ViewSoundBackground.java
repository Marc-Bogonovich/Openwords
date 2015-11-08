package com.openwords.ui.lily.lm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.View;
import com.openwords.R;
import com.openwords.util.log.LogUtil;

public class ViewSoundBackground extends View {

    private static Bitmap icon;
    private static Bitmap drawIcon;
    private static final float iconScale = 0.5f;

    private Paint colorPaint, iconPaint;
    private float centerX, centerY, radius, initRadius, iconX, iconY;
    private MyTweenComputer tween;

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
        if (icon == null) {
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sound);
        }
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

    @Override
    public void onDraw(Canvas canvas) {
        if (colorPaint != null) {
            canvas.drawCircle(centerX, centerY, radius, colorPaint);
        }
        if (drawIcon != null) {
            canvas.drawBitmap(drawIcon, iconX, iconY, iconPaint);
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
        drawIcon = Bitmap.createScaledBitmap(icon, side, side, true);
        iconX = centerX - side / 2;
        iconY = centerY - side / 2;
        LogUtil.logDeubg(this, "resizeIcon(): side " + side);
    }

    public void config(int color, int alpha, boolean soundEffects, boolean changeIconColor, int iconColor, View.OnClickListener onclick) {
        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.FILL);
        colorPaint.setColor(color);
        colorPaint.setAlpha(alpha);

        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);
        iconPaint.setFilterBitmap(true);
        iconPaint.setDither(true);
        if (changeIconColor) {
            ColorFilter filter = new PorterDuffColorFilter(iconColor, PorterDuff.Mode.MULTIPLY);
            iconPaint.setColorFilter(filter);
        }

        setSoundEffectsEnabled(soundEffects);
        setOnClickListener(onclick);
    }
}
