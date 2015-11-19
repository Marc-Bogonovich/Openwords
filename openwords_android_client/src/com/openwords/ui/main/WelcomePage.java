package com.openwords.ui.main;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import com.openwords.R;

public class WelcomePage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            lift_word_from_buttom(R.id.flashword_string_homePageLift07, 500);
            lift_word_from_buttom(R.id.flashword_string_homePageLift08, 700);
        } else { // landscape mode
            lift_word_from_buttom(R.id.flashword_string_homePageLift01, 150);
            lift_word_from_buttom(R.id.flashword_string_homePageLift02, 100);
            lift_word_from_buttom(R.id.flashword_string_homePageLift03, 300);
            lift_word_from_buttom(R.id.flashword_string_homePageLift04, 50);
            lift_word_from_buttom(R.id.flashword_string_homePageLift05, 210);
            lift_word_from_buttom(R.id.flashword_string_homePageLift06, 250);
            lift_word_from_buttom(R.id.flashword_string_homePageLift07, 300);
            lift_word_from_buttom(R.id.flashword_string_homePageLift08, 500);
        }
    }

    private void lift_word_from_buttom(int stringName, int distance) {
        View word = findViewById(stringName);
        Animation animation = new TranslateAnimation(0, 0, 50, (-1 * distance));
        animation.setDuration(5000);
        word.startAnimation(animation);  // start animation 
        animation.setFillAfter(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        finish();
        return true;
    }
}
