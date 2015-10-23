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
    public Rect textBounds;
    public boolean textOut;

    public MyCanvasTextModel(int color, int alpha, String text) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setAlpha(alpha);

        this.text = text;
        textBounds = new Rect();
    }
}
