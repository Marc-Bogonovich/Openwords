package com.openwords.ui.lily.lm;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 *
 * @author hanaldo
 */
public class MyCanvasTextModel {

    public Paint paint;
    public String text = "";
    public float viewWidth, viewHeight, centerX, centerY, textWidth, textHeight, textX, textY, initialTextX;
    private final int color, alpha;
    public Rect textBounds;
    public boolean textOut;

    public MyCanvasTextModel(int color, int alpha, String text) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setAlpha(alpha);

        this.color = color;
        this.alpha = alpha;
        this.text = text;
        textBounds = new Rect();
    }

    public int getColor() {
        return color;
    }

    public int getAlpha() {
        return alpha;
    }

}
