package com.openwords.view.learningModule;

import android.app.Activity;
import android.os.Bundle;
import com.openwords.R;

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

public class Review extends Activity {
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
	private Integer questioIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		questionPool.add(new LeafCardSelfEval("äºº","man", "ren"));
		questionPool.add(new LeafCardSelfEval("çŒ«","cat", "mao"));
		questionPool.add(new LeafCardSelfEval("åœ°ç�ƒ","earth", "di qiu"));
		questionPool.add(new LeafCardSelfEval("æ—¶é—´","time", "shi jian"));
		questionPool.add(new LeafCardSelfEval("ä¸–ç•Œ","world", "shi jie"));
		questionPool.add(new LeafCardSelfEval("ç”µè„‘","computer", "dian nao"));
		questionPool.add(new LeafCardSelfEval("è½¯ä»¶","software", "ruan jian"));
//		Log.e("Question",questionPool.get(0).getWordLang2());
//		Log.e("Answer",questionPool.get(0).getWordLang1());

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);
		//
//		settings = getSharedPreferences(OPENWORDS_PREFERENCES, 0);
//		editor = settings.edit();
//		editor.putInt(PLATE_POSITION, 1);
//		editor.commit();
		mViewFlipper = (ViewFlipper) findViewById(R.id.review_ViewFlipper_frame);
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

	
		
		final TextView question = (TextView) findViewById(R.id.review_TextView_question);
		final TextView answer = (TextView) findViewById(R.id.review_TextView_answer);
		final TextView transcription = (TextView) findViewById(R.id.review_TextView_transcription);
		question.setText(questionPool.get(0).getWordLang2());
		question.setVisibility(View.VISIBLE);
		answer.setText(questionPool.get(0).getWordLang1());
		answer.setVisibility(View.VISIBLE);
		transcription.setText(questionPool.get(0).getTranscription());
		transcription.setVisibility(View.VISIBLE);
	}

	private void moveForward() {
		questioIndex++;
		//Log.e("Index",Integer.toString(index));
		final TextView question = (TextView) findViewById(R.id.review_TextView_question);
		final TextView answer = (TextView) findViewById(R.id.review_TextView_answer);
		final TextView transcription = (TextView) findViewById(R.id.review_TextView_transcription);


		mViewFlipper.setInAnimation(mInFromRight);
		mViewFlipper.setOutAnimation(mOutToLeft);
		mViewFlipper.showNext();

		if(questioIndex>=questionPool.size()) {
			Toast.makeText(Review.this, "You have arrived the last", Toast.LENGTH_LONG).show();
			questioIndex = questionPool.size()-1;
		} else {	
			question.setText(questionPool.get(questioIndex).getWordLang2());
			question.setVisibility(View.VISIBLE);
			answer.setText(questionPool.get(questioIndex).getWordLang1());
			answer.setVisibility(View.VISIBLE);
			transcription.setText(questionPool.get(questioIndex).getTranscription());
			transcription.setVisibility(View.VISIBLE);
		}
	}
	
	private void moveBackward(){
		questioIndex--;
		final TextView question = (TextView) findViewById(R.id.review_TextView_question);
		final TextView answer = (TextView) findViewById(R.id.review_TextView_answer);
		final TextView transcription = (TextView) findViewById(R.id.review_TextView_transcription);
		mViewFlipper.setInAnimation(mInFromLeft);
		mViewFlipper.setOutAnimation(mOutToRight);
		mViewFlipper.showPrevious();
		if (questioIndex<0) {
			Toast.makeText(Review.this, "You have arrived the last", Toast.LENGTH_LONG).show();
			questioIndex = 0;
		} else {
			question.setText(questionPool.get(questioIndex).getWordLang2());
			question.setVisibility(View.VISIBLE);
			answer.setText(questionPool.get(questioIndex).getWordLang1());
			answer.setVisibility(View.VISIBLE);
			transcription.setText(questionPool.get(questioIndex).getTranscription());
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
	

	private class MyGestureDetector extends SimpleOnGestureListener {

		// Marc - Here I will try to make a textView that will change, and I
		// will place it below
		// THE PURPOSE OF THIS LINE IS TO TEST A CHANGE IN TEXT IN RESPONSE TO A
		// VIEWFLIPPER
		private static final int SWIPE_MIN_DISTANCE = 60; // 120
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200; // 200

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
