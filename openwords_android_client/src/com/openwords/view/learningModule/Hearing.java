package com.openwords.view.learningModule;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.openwords.R;
import com.openwords.model.LeafCardHearing;
import com.openwords.util.WordComparsion;

public class Hearing extends Activity {
	private Animation mInFromRight;
	private Animation mOutToLeft;
	private Animation mInFromLeft;
	private Animation mOutToRight;
	private ViewFlipper mViewFlipper;
	private LinkedList<LeafCardHearing> questionPool = new LinkedList<LeafCardHearing>();
	private Integer questionIndex = 0;
	private EditText userInput;
	private TextView answer;
	private TextView question;
	private TextView transcription;
	private ImageView checkButton;
	private ImageView indicator;
	private ImageView audioPlayButton;
	private final double CUTOFF = 0.75f;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearing);
		questionPool.add(new LeafCardHearing("人","man", "ren"));
		questionPool.add(new LeafCardHearing("猫","cat", "mao"));
		questionPool.add(new LeafCardHearing("地球","earth", "di qiu"));
		questionPool.add(new LeafCardHearing("时间","time", "shi jian"));
		questionPool.add(new LeafCardHearing("世界","world", "shi jie"));
		questionPool.add(new LeafCardHearing("电脑","computer", "dian nao"));
		questionPool.add(new LeafCardHearing("软件","software", "ruan jian"));
		mViewFlipper = (ViewFlipper) findViewById(R.id.hearing_ViewFlipper_frame);
		mViewFlipper.setDisplayedChild(0);
		initAnimations();
		answer = (TextView) findViewById(R.id.hearing_TextView_answer);
		question = (TextView) findViewById(R.id.hearing_TextView_question);
		transcription = (TextView) findViewById(R.id.hearing_TextView_transcription);
		userInput = (EditText) findViewById(R.id.hearing_EditText_input);
		checkButton = (ImageView) findViewById(R.id.hearing_ImageView_checkButton);
		indicator = (ImageView) findViewById(R.id.hearing_ImageView_indicator);
		audioPlayButton = (ImageView) findViewById(R.id.hearing_ImageView_audioPlay);
		//prepareQuestion(questionIndex);
		setInterfaceView();
		
		checkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer userChoice = 0;
				
				if(userInput!=null) {
					//user choice 0--null 1--wrong 2--close 3--correct
					String userInputString = userInput.getText().toString().toLowerCase();
					double similarity = Math.max(WordComparsion.similarity(userInputString, questionPool.get(questionIndex).getWordLang1()),
							WordComparsion.similarity(userInputString, questionPool.get(questionIndex).getWordLang2()));
					if(userInputString.equals(questionPool.get(questionIndex).getWordLang1()) || 
							userInputString.equals(questionPool.get(questionIndex).getWordLang2())) {
						indicator.setImageResource(R.drawable.ic_learning_module_correct);
						questionPool.get(questionIndex).setUserInput(userInputString);
						userChoice = 3;
						question.setVisibility(View.VISIBLE);
						answer.setVisibility(View.VISIBLE);
						transcription.setVisibility(View.VISIBLE);
					} else if(similarity>=CUTOFF) {
						indicator.setImageResource(R.drawable.ic_learning_module_close);
						userChoice = 2;
						questionPool.get(questionIndex).setUserInput(userInputString);
						answer.setVisibility(View.VISIBLE);
						question.setVisibility(View.VISIBLE);
						transcription.setVisibility(View.VISIBLE);
						//if want the status icon becomes null when move forward/backward, change the value of userChoice
					} else {
						indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
						userChoice = 1;
						questionPool.get(questionIndex).setUserInput(userInputString);
						answer.setVisibility(View.VISIBLE);
						question.setVisibility(View.VISIBLE);
						transcription.setVisibility(View.VISIBLE);
					}
				} else {
					indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
					userChoice = 0;
					answer.setVisibility(View.VISIBLE);
					question.setVisibility(View.VISIBLE);
					transcription.setVisibility(View.VISIBLE);
				}
				questionPool.get(questionIndex).setUserChoice(userChoice);
				
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {
		            public void run() {
		            	moveForward();
		            }
		        }, 3000);
				
			}
		});
    }
    private void setInterfaceView() {
    	Integer userChoice = questionPool.get(questionIndex).getUserChoice();
    	transcription.setText(questionPool.get(questionIndex).getTranscription());
    	question.setText(questionPool.get(questionIndex).getWordLang2());
    	answer.setText(questionPool.get(questionIndex).getWordLang1());
    	// 0 -- null 1-- wrong 2-- close 3--correct
    	if(userChoice.equals(0)) {
			indicator.setImageResource(R.drawable.ic_learning_module_null);
			userInput.setText("");
			transcription.setVisibility(View.INVISIBLE);
			answer.setVisibility(View.INVISIBLE);
			question.setVisibility(View.INVISIBLE);
		} else if (userChoice.equals(3)) {
			indicator.setImageResource(R.drawable.ic_learning_module_correct);
			userInput.setText(questionPool.get(questionIndex).getUserInput());	
			answer.setVisibility(View.VISIBLE);
			transcription.setVisibility(View.VISIBLE);
			question.setVisibility(View.VISIBLE);
		} else if (userChoice.equals(2)) {
			indicator.setImageResource(R.drawable.ic_learning_module_close);
			userInput.setText(questionPool.get(questionIndex).getUserInput());
			answer.setVisibility(View.VISIBLE);
			transcription.setVisibility(View.VISIBLE);
			question.setVisibility(View.VISIBLE);
		} else {
			indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
			userInput.setText(questionPool.get(questionIndex).getUserInput());
			answer.setVisibility(View.VISIBLE);
			question.setVisibility(View.VISIBLE);
			transcription.setVisibility(View.VISIBLE);
		}
		
	}
    
    private void initAnimations() {
		mInFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				+1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mInFromRight.setDuration(500);
		AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
		mInFromRight.setInterpolator(accelerateInterpolator);

		mInFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				-1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mInFromLeft.setDuration(500);
		mInFromLeft.setInterpolator(accelerateInterpolator);

		mOutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mOutToRight.setDuration(500);
		mOutToRight.setInterpolator(accelerateInterpolator);

		mOutToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mOutToLeft.setDuration(500);
		mOutToLeft.setInterpolator(accelerateInterpolator);

		final GestureDetector gestureDetector;
		// WE NEED TO FIGURE THIS OUT
		gestureDetector = new GestureDetector(new MyGestureDetector());

		mViewFlipper.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return false;
				} else {
					return true;
				}
			}
		});
	}
 
    private void moveForward() {
		questionIndex++;
		if(questionIndex>=questionPool.size()) {
			Toast.makeText(Hearing.this, "You have arrived the last", Toast.LENGTH_SHORT).show();
			questionIndex = questionPool.size()-1;
		} else {
			mViewFlipper.setInAnimation(mInFromRight);
			mViewFlipper.setOutAnimation(mOutToLeft);
			mViewFlipper.showNext();
			setInterfaceView();
			//prepareQuestion(questionIndex);
		}
	}
	
	private void moveBackward(){
		questionIndex--;
		if (questionIndex<0) {
			Toast.makeText(Hearing.this, "You have arrived the last", Toast.LENGTH_SHORT).show();
			questionIndex = 0;
		} else {
			mViewFlipper.setInAnimation(mInFromLeft);
			mViewFlipper.setOutAnimation(mOutToRight);
			mViewFlipper.showPrevious();
			setInterfaceView();
			//prepareQuestion(questionIndex);
		}
	}
	private class MyGestureDetector extends SimpleOnGestureListener {

		// Marc - Here I will try to make a textView that will change, and I
		// will place it below
		// THE PURPOSE OF THIS LINE IS TO TEST A CHANGE IN TEXT IN RESPONSE TO A
		// VIEWFLIPPER
		private static final int SWIPE_MIN_DISTANCE = 60; // 120
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 100; // 200

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				 moveForward();
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				moveBackward();
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}

