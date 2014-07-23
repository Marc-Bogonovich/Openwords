package com.openwords.learningmodule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openwords.R;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.model.LeafCardTypeEval;
import com.openwords.tts.Speak;
import com.openwords.util.TimeConvertor;
import com.openwords.util.WordComparsion;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;

@SuppressWarnings("unused")
@SuppressLint({ "ValidFragment", "DefaultLocale" })
public class FragmentTypeEval extends Fragment {

    private final int cardIndex;
	private EditText userInput;
	private TextView answer;
	private TextView question;
	private TextView transcription;
	private ImageView checkButton;
	private ImageView audioPlayButton;
	private ImageView status;
	private final double CUTOFF = 0.75f;
	private LeafCardTypeEval card;
    private LinearLayout breadcrumbs;
    private View myFragmentView;
	
    @SuppressLint("ValidFragment")
	public FragmentTypeEval(int cardIndex) {
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

        myFragmentView = inflater.inflate(R.layout.fragment_type_eval, container, false);
        card = ActivityTypeEval.getCardsPool().get(this.cardIndex);

        answer = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_answer);
		question = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_question);
		transcription = (TextView) myFragmentView.findViewById(R.id.typeEvaluate_TextView_transcription);
		userInput = (EditText) myFragmentView.findViewById(R.id.typeEvaluate_EditText_input);
		checkButton = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_checkButton);
		status = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_status);
		audioPlayButton = (ImageView) myFragmentView.findViewById(R.id.typeEvaluate_ImageView_audioPlay);
		setInterfaceView();
		//makeBreadCrumbs();
        card.setLastTime(TimeConvertor.getUnixTime());
		answer.setVisibility(View.INVISIBLE);
		
		userInput.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void afterTextChanged(Editable s){}
			@SuppressLint("DefaultLocale") @Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 {
						Integer userChoice = 0;
						
						if(userInput!=null) {
							status.setImageResource(R.drawable.ic_learning_module_null);
							//user choice 0--null 1--wrong 2--close 3--correct
							String userInputString = userInput.getText().toString().toLowerCase();
							double similarity = WordComparsion.similarity(userInputString, card.getWordLang1());
							if(userInputString.equals(card.getWordLang1())) {
								status.setImageResource(R.drawable.ic_learning_module_correct);
								card.setUserInput(userInputString);
								userChoice = 3;
								Handler mHandler = new Handler();
								mHandler.postDelayed(new Runnable() {
						            public void run() {
						            	ActivityTypeEval.getInstance().getPager().setCurrentItem(cardIndex + 1, true);
						            }
						        }, 3000);
						} else {
							answer.setVisibility(View.INVISIBLE);
							status.setImageResource(R.drawable.ic_learning_module_null);
							userChoice = 0;
							//answer.setVisibility(View.VISIBLE);
						}
						card.setUserChoice(userChoice);

					}
			}
		}});
		
		checkButton.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("DefaultLocale") @Override
			public void onClick(View v) {
				Integer userChoice = 0;
				
				if(userInput!=null) {
					//user choice 0--null 1--wrong 2--close 3--correct
					String userInputString = userInput.getText().toString().toLowerCase();
					double similarity = WordComparsion.similarity(userInputString, card.getWordLang1());
					if(userInputString.equals(card.getWordLang1())) {
						status.setImageResource(R.drawable.ic_learning_module_correct);
						card.setUserInput(userInputString);
						userChoice = 3;
					} else if(similarity>=CUTOFF) {
						status.setImageResource(R.drawable.ic_learning_module_close);
						userChoice = 2;
						card.setUserInput(userInputString);
						answer.setVisibility(View.VISIBLE);
						//if want the status icon becomes null when move forward/backward, change the value of userChoice
					} else {
						status.setImageResource(R.drawable.ic_learning_module_incorrect);
						userChoice = 1;
						card.setUserInput(userInputString);
						answer.setVisibility(View.VISIBLE);
					}
				} else {
					status.setImageResource(R.drawable.ic_learning_module_incorrect);
					userChoice = 0;
					answer.setVisibility(View.VISIBLE);
				}
				card.setUserChoice(userChoice);
				
				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {
		            public void run() {
		            	ActivityTypeEval.getInstance().getPager().setCurrentItem(cardIndex + 1, true);
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
    	if(userChoice.equals(0)) {
			status.setImageResource(R.drawable.ic_learning_module_null);
			userInput.setText("");
			answer.setVisibility(View.INVISIBLE);
		} else if (userChoice.equals(3)) {
			status.setImageResource(R.drawable.ic_learning_module_correct);
			userInput.setText("");	
			answer.setVisibility(View.INVISIBLE);
		} else if (userChoice.equals(2)) {
			status.setImageResource(R.drawable.ic_learning_module_close);
			userInput.setText("");
			answer.setVisibility(View.VISIBLE);
		} else {
			status.setImageResource(R.drawable.ic_learning_module_incorrect);
			userInput.setText("");
			answer.setVisibility(View.VISIBLE);
		}
		
	}
    
//    private void makeBreadCrumbs() {
//    	breadcrumbs = (LinearLayout) myFragmentView.findViewById(R.id.review_LinearLayout_breadcrumbs);
//    	int size = OpenwordsSharedPreferences.getLeafCardSize();
//    	for(int i=0;i<size;i++) {
//    		ImageView crumb = new ImageView(this.getActivity().getApplicationContext());
//    		
//    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//    		            0,
//    		            LinearLayout.LayoutParams.WRAP_CONTENT, 1);
//    		
//    		crumb.setImageResource(R.drawable.ic_learning_module_breadcrumb_normal);
//    		if(i==cardIndex) crumb.setImageResource(R.drawable.ic_learning_module_breadcrumb_large);
//    		breadcrumbs.addView(crumb, i , params);
//    	}
//    }
}
