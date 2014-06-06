package com.openwords.view.learningModule;

//Marc - Lines related to the slide activity are between "//" and "//"

//THESE LINES APPEAR TO BE HAVE BEEN AUTOMATICALLY GENERATED TO FIND com.openwords_alpha_04.R, but they do not appear necessary 
//import com.openwords_alpha_05.R;
//import com.openwords_alpha_05.SlideActivity.MyGestureDetector;
//import com.openwords_alpha_04.R;

import java.util.*;

import com.openwords.R;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.util.PlateDbHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
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

public class SelfEvaluate extends Activity {
	private Animation mInFromRight;
	private Animation mOutToLeft;
	private Animation mInFromLeft;
	private Animation mOutToRight;
	private ViewFlipper mViewFlipper;
	//private SharedPreferences settings;
	//private Editor editor;
	public static final String OPENWORDS_PREFERENCES = "OpenwordsPrefs";
	public static final String PLATE_POSITION = "PlatePosition";
	private LinkedList<LeafCardSelfEval> questionPool = new LinkedList<LeafCardSelfEval>();
	private Integer questionIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		questionPool.add(new LeafCardSelfEval("人","man", "ren"));
		questionPool.add(new LeafCardSelfEval("猫","cat", "mao"));
		questionPool.add(new LeafCardSelfEval("地球","earth", "di qiu"));
		questionPool.add(new LeafCardSelfEval("时间","time", "shi jian"));
		questionPool.add(new LeafCardSelfEval("世界","world", "shi jie"));
		questionPool.add(new LeafCardSelfEval("电脑","computer", "dian nao"));
		questionPool.add(new LeafCardSelfEval("软件","software", "ruan jian"));
//		Log.e("Question",questionPool.get(0).getWordLang2());
//		Log.e("Answer",questionPool.get(0).getWordLang1());

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_evaluate);
		//
//		settings = getSharedPreferences(OPENWORDS_PREFERENCES, 0);
//		editor = settings.edit();
//		editor.putInt(PLATE_POSITION, 1);
//		editor.commit();
		mViewFlipper = (ViewFlipper) findViewById(R.id.selfEvaluate_ViewFlipper_frame);
		mViewFlipper.setDisplayedChild(0);
		initAnimations();
		//final int startingQuestionNumber = settings.(PLATE_POSITION, 1);

		PlateDbHelper myretrieverObject = new PlateDbHelper(this);
		SQLiteDatabase db = myretrieverObject.getWritableDatabase();
		// Cursor c = db.query(distinct, Plate_db, columns, selection,
		// selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
		// Cursor c = db.getAllRows();
		// public String getData() {
		//
		// }

		// TESTING THE TOAST THING
		// Toast.makeText(this, "This is my Toast!", Toast.LENGTH_LONG).show();

		Button showAnswer = (Button) findViewById(R.id.selfEvaluate_Button_showAnswer);
		
		final TextView question = (TextView) findViewById(R.id.selfEvaluate_TextView_question);
		final TextView answer = (TextView) findViewById(R.id.selfEvaluate_TextView_answer);
		final TextView transcription = (TextView) findViewById(R.id.selfEvaluate_TextView_transcription);
		question.setText(questionPool.get(0).getWordLang2());
		answer.setText(questionPool.get(0).getWordLang1());
		answer.setVisibility(View.INVISIBLE);
		transcription.setText(questionPool.get(0).getTranscription());

		showAnswer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				answer.setVisibility(View.VISIBLE);
				transcription.setVisibility(View.VISIBLE);
			}
		});
		final ImageView known = (ImageView) findViewById(R.id.selfEvaluate_ImageView_known);
		final ImageView unknown = (ImageView) findViewById(R.id.selfEvaluate_ImageView_unknown);
		known.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				questionPool.get(questionIndex).setUserChoice(true);
//				known.setImageResource(R.drawable.button_self_evaluate_correct_selected);
//				unknown.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
				moveForward();
			}
		});
		
		unknown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				questionPool.get(questionIndex).setUserChoice(false);;
//				known.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
//				unknown.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
				moveForward();
			}
		});		
	}
	private void setButtonsView(Boolean userChoice) {
		final ImageView known = (ImageView) findViewById(R.id.selfEvaluate_ImageView_known);
		final ImageView unknown = (ImageView) findViewById(R.id.selfEvaluate_ImageView_unknown);
		if(userChoice==null) {
			known.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
			unknown.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
		} else if (userChoice) {
			known.setImageResource(R.drawable.button_self_evaluate_correct_selected);
			unknown.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
		} else {
			known.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
			unknown.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
		}
	}
	
	private void moveForward() {
		questionIndex++;
		//Log.e("Index",Integer.toString(index));
		final TextView question = (TextView) findViewById(R.id.selfEvaluate_TextView_question);
		final TextView answer = (TextView) findViewById(R.id.selfEvaluate_TextView_answer);
		final TextView transcription = (TextView) findViewById(R.id.selfEvaluate_TextView_transcription);

		mViewFlipper.setInAnimation(mInFromRight);
		mViewFlipper.setOutAnimation(mOutToLeft);
		mViewFlipper.showNext();
		
		if(questionIndex>=questionPool.size()) {
			Toast.makeText(SelfEvaluate.this, "You have arrived the last", Toast.LENGTH_LONG).show();
			questionIndex = questionPool.size()-1;
		} else {
			setButtonsView(questionPool.get(questionIndex).getUserChoice());
			question.setText(questionPool.get(questionIndex).getWordLang2());
			answer.setText(questionPool.get(questionIndex).getWordLang1());
			answer.setVisibility(View.INVISIBLE);
			transcription.setText(questionPool.get(questionIndex).getTranscription());
		}
	}
	
	private void moveBackward(){
		questionIndex--;
		final TextView question = (TextView) findViewById(R.id.selfEvaluate_TextView_question);
		final TextView answer = (TextView) findViewById(R.id.selfEvaluate_TextView_answer);
		final TextView transcription = (TextView) findViewById(R.id.selfEvaluate_TextView_transcription);
		mViewFlipper.setInAnimation(mInFromLeft);
		mViewFlipper.setOutAnimation(mOutToRight);
		mViewFlipper.showPrevious();
		if (questionIndex<0) {
			Toast.makeText(SelfEvaluate.this, "You have arrived the last", Toast.LENGTH_LONG).show();
			questionIndex = 0;
		} else {
			setButtonsView(questionPool.get(questionIndex).getUserChoice());
			question.setText(questionPool.get(questionIndex).getWordLang2());
			answer.setText(questionPool.get(questionIndex).getWordLang1());
			answer.setVisibility(View.INVISIBLE);
			transcription.setText(questionPool.get(questionIndex).getTranscription());
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
