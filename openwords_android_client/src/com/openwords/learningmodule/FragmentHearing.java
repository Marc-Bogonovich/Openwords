package com.openwords.learningmodule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.openwords.R;
import com.openwords.model.LeafCardHearing;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.tts.Speak;
import com.openwords.util.TimeConvertor;
import com.openwords.util.WordComparsion;
import com.openwords.util.log.LogUtil;

@SuppressLint("ValidFragment")
public class FragmentHearing extends Fragment {

    private final int cardIndex;
    private TextView question, transcription, answer;
    private ImageView checkButton, indicator, audioPlayButton;
    private EditText userInput;
	private final double CUTOFF = 0.75f;
    private LeafCardHearing card;
    @SuppressLint("ValidFragment")
	public FragmentHearing(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate for card: " + cardIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreateView for card: " + cardIndex);

        View myFragmentView = inflater.inflate(R.layout.fragment_hearing, container, false);
        card = ActivityHearing.getCardsPool().get(this.cardIndex);

        answer = (TextView) myFragmentView.findViewById(R.id.hearing_TextView_answer);
		question = (TextView) myFragmentView.findViewById(R.id.hearing_TextView_question);
		transcription = (TextView) myFragmentView.findViewById(R.id.hearing_TextView_transcription);
		userInput = (EditText) myFragmentView.findViewById(R.id.hearing_EditText_input);
		checkButton = (ImageView) myFragmentView.findViewById(R.id.hearing_ImageView_checkButton);
		indicator = (ImageView) myFragmentView.findViewById(R.id.hearing_ImageView_indicator);
		audioPlayButton = (ImageView) myFragmentView.findViewById(R.id.hearing_ImageView_audioPlay);
		setInterfaceView(); 
		checkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer userChoice = 0;
				
				if(userInput!=null) {
					//user choice 0--null 1--wrong 2--close 3--correct
					String userInputString = userInput.getText().toString().toLowerCase();
					double similarity = Math.max(WordComparsion.similarity(userInputString, card.getWordLang1()),
							WordComparsion.similarity(userInputString, card.getWordLang2()));
					if(userInputString.equals(card.getWordLang1()) || 
							userInputString.equals(card.getWordLang2())) {
						indicator.setImageResource(R.drawable.ic_learning_module_correct);
						card.setUserInput(userInputString);
						userChoice = 3;
						question.setVisibility(View.VISIBLE);
						answer.setVisibility(View.VISIBLE);
						transcription.setVisibility(View.VISIBLE);
					} else if(similarity>=CUTOFF) {
						indicator.setImageResource(R.drawable.ic_learning_module_close);
						userChoice = 2;
						card.setUserInput(userInputString);
						answer.setVisibility(View.VISIBLE);
						question.setVisibility(View.VISIBLE);
						transcription.setVisibility(View.VISIBLE);
						//if want the status icon becomes null when move forward/backward, change the value of userChoice
					} else {
						indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
						userChoice = 1;
						card.setUserInput(userInputString);
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
				card.setUserChoice(userChoice);
				
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {
		            public void run() {
		            	 ActivityHearing.getInstance().getPager().setCurrentItem(cardIndex + 1, true);
		            }
		        }, 3000);
				
			}
		});
		audioPlayButton.setOnClickListener(new View.OnClickListener() {

	            public void onClick(View view) {
	                Speak.getInstance(null).speak(card.getWordLang2());
	            }
	        });
		
		return myFragmentView;
    }
    
    private void setInterfaceView() {
    	Integer userChoice = card.getUserChoice();
    	transcription.setText(card.getTranscription());
    	question.setText(card.getWordLang2());
    	answer.setText(card.getWordLang1());
    	// 0 -- null 1-- wrong 2-- close 3--correct
        card.setLastTime(TimeConvertor.getUnixTime());
    	if(userChoice.equals(0)) {
			indicator.setImageResource(R.drawable.ic_learning_module_null);
			userInput.setText("");
			transcription.setVisibility(View.INVISIBLE);
			answer.setVisibility(View.INVISIBLE);
			question.setVisibility(View.INVISIBLE);
		} else if (userChoice.equals(3)) {
			indicator.setImageResource(R.drawable.ic_learning_module_correct);
			userInput.setText(card.getUserInput());	
			answer.setVisibility(View.VISIBLE);
			transcription.setVisibility(View.VISIBLE);
			question.setVisibility(View.VISIBLE);
		} else if (userChoice.equals(2)) {
			indicator.setImageResource(R.drawable.ic_learning_module_close);
			userInput.setText(card.getUserInput());
			answer.setVisibility(View.VISIBLE);
			transcription.setVisibility(View.VISIBLE);
			question.setVisibility(View.VISIBLE);
		} else {
			indicator.setImageResource(R.drawable.ic_learning_module_incorrect);
			userInput.setText(card.getUserInput());
			answer.setVisibility(View.VISIBLE);
			question.setVisibility(View.VISIBLE);
			transcription.setVisibility(View.VISIBLE);
		}
	}
}
