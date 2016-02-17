package com.openwords.ui.lily.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ViewDeckCircle extends View {

    private final float iconScale = 0.4f;
    private Bitmap icon;
    private Bitmap drawIcon;
    private Paint shadePaint, textPaint, iconPaint;
    private float centerX, centerY, radius, textX, textY, iconX, iconY;
    private int color, alpha, iconColor, textColor, textAlpha, iconResource;
    private String text;

    public ViewDeckCircle(Context context) {
        super(context);
    }

    public ViewDeckCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewDeckCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ViewDeckCircle(Context context, int color, int alpha, int iconResource, int iconColor, int textColor, int markerAlpha) {
        super(context);
        this.color = color;
        this.alpha = alpha;
        this.iconColor = iconColor;
        this.textColor = textColor;
        this.textAlpha = markerAlpha;
        this.iconResource = iconResource;
        makePaints();
        loadImages(iconResource);
    }

    public void config(int color, int alpha, int iconResource, int iconColor, int textColor, int textAlpha) {
        this.color = color;
        this.alpha = alpha;
        this.iconColor = iconColor;
        this.textColor = textColor;
        this.textAlpha = textAlpha;
        this.iconResource = iconResource;
        makePaints();
        loadImages(iconResource);
    }

    private void loadImages(int image) {
        if (image != 0) {
            icon = BitmapFactory.decodeResource(getResources(), image);
        }
    }

    private void resizeIcon(int side) {
        if (icon != null) {
            drawIcon = Bitmap.createScaledBitmap(icon, side, side, true);
            iconX = centerX - side / 2;
            iconY = centerY - side / 2;
        }
    }

    public void setText(String text) {
        this.text = text;
        measureText();
    }

    private void measureText() {
        if (text != null) {
            textPaint.setTextSize(radius / 3 * 2);
            Rect textBounds = new Rect();
            textPaint.getTextBounds("A", 0, 1, textBounds);
            float textWidth = textBounds.width();
            float textHeight = textBounds.height();
            textX = centerX - textWidth * text.length() / 2;
            textY = centerY + textHeight / 2;
            invalidate();
            //LogUtil.logDeubg(this, "measureText()");
        }
    }

    public void updateDimension(int viewWidth, int viewHeight) {
        if (viewWidth > viewHeight) {
            radius = viewHeight / 2;
            centerX = viewWidth / 2;
            centerY = viewHeight / 2;
            resizeIcon((int) (viewHeight * iconScale));
        } else {
            radius = viewWidth / 2;
            centerX = viewWidth / 2;
            centerY = viewHeight / 2;
            resizeIcon((int) (viewWidth * iconScale));
        }
        measureText();
        //LogUtil.logDeubg(this, "updateDimension: " + viewWidth + " " + viewHeight);
    }

    public void updateColor(int color) {
        shadePaint.setColor(color);
        invalidate();
    }

    private void makePaints() {
        shadePaint = new Paint();
        shadePaint.setAntiAlias(true);
        shadePaint.setStyle(Paint.Style.FILL);
        shadePaint.setColor(color);
        shadePaint.setAlpha(alpha);

        if (textColor != 0) {
            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(textColor);
            textPaint.setAlpha(textAlpha);
            textPaint.setStrokeWidth(5);
        }

        if (iconResource != 0) {
            iconPaint = new Paint();
            iconPaint.setAntiAlias(true);
            iconPaint.setFilterBitmap(true);
            iconPaint.setDither(true);
            if (iconColor != 0) {
                ColorFilter filter = new PorterDuffColorFilter(iconColor, PorterDuff.Mode.MULTIPLY);
                iconPaint.setColorFilter(filter);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, shadePaint);
        if (text != null) {
            canvas.drawText(text, textX, textY, textPaint);
        }
        if (drawIcon != null) {
            canvas.drawBitmap(drawIcon, iconX, iconY, iconPaint);
        }
    }
}
