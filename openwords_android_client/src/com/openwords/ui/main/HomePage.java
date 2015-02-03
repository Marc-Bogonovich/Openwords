package com.openwords.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.learningmodule.ActivityInstantiationCallbackBundle;
import com.openwords.learningmodule.ActivityLM;
import com.openwords.learningmodule.LearningModuleType;
import com.openwords.learningmodule.ProgressLM;
import com.openwords.model.Language;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardHearingAdapter;
import com.openwords.model.LeafCardReviewAdapter;
import com.openwords.model.LeafCardSelfEvalAdapter;
import com.openwords.model.LeafCardTypeEvalAdapter;
import com.openwords.model.LocalSettings;
import com.openwords.sound.WordAudioManager;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.other.ActionBarBuilder;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.openwords.util.ui.MyQuickToast;
import java.util.LinkedList;
import java.util.List;

public class HomePage extends Activity {

    public static HomePage instance;
    private Spinner learningModuleOption, languageOption;
    private ProgressDialog pDialog = null;
    private int SIZE = 10;
    private ActionBarBuilder actionBar;
    private LearningModuleType lmType = null;
    private Button testPageGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_home_page);
        //building the action bar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Home_Page);

        languageOption = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);
        learningModuleOption = (Spinner) findViewById(R.id.homePage_Spinner_begin);
        learningModuleOption.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> av, View view, int i, long l) {
                switch (i) {
                    case 0:
                        lmType = LearningModuleType.LM_Review;
                        break;
                    case 1:
                        lmType = LearningModuleType.LM_SelfEvaluation;
                        break;
                    case 2:
                        lmType = LearningModuleType.LM_TypeEvaluation;
                        break;
                    case 3:
                        lmType = LearningModuleType.LM_HearingEvaluation;
                        break;
                    default:
                        lmType = null;
                        break;
                }
                if (lmType != null) {
                    Toast.makeText(HomePage.this, "Current LM type: " + lmType.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            public void onNothingSelected(AdapterView<?> av) {
                //do nothing
            }
        });

        testPageGo = (Button) findViewById(R.id.homePage_Button_testPageGo);
        testPageGo.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                testPageButtonClick();
            }
        });
    }

    private void fillUI() {
        String[] options = new String[]{
            LocalizationManager.getTextOptionReview(),
            LocalizationManager.getTextOptionSelf(),
            LocalizationManager.getTextOptionType(),
            LocalizationManager.getTextOptionHearing()
        };
        learningModuleOption.setAdapter(new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, options));
        testPageGo.setText(LocalizationManager.getTextGo());
    }

    private void refreshLanguageOptions() {
        LogUtil.logDeubg(this, "refreshLanguageOptions");
        final List<Language> languages = Language.getLearningLanguages(LocalSettings.getBaseLanguageId());
        List<String> languageNames = new LinkedList<String>();
        if (!languages.isEmpty()) {
            languageOption.setOnTouchListener(null);
            for (Language lang : languages) {
                languageNames.add(lang.name);
            }
        } else {
            final boolean[] touched = new boolean[]{false};
            languageOption.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View view, MotionEvent me) {
                    if (touched[0]) {
                        return true;
                    }
                    touched[0] = true;
                    startActivity(new Intent(HomePage.this, LanguagePage.class));
                    return true;
                }
            });
        }

        languageNames.add(LocalizationManager.getTextMoreLang());
        languageOption.setAdapter(new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_list_item_1, android.R.id.text1, languageNames));
        languageOption.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (languages.isEmpty()) {
                    return;
                }
                if (position >= languages.size()) {
                    startActivity(new Intent(HomePage.this, LanguagePage.class));
                    return;
                }
                Language chosen = languages.get(position);
                MyQuickToast.showShort(HomePage.this, "got " + chosen.name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                MyQuickToast.showShort(HomePage.this, "onNothingSelected");
            }
        });
    }

    public void testPageButtonClick() {
        final List<LeafCard> cards;
        final int currentCard;
        final ProgressLM progress;

        switch (lmType) {
            case LM_Review:
                progress = OpenwordsSharedPreferences.getReviewProgress();
                break;
            case LM_SelfEvaluation:
                progress = OpenwordsSharedPreferences.getSelfEvaluationProgress();
                break;
            case LM_TypeEvaluation:
                progress = OpenwordsSharedPreferences.getTypeEvaluationProgress();
                break;
            case LM_HearingEvaluation:
                progress = OpenwordsSharedPreferences.getHearingProgress();
                break;
            default:
                return;
        }

        pDialog = ProgressDialog.show(this, "", "Assembling leaf cards", true);
        if (progress == null || progress.getLanguageID() != OpenwordsSharedPreferences.getUserInfo().getLang_id()) {
            switch (lmType) {
                case LM_Review:
                    cards = new LeafCardReviewAdapter().getList(SIZE);
                    break;
                case LM_SelfEvaluation:
                    cards = new LeafCardSelfEvalAdapter().getList(SIZE);
                    break;
                case LM_TypeEvaluation:
                    cards = new LeafCardTypeEvalAdapter().getList(SIZE);
                    break;
                case LM_HearingEvaluation:
                    cards = new LeafCardHearingAdapter().getList(SIZE);
                    break;
                default:
                    return;
            }
            if (cards.size() <= 0) {
                Toast.makeText(HomePage.this, "Please select word first", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                return;
            } else {
                currentCard = 0;
            }
        } else {
            cards = progress.getCardsPool();
            currentCard = progress.getCurrentCard();
        }

        ActivityInstantiationCallbackBundle.setBundle(lmType,
                false,
                cards,
                currentCard,
                true,
                this,
                new WordAudioManager.AsyncCallback() {

                    public void doneAddAudioFiles() {
                        startActivity(new Intent(HomePage.this, ActivityLM.class));
                        pDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
        actionBar.checkSetting();
        SIZE = OpenwordsSharedPreferences.getLeafCardSize();
        refreshLanguageOptions();
        fillUI();
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                HomePage.super.onBackPressed();
            }
        });
    }
}
