package com.openwords.ui.lily.lm;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class MyMaxTextView extends View {

    private static final float sizeOffset = 64;

    private MyCanvasTextModel mainText;
    private float minTextSize;

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
                updateDimension(v.getWidth(), v.getHeight(), mainText);
            }
        });
    }

    public void config(int color, int alpha, String text, float minTextSize) {
        mainText = new MyCanvasTextModel(color, alpha, text);
        this.minTextSize = minTextSize;
        invalidate();
    }

    private void updateDimension(int width, int height, MyCanvasTextModel myText) {
        myText.viewWidth = width;
        myText.viewHeight = height;
        myText.centerX = width / 2;
        myText.centerY = height / 2;
        //LogUtil.logDeubg(this, "updateDimension: " + viewWidth + " " + viewHeight);

        int numberOffset = 0;
        do {
            numberOffset += 1;
            float newSize = height - sizeOffset * numberOffset;
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
            //LogUtil.logDeubg(this, "textBounds: " + textWidth + " " + textHeight);
            if (doBreak) {
                break;
            }
        } while (myText.viewWidth < myText.textWidth + sizeOffset);

        myText.textX = myText.centerX - myText.textWidth / 2;
        myText.initialTextX = myText.textX;
        myText.textY = myText.centerY + myText.textHeight / 2;
        myText.textOut = myText.textWidth > myText.viewWidth;
        //LogUtil.logDeubg(this, "textOut: " + myText.textOut + ", " + myText.text);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(mainText.text, mainText.textX, mainText.textY, mainText.paint);
    }
}
