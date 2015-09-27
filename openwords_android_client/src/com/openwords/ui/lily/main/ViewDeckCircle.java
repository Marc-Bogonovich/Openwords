package com.openwords.ui.lily.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class ViewDeckCircle extends View {

    private Paint shadePaint, markerPaint;
    private float centerX, centerY, radius;
    private final int color, alpha, markerColor, markerAlpha;
    private final boolean drawMarker;
    private String text;

    public ViewDeckCircle(Context context, int color, int alpha, boolean drawMarker, int markerColor, int markerAlpha) {
        super(context);
        this.color = color;
        this.alpha = alpha;
        this.markerColor = markerColor;
        this.markerAlpha = markerAlpha;
        this.drawMarker = drawMarker;
        makePaints();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
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
        //LogUtil.logDeubg(this, "updateDimension: " + viewWidth + " " + viewHeight);
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
            canvas.drawText(text, centerX, centerY, markerPaint);
        }
    }
}
