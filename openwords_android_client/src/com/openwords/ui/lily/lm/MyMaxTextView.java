package com.openwords.ui.lily.lm;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class MyMaxTextView extends View {

    public final float sizeOffsetUnit = 9;
    public final float minTextSize = 16;
    private MyCanvasTextModel textModel;
    private TextIsOutCallback out;

    public MyMaxTextView(Context context) {
        super(context);
        addChangeListener();
    }

    public MyMaxTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addChangeListener();

    }

    public MyMaxTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addChangeListener();
    }

    private void addChangeListener() {
        addOnLayoutChangeListener(new OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateDimension(v.getWidth(), v.getHeight(), textModel);
            }
        });
    }

    public void config(int color, int alpha, String text, TextIsOutCallback out) {
        textModel = new MyCanvasTextModel(color, alpha, text);
        this.out = out;
        invalidate();
    }

    public void updateColor(int color, int alpha) {
        textModel.paint.setColor(color);
        textModel.paint.setAlpha(alpha);
        invalidate();
    }

    private void updateDimension(int width, int height, MyCanvasTextModel myText) {
        myText.viewWidth = width;
        myText.viewHeight = height;
        myText.centerX = width / 2;
        myText.centerY = height / 2;
        //LogUtil.logDeubg(this, "updateDimension: " + viewWidth + " " + viewHeight);

        int numberOffset = 0;
        float newSize;
        do {
            numberOffset += 1;
            newSize = height - sizeOffsetUnit * numberOffset;
            boolean doBreak = false;
            if (newSize < minTextSize) {
                newSize = minTextSize;
                doBreak = true;
                //LogUtil.logDeubg(this, "reached minTextSize");
            }
            myText.paint.setTextSize(newSize);
            myText.paint.getTextBounds(myText.text, 0, myText.text.length(), myText.textBounds);
            myText.textWidth = myText.textBounds.width();
            myText.textHeight = myText.textBounds.height();
            //LogUtil.logDeubg(this, "textBounds: " + myText.textWidth + " " + myText.textHeight);
            if (doBreak) {
                break;
            }
        } while (myText.viewWidth < myText.textWidth + newSize || myText.viewHeight < myText.textHeight + newSize);

        myText.textX = myText.centerX - myText.textWidth / 2;
        myText.initialTextX = myText.textX;
        myText.textY = myText.textHeight;//myText.centerY + myText.textHeight / 2;
        myText.textOut = myText.textWidth + sizeOffsetUnit > myText.viewWidth || myText.textHeight + sizeOffsetUnit > myText.viewHeight;

        if (myText.textOut) {
            myText.textX = 0;
            if (out != null) {
                out.tell();
            }
        }
        invalidate();
    }

    public MyCanvasTextModel getTextModel() {
        return textModel;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(textModel.text, textModel.textX, textModel.textY, textModel.paint);
    }

    public interface TextIsOutCallback {

        public void tell();
    }
}
