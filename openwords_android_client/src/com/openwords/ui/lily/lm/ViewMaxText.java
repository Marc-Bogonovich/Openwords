package com.openwords.ui.lily.lm;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.openwords.util.log.LogUtil;

public class ViewMaxText extends View {

    private MyCanvasTextModel mainText, otherText;
    private float minTextSize;
    private static final float sizeOffset = 96;
    private ObjectAnimator alphaAnimator, outTextAnimator;

    public ViewMaxText(Context context) {
        super(context);
        addChangeListener();
    }

    public ViewMaxText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addChangeListener();

    }

    public ViewMaxText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addChangeListener();
    }

    private void addChangeListener() {
        addOnLayoutChangeListener(new OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateDimension(v.getWidth(), v.getHeight(), mainText);
                if (otherText != null) {
                    updateDimension(v.getWidth(), v.getHeight(), otherText);
                }
            }
        });

        alphaAnimator = ObjectAnimator.ofInt(this, "textAlpha", 255, 0);
        alphaAnimator.setDuration(500);
        //alphaAnimator.setAutoCancel(true);

        outTextAnimator = ObjectAnimator.ofInt(this, "textPosition", 1, 1000);
        outTextAnimator.setDuration(3000);
        //outTextAnimator.setAutoCancel(true);
    }

    public void config(int color, int alpha, String text, float minTextSize, String swapText) {
        mainText = new MyCanvasTextModel(color, alpha, text);
        if (swapText != null) {
            otherText = new MyCanvasTextModel(color, 0, swapText);
        }
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
        if (otherText != null) {
            canvas.drawText(otherText.text, otherText.textX, otherText.textY, otherText.paint);
        }
    }

    public void setTextAlpha(int newAlpha) {
        mainText.paint.setAlpha(newAlpha);
        int otherAlpha = 255 - newAlpha;
        if (newAlpha == mainText.getAlpha()) {
            otherAlpha = 0;
        }
        otherText.paint.setAlpha(otherAlpha);
        invalidate();
    }

    public void setTextPosition(int xOff) {
        if (mainText.textOut) {
            animateTextPosition(xOff, mainText);
        }
        if (otherText != null) {
            animateTextPosition(xOff, otherText);
        }
    }

    private void animateTextPosition(int xOff, MyCanvasTextModel t) {
        float start = t.viewWidth / 5;
        float more = t.textWidth * xOff / 1000;
        t.textX = start - more;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (otherText != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    LogUtil.logDeubg(this, "down");
                    alphaAnimator.start();
                    if (otherText.textOut) {
                        outTextAnimator.start();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    LogUtil.logDeubg(this, "up");
                    alphaAnimator.cancel();
                    setTextAlpha(mainText.getAlpha());
                    outTextAnimator.cancel();
                    otherText.textX = otherText.initialTextX;
                    break;
            }
        } else if (mainText.textOut) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    outTextAnimator.start();
                    break;

                case MotionEvent.ACTION_UP:
                    outTextAnimator.cancel();
                    mainText.textX = mainText.initialTextX;
                    invalidate();
                    break;
            }
        }
        return true;
    }
}
