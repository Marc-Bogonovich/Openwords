package com.openwords.view;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.openwords.R;

public class MainActivity extends Activity {
        Calendar c = Calendar.getInstance(); 
        
        @SuppressWarnings("deprecation")
		@Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
               this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Hides the title and notification bar on this page
               this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                
                setContentView(R.layout.activity_main);
                Display display = getWindowManager().getDefaultDisplay(); 
            int width = display.getWidth();
            int height = display.getHeight();

            if(width<height){ // potrait mode
                    lift_word_from_buttom(R.id.flashword_string_homePageLift01,200);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift02,150);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift03,350);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift04,100);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift05,260);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift06,400);
            }
            else{ // landscape mode
                    lift_word_from_buttom(R.id.flashword_string_homePageLift01,150);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift02,100);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift03,300);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift04,50);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift05,210);
                        lift_word_from_buttom(R.id.flashword_string_homePageLift06,250);
            }   
                
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }
        
        private void lift_word_from_buttom(int stringName, int distance) {
                TextView word = (TextView) findViewById(stringName);        
                Animation animation = new TranslateAnimation(0, 0, 50, (-1*distance));
                animation.setDuration(5000); 
            word.startAnimation(animation);  // start animation 
            animation.setFillAfter(true);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.i("clicks","You Clicked screen");
            loginButtonClick();
            return super.onTouchEvent(event); 
        }
        
        
        
        private void loginButtonClick() {
                Intent loginPageIntent = new Intent(MainActivity.this, LoginPage.class);
                MainActivity.this.startActivity(loginPageIntent);
        }
        

}