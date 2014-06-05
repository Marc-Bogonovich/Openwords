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
	SharedPreferences settings;
	Editor editor;
	public static final String OPENWORDS_PREFERENCES = "OpenwordsPrefs";
	public static final String PLATE_POSITION = "PlatePosition";
	private LinkedList<LeafCardSelfEval> questionPool = new LinkedList<LeafCardSelfEval>();

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
		settings = getSharedPreferences(OPENWORDS_PREFERENCES, 0);
		editor = settings.edit();
		editor.putInt(PLATE_POSITION, 1);
		editor.commit();
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
		
		
		final TextView answer = (TextView) findViewById(R.id.selfEvaluate_TextView_answer);
		showAnswer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final int currentQuestionNumber = settings.getInt(
						PLATE_POSITION, 1);
				answer.setText(String.valueOf(currentQuestionNumber));
			}
		});
		final ImageView known = (ImageView) findViewById(R.id.selfEvaluate_ImageView_known);
		final ImageView unknown = (ImageView) findViewById(R.id.selfEvaluate_ImageView_unknown);
		known.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				known.setImageResource(R.drawable.button_self_evaluate_correct_selected);
				unknown.setImageResource(R.drawable.button_self_evaluate_incorrect_unselected);
				moveForward();
			}
		});
		
		unknown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				known.setImageResource(R.drawable.button_self_evaluate_correct_unselected);
				unknown.setImageResource(R.drawable.button_self_evaluate_incorrect_selected);
				moveForward();
			}
		});		
	}
	
	private void moveForward() {
		final TextView question = (TextView) findViewById(R.id.selfEvaluate_TextView_question);
		final TextView answer = (TextView) findViewById(R.id.selfEvaluate_TextView_answer);


		// Marc 2013-09-15 ADDING LINES TO ADVANCE THE PLATE
		mViewFlipper.setInAnimation(mInFromRight);
		mViewFlipper.setOutAnimation(mOutToLeft);
		mViewFlipper.showNext();
		// HERE WE WILL INCREMENT UP THE PLATE POSITION
		int nextQuestionNumber = settings.getInt(PLATE_POSITION, 0) + 1;
		Log.e("question pool",Integer.toString(questionPool.size()));
		Log.e("next index",Integer.toString(nextQuestionNumber));
		if (nextQuestionNumber < questionPool.size()) {
			editor.putInt(PLATE_POSITION, nextQuestionNumber);
			editor.commit();
		} else {
			nextQuestionNumber = 0;
			editor.putInt(PLATE_POSITION, 1);
			editor.commit();
		}
		question.setText(questionPool.get(nextQuestionNumber).getWordLang2());
		answer.setText(questionPool.get(nextQuestionNumber).getWordLang1());
		Editor editor = settings.edit();
		editor.putInt(PLATE_POSITION, nextQuestionNumber);
		editor.commit();
	}
	
	private void moveBackward(){
		final TextView question = (TextView) findViewById(R.id.selfEvaluate_TextView_question);
		final TextView answer = (TextView) findViewById(R.id.selfEvaluate_TextView_answer);

		mViewFlipper.setInAnimation(mInFromLeft);
		mViewFlipper.setOutAnimation(mOutToRight);
		mViewFlipper.showPrevious();
		// HERE WE WILL INCREMENT DOWN THE PLATE POSITION
		int prevQuestionNumber = settings.getInt(PLATE_POSITION, 0) - 1;
		if (prevQuestionNumber >= 0) {
			editor.putInt(PLATE_POSITION, prevQuestionNumber);
			editor.commit();
		} else {
			prevQuestionNumber = 0;
			editor.putInt(PLATE_POSITION, 1);
			editor.commit();
		}
		question.setText(questionPool.get(prevQuestionNumber).getWordLang2());
		answer.setText(questionPool.get(prevQuestionNumber).getWordLang1());
	}

	private void initAnimations() {
		mInFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				+1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mInFromRight.setDuration(500);// originally 500, then 100, back to 500
		AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
		mInFromRight.setInterpolator(accelerateInterpolator);

		mInFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				-1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mInFromLeft.setDuration(100);// 500
		mInFromLeft.setInterpolator(accelerateInterpolator);

		mOutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
				0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mOutToRight.setDuration(100);// 500
		mOutToRight.setInterpolator(accelerateInterpolator);

		mOutToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		mOutToLeft.setDuration(500);// originally 500, then 100, back to 500
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
	
	private TextView moveFilpper(int currentPos, int direct) {
		//It should return the previous and next text view
		
		return null;
	}
	

	private class MyGestureDetector extends SimpleOnGestureListener {

		// Marc - Here I will try to make a textView that will change, and I
		// will place it below
		// THE PURPOSE OF THIS LINE IS TO TEST A CHANGE IN TEXT IN RESPONSE TO A
		// VIEWFLIPPER
		final public TextView question = (TextView) findViewById(R.id.selfEvaluate_TextView_question);
		final public TextView answer = (TextView) findViewById(R.id.selfEvaluate_TextView_answer);

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
				// scrap 缇庡湅
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}
