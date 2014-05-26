package com.openwords.view;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import com.openwords.R;
import com.openwords.util.log.LogUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hides the title and notification bar on this page
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose

        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (width < height) { // potrait mode
            lift_word_from_buttom(R.id.flashword_string_homePageLift01, 200);
            lift_word_from_buttom(R.id.flashword_string_homePageLift02, 150);
            lift_word_from_buttom(R.id.flashword_string_homePageLift03, 350);
            lift_word_from_buttom(R.id.flashword_string_homePageLift04, 100);
            lift_word_from_buttom(R.id.flashword_string_homePageLift05, 260);
            lift_word_from_buttom(R.id.flashword_string_homePageLift06, 400);
        } else { // landscape mode
            lift_word_from_buttom(R.id.flashword_string_homePageLift01, 150);
            lift_word_from_buttom(R.id.flashword_string_homePageLift02, 100);
            lift_word_from_buttom(R.id.flashword_string_homePageLift03, 300);
            lift_word_from_buttom(R.id.flashword_string_homePageLift04, 50);
            lift_word_from_buttom(R.id.flashword_string_homePageLift05, 210);
            lift_word_from_buttom(R.id.flashword_string_homePageLift06, 250);
        }

    }

    private void lift_word_from_buttom(int stringName, int distance) {
        TextView word = (TextView) findViewById(stringName);
        Animation animation = new TranslateAnimation(0, 0, 50, (-1 * distance));
        animation.setDuration(5000);
        word.startAnimation(animation);  // start animation 
        animation.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.logDeubg(this, "You Clicked screen");
        finish();
        return super.onTouchEvent(event);
    }

}
