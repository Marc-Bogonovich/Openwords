package com.openwords.learningmodule;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyPager extends ViewPager {

    public MyPager(Context context) {
        super(context);
    }

    public MyPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent me) {
        return false;
    }
}
