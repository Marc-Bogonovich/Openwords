package com.openwords.view.learningModule;

import java.util.LinkedList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.openwords.R;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.util.WordComparsion;


public class TypeEvaluate extends Activity {
	private Animation mInFromRight;
	private Animation mOutToLeft;
	private Animation mInFromLeft;
	private Animation mOutToRight;
	private ViewFlipper mViewFlipper;
	private LinkedList<LeafCardSelfEval> questionPool = new LinkedList<LeafCardSelfEval>();
	private Integer questionIndex = 0;
	private EditText userInput;
	private TextView answer;
	private TextView question;
	private TextView transcription;
	private ImageView checkButton;
	private ImageView status;
	private final double CUTOFF = 0.75d;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_evaluate);
		questionPool.add(new LeafCardSelfEval("人","man", "ren"));
		questionPool.add(new LeafCardSelfEval("猫","cat", "mao"));
		questionPool.add(new LeafCardSelfEval("地球","earth", "di qiu"));
		questionPool.add(new LeafCardSelfEval("时间","time", "shi jian"));
		questionPool.add(new LeafCardSelfEval("世界","world", "shi jie"));
		questionPool.add(new LeafCardSelfEval("电脑","computer", "dian nao"));
		questionPool.add(new LeafCardSelfEval("软件","software", "ruan jian"));
		mViewFlipper = (ViewFlipper) findViewById(R.id.typeEvaluate_ViewFlipper_frame);
		mViewFlipper.setDisplayedChild(0);
		initAnimations();
		answer = (TextView) findViewById(R.id.typeEvaluate_TextView_answer);
		question = (TextView) findViewById(R.id.typeEvaluate_TextView_question);
		transcription = (TextView) findViewById(R.id.typeEvaluate_TextView_transcription);
		userInput = (EditText) findViewById(R.id.typeEvaluate_EditText_input);
		checkButton = (ImageView) findViewById(R.id.typeEvaluate_ImageView_checkButton);
		prepareQuestion(questionIndex);
		
		checkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Boolean userChoice = null;
				status = (ImageView) findViewById(R.id.typeEvaluate_ImageView_status);
				
				if(userInput!=null) {
					String userInputString = userInput.getText().toString();
					double similarity = WordComparsion.similarity(userInputString, questionPool.get(questionIndex).getWordLang1());
					if(userInputString.equals(questionPool.get(questionIndex).getWordLang1())) {
						status.setImageResource(R.drawable.ic_learning_module_correct);
						userChoice = true;
					} else if(similarity>=CUTOFF) {
						status.setImageResource(R.drawable.ic_learning_module_close);
						userChoice = false;
						answer.setVisibility(View.VISIBLE);
						//if want the status icon becomes null when move forward/backward, change the value of userChoice
					} else {
						status.setImageResource(R.drawable.ic_learning_module_incorrect);
						userChoice = false;
						answer.setVisibility(View.VISIBLE);
					}
				} else {
					status.setImageResource(R.drawable.ic_learning_module_incorrect);
					userChoice = false;
					answer.setVisibility(View.VISIBLE);
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
    private void setButtonsView(Boolean userChoice) {
		if(userChoice==null) {
			status.setImageResource(R.drawable.ic_learning_module_null);
		} else if (userChoice) {
			status.setImageResource(R.drawable.ic_learning_module_correct);
		} else {
			status.setImageResource(R.drawable.ic_learning_module_incorrect);
		}
	}
    private void prepareQuestion(Integer questionIndex) {
		question.setText(questionPool.get(questionIndex).getWordLang2());
		answer.setText(questionPool.get(questionIndex).getWordLang1());
		answer.setVisibility(View.INVISIBLE);
		transcription.setText(questionPool.get(questionIndex).getTranscription());
		userInput.setText("");
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
		mViewFlipper.setInAnimation(mInFromRight);
		mViewFlipper.setOutAnimation(mOutToLeft);
		mViewFlipper.showNext();
		setButtonsView(questionPool.get(questionIndex).getUserChoice());
		if(questionIndex>=questionPool.size()) {
			Toast.makeText(TypeEvaluate.this, "You have arrived the last", Toast.LENGTH_LONG).show();
			questionIndex = questionPool.size()-1;
		} else {
			prepareQuestion(questionIndex);
		}
	}
	
	private void moveBackward(){
		questionIndex--;
		mViewFlipper.setInAnimation(mInFromLeft);
		mViewFlipper.setOutAnimation(mOutToRight);
		mViewFlipper.showPrevious();
		setButtonsView(questionPool.get(questionIndex).getUserChoice());
		if (questionIndex<0) {
			Toast.makeText(TypeEvaluate.this, "You have arrived the last", Toast.LENGTH_LONG).show();
			questionIndex = 0;
		} else {
			prepareQuestion(questionIndex);
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

				//
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				moveBackward();
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}
