package com.openwords.view.learningModule;

//Marc - Lines related to the slide activity are between "//" and "//"

//THESE LINES APPEAR TO BE HAVE BEEN AUTOMATICALLY GENERATED TO FIND com.openwords_alpha_04.R, but they do not appear necessary 
//import com.openwords_alpha_05.R;
//import com.openwords_alpha_05.SlideActivity.MyGestureDetector;
//import com.openwords_alpha_04.R;

import com.openwords.R;
import com.openwords.util.PlateDbHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
//
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
//

public class SelfEvaluate extends Activity {

	//
	private Animation mInFromRight;
    private Animation mOutToLeft;
    private Animation mInFromLeft;
    private Animation mOutToRight;
    private ViewFlipper mViewFlipper;
    SharedPreferences settings;
	//

    //Shashank wrote these lines of code with Marc present
    public static final String OPENWORDS_PREFERENCES = "OpenwordsPrefs";
    public static final String PLATE_POSITION = "PlatePosition";
	//Shashank - Creating an instance variable to hold plate position
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_evaluate);
		//
		settings = getSharedPreferences(OPENWORDS_PREFERENCES, 0);
	    mViewFlipper = (ViewFlipper) findViewById(R.id.selfEvaluate_ViewFlipper_frame);
	    mViewFlipper.setDisplayedChild(0);
	    initAnimations();
	    // Marc 2013-09-15 I added final to the line here.
	    final int startingQuestionNumber = settings.getInt(PLATE_POSITION, 1);
	    //final int nextQuestionNumber = settings.getInt(PLATE_POSITION, 1);
	    //
	    
	    

	    
	    //I AM TRYING TO PULL STUFF FROM THE db 2013-09-16 
	    // this is the exact (almost) code that is present in MainActivity.java
	    // this crashed the program if it was above onCreate above.
		PlateDbHelper myretrieverObject = new PlateDbHelper(this);
		SQLiteDatabase db = myretrieverObject.getWritableDatabase();
	    //Cursor c = db.query(distinct, Plate_db, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
        //Cursor c = db.getAllRows();
        //public String getData() {
		//	
		//}
		

		
		//TESTING THE TOAST THING
		Toast.makeText(this, "This is my Toast!", Toast.LENGTH_LONG).show();
		//

		//TESTING THE TOAST THING
		Toast.makeText(this, "I'm putting toast everywhere now", Toast.LENGTH_LONG).show();
		//
		
		
//Marc - SHOW ANSWER BUTTON
		Button showAnswer = (Button) findViewById(R.id.selfEvaluate_activity_button_showAnswer);
        // Marc 2013-09-15 I added final to the line here.
		final TextView answer1 = (TextView) findViewById(R.id.selfEvaluate_activity_textView_answer);
		showAnswer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//TODO Auto-generated method stub
                //REPORT PLATE POSITION FOR A TEST
				//2103-09-15: By placing a new variable in the 
			    final int currentQuestionNumber = settings.getInt(PLATE_POSITION, 1);
				answer1.setText(String.valueOf(currentQuestionNumber));
				//THIS WILL BE USED WHEN I NEED TO CHANGE THE ANSWER TO READ FROM
				//THE plate_db
				//answer1.setText("oy");
			}
		});
//END SHOW ANSWER BUTTON

//Marc - GOING TO TRY TO MAKE AN ON PRESS LISTENER HERE

//


//Marc - KNOW IMAGEVIEW CLICK LISTENER
				final ImageView know = (ImageView) findViewById(R.id.self_evaluate_activity_imageView_know);
                final ImageView didntknow = (ImageView) findViewById(R.id.self_evaluate_activity_imageView_didntKnow);
                know.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					//TODO Auto-generated method stub
						know.setImageResource(R.drawable.button_self_evaluate_correct_selected);
//CURRENTLY CANNOT GET THIS LINE TO WORK
//HAD TO SPECIFY A DIFFERENT HANDLER "didntknow2" AND DECLARE IT ABOVE
						didntknow.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
						
		                // THE PURPOSE OF THIS LINE IS TO TEST A CHANGE IN TEXT IN RESPONSE TO A VIEWFLIPPER
		                final TextView textView4 = (TextView) findViewById(R.id.selfEvaluate_activity_textView_problem);
						textView4.setText("諃铡盏榨謤榨斩");
		                final TextView textView5 = (TextView) findViewById(R.id.selfEvaluate_activity_textView_answer);
						textView5.setText("");
						
		            	//Marc 2013-09-15 ADDING LINES TO ADVANCE THE PLATE
		                mViewFlipper.setInAnimation(mInFromRight);
		                mViewFlipper.setOutAnimation(mOutToLeft);
		                mViewFlipper.showNext();
		                // HERE WE WILL INCREMENT UP THE PLATE POSITION
		                int nextQuestionNumber = settings.getInt(PLATE_POSITION, 0)+1;
		                Editor editor = settings.edit();
		                editor.putInt(PLATE_POSITION, nextQuestionNumber);
		                editor.commit();
		            	//
		                
					}
				});
