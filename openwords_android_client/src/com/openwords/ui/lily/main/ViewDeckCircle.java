package com.openwords.ui.lily.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ViewDeckCircle extends View {

    private Paint shadePaint, markerPaint;
    private float centerX, centerY, radius, textX, textY;
    private int color, alpha, markerColor, markerAlpha;
    private boolean drawMarker;
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

    public ViewDeckCircle(Context context, int color, int alpha, boolean drawMarker, int markerColor, int markerAlpha) {
        super(context);
        this.color = color;
        this.alpha = alpha;
        this.markerColor = markerColor;
        this.markerAlpha = markerAlpha;
        this.drawMarker = drawMarker;
        makePaints();
    }

    public void config(int color, int alpha, boolean drawMarker, int markerColor, int markerAlpha) {
        this.color = color;
        this.alpha = alpha;
        this.markerColor = markerColor;
        this.markerAlpha = markerAlpha;
        this.drawMarker = drawMarker;
        makePaints();
    }

    public void setText(String text) {
        this.text = text;
        measureText();
    }

    private void measureText() {
        if (text != null) {
            markerPaint.setTextSize(radius / 3 * 2);
            Rect textBounds = new Rect();
            markerPaint.getTextBounds("A", 0, 1, textBounds);
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
        } else {
            radius = viewWidth / 2;
            centerX = viewWidth / 2;
            centerY = viewHeight / 2;
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

        markerPaint = new Paint();
        markerPaint.setAntiAlias(true);
        markerPaint.setStyle(Paint.Style.FILL);
        markerPaint.setColor(markerColor);
        markerPaint.setAlpha(markerAlpha);
        markerPaint.setStrokeWidth(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, shadePaint);
        if (drawMarker) {
            canvas.drawLine(centerX, centerY - radius / 3, centerX, centerY + radius / 3, markerPaint);
            canvas.drawLine(centerX - radius / 3, centerY, centerX + radius / 3, centerY, markerPaint);
        }
        if (text != null) {
            canvas.drawText(text, textX, textY, markerPaint);
        }
    }
}