//END KNOW IMAGEVIEW CLICK LISTENER
//Marc - DIDNTKNOW IMAGEVIEW CLICK LISTENER
                //Line below was a redundant declaration
				//final ImageView didntknow1 = (ImageView) findViewById(R.id.self_evaluate_activity_imageView_didntKnow);
                //YES LINE ABOVE, I HAD TO LOOK IT UP AGAIN ABOVE
				final TextView textView3 = (TextView) findViewById(R.id.selfEvaluate_activity_textView_problem);
                //Line below was a redundant declaration
				//final TextView textView4 = (TextView) findViewById(R.id.self_evaluate_activity_textView_answer);
                //YES, THE ABOVE TWO LINES ARE RELOOKUPS
				didntknow.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					//TODO Auto-generated method stub
						know.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
						didntknow.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);						
                        //THESE TWO LINES BELOW REPRESENT "AN ADVANCE" TO THE NEXT SLIDE
						textView3.setText("鎴戝�");
		            	answer1.setText("");
		            	
		            	//Marc 2013-09-15 ADDING LINES TO ADVANCE THE PLATE
		                mViewFlipper.setInAnimation(mInFromRight);
		                mViewFlipper.setOutAnimation(mOutToLeft);
		                mViewFlipper.showNext();
		                // HERE WE WILL INCREMENT UP THE PLATE POSITION
		                int nextQuestionNumber = settings.getInt(PLATE_POSITION, 0)+1;
		                Editor editor = settings.edit();
		                editor.putInt(PLATE_POSITION, nextQuestionNumber);
		                editor.commit();
		            	//
		                
		                //Marc 2013-09-15 I added This line. This puts the new question 
		            	//number in a variable, so that it can be pulled up for 
		            	//use in TESTING
		            	//int questionNumber = settings.getInt(PLATE_POSITION, 1);
		            	//
					}
				});
//END DIDNTKNOW IMAGEVIEW CLICK LISTENER
	}






// HERE IS THE ADDITION OF THE ANIMATION
    private void initAnimations() {
        mInFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mInFromRight.setDuration(500);//originally 500, then 100, back to 500
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
        mInFromRight.setInterpolator(accelerateInterpolator);

        mInFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mInFromLeft.setDuration(100);//500
        mInFromLeft.setInterpolator(accelerateInterpolator);

        mOutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mOutToRight.setDuration(100);//500
        mOutToRight.setInterpolator(accelerateInterpolator);

        mOutToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mOutToLeft.setDuration(500);//originally 500, then 100, back to 500
        mOutToLeft.setInterpolator(accelerateInterpolator);

        final GestureDetector gestureDetector;
        // WE NEED TO FIGURE THIS OUT
        gestureDetector = new GestureDetector(new MyGestureDetector());

        mViewFlipper.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                } else {
                    return true;
                }
            }
        });
    }

    private class MyGestureDetector extends SimpleOnGestureListener {

        //Marc - Here I will try to make a textView that will change, and I will place it below
        // THE PURPOSE OF THIS LINE IS TO TEST A CHANGE IN TEXT IN RESPONSE TO A VIEWFLIPPER
            final public TextView textView1 = (TextView) findViewById(R.id.selfEvaluate_activity_textView_problem);
            final public TextView textView2 = (TextView) findViewById(R.id.selfEvaluate_activity_textView_answer);
    	
        private static final int SWIPE_MIN_DISTANCE = 60; //120
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100; //200

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            System.out.println(" in onFling() :: ");
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(mInFromRight);
                mViewFlipper.setOutAnimation(mOutToLeft);
                mViewFlipper.showNext();
                // HERE WE WILL INCREMENT UP THE PLATE POSITION
                // I PLACED AN IF LOOP (2013-09-15) THAT WILL REDUCE THE PLATE
                // POSITION IF IT GETS ABOVE 16.
                int nextQuestionNumber = settings.getInt(PLATE_POSITION, 0)+1;
                if (nextQuestionNumber < 16) {
                Editor editor = settings.edit();
                editor.putInt(PLATE_POSITION, nextQuestionNumber);
                editor.commit();
                } else {
                	Editor editor2 = settings.edit();
                	editor2.putInt(PLATE_POSITION, 1);
                    editor2.commit();
                }
                //
                
                // THE PURPOSE OF THIS LINE IS TO TEST A CHANGE IN TEXT IN RESPONSE TO A VIEWFLIPPER
            	textView1.setText("賵鬲賴");
            	textView2.setText("");
            	//THE PURPOSE OF THIS LINE IS TO TEST WRITING THE POSITION TO THE SHARED PREFERENCES
            	
            	//
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(mInFromLeft);
                mViewFlipper.setOutAnimation(mOutToRight);
                mViewFlipper.showPrevious();
                // HERE WE WILL INCREMENT DOWN THE PLATE POSITION
            	textView1.setText("諃铡盏榨謤榨斩");
            	textView2.setText("");
            	// scrap 缇庡湅
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

}
