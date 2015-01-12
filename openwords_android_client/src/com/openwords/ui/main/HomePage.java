package com.openwords.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.openwords.model.DataPool;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardHearingAdapter;
import com.openwords.model.LeafCardReviewAdapter;
import com.openwords.model.LeafCardSelfEvalAdapter;
import com.openwords.model.LeafCardTypeEvalAdapter;
import com.openwords.model.UserInfo;
import com.openwords.model.UserWords;
import com.openwords.model.WordTranscription;
import com.openwords.services.GetWords;
import com.openwords.services.ModelWordConnection;
import com.openwords.services.SetUserWords;
import com.openwords.sound.WordAudioManager;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.ui.other.ActionBarBuilder;
import com.openwords.util.TimeConvertor;
import com.openwords.util.localization.LocalizationManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.LinkedList;
import java.util.List;

public class HomePage extends Activity {

    public static HomePage instance;
    private Spinner begin, l2_dropdown;
    private int language_position = -1;
    private List<String> languageOptions;
    private ArrayAdapter<String> dropdownAdapter;
    private int homelang_id;
    private String homelang_name;
    private ProgressDialog pDialog = null;
    private UserInfo userinfo;
    private int SIZE = 10;
    private ActionBarBuilder actionBar;
    private LearningModuleType lmType = null;
    private Button testPageGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_home_page);
        instance = this;

        //building the action bar
        actionBar = new ActionBarBuilder(this, ActionBarBuilder.Home_Page);

        // Creating the drop down object
        l2_dropdown = (Spinner) findViewById(R.id.homePage_Spinner_chooseLanguage);

        begin = (Spinner) findViewById(R.id.homePage_Spinner_begin);
        begin.setOnItemSelectedListener(new OnItemSelectedListener() {

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

        userinfo = OpenwordsSharedPreferences.getUserInfo();

        fillLanguageOptions();
    }

    private void fillUI() {
        String[] options = new String[]{
            LocalizationManager.getTextOptionReview(),
            LocalizationManager.getTextOptionSelf(),
            LocalizationManager.getTextOptionType(),
            LocalizationManager.getTextOptionHearing()
        };
        begin.setAdapter(new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, options));
        testPageGo.setText(LocalizationManager.getTextGo());
    }

    private void refreshLanguageOptions() {
        LogUtil.logDeubg(this, "refreshLanguageOptions");
//        DataPool.LanguageList.remove(DataPool.LanguageList.size() - 1);
//        DataPool.LanguageList.add(new ModelLanguage(-999, LocalizationManager.getTextMoreLang()));
//
//        languageOptions.clear();
//        language_position = -1;
//        for (ModelLanguage l : DataPool.LanguageList) {
//            languageOptions.add(l.getL2name());
//            LogUtil.logDeubg(this, "user " + userinfo.getUserId() + " has lang: " + l.getL2name());
//            if (l.getL2id() == OpenwordsSharedPreferences.getUserInfo().getLang_id()) {
//                language_position = languageOptions.size() - 1;
//            }
//        }
//        dropdownAdapter.notifyDataSetChanged();
//        if (language_position != -1) {
//            l2_dropdown.setSelection(language_position);
//        } else {
//            l2_dropdown.setSelection(0);
//        }
    }

    private void fillLanguageOptions() {
        if (!DataPool.LanguageList.isEmpty()) {
            languageOptions = new LinkedList<String>();
            dropdownAdapter = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_list_item_1, android.R.id.text1, languageOptions);
            l2_dropdown.setAdapter(dropdownAdapter);
            l2_dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    LogUtil.logDeubg(this, "L2 ID: " + Integer.toString(DataPool.LanguageList.get(position).getL2id()));
                    if (DataPool.LanguageList.get(position).getL2id() == -999) {
                        HomePage.this.startActivity(new Intent(HomePage.this, LanguagePage.class));
                    } else {
                        homelang_id = DataPool.LanguageList.get(position).getL2id();
                        homelang_name = DataPool.LanguageList.get(position).getL2name(); //new
                        UserInfo user = OpenwordsSharedPreferences.getUserInfo();
                        user.setLang_id(homelang_id);
                        user.setLang_Name(homelang_name); //new
                        OpenwordsSharedPreferences.setUserInfo(user);
                        Toast.makeText(HomePage.this, "Selected Lang: " + OpenwordsSharedPreferences.getUserInfo().getLang_Name() + " ID: " + OpenwordsSharedPreferences.getUserInfo().getLang_id(), Toast.LENGTH_SHORT).show();

                        Log.d("saved Lang", "" + OpenwordsSharedPreferences.getUserInfo().getLang_id()
                                + OpenwordsSharedPreferences.getUserInfo().getLang_Name()); //new
                        //-----getting first x words if not present----
                        //new GetFirstWords().execute();
                        getFirstWords();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }
    }

    //-------- Getting first 10 words from server for a language -----------------
    /*public void getFirstWordsFromServer() {
     ArrayList<WordsPageTool> words_list = new ArrayList<WordsPageTool>();
     try {
     List<NameValuePair> params = new ArrayList<NameValuePair>(3);
     params.add(new BasicNameValuePair("user", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getUserId())));
     params.add(new BasicNameValuePair("langOne", "1"));
     params.add(new BasicNameValuePair("langTwo", Integer.toString(OpenwordsSharedPreferences.getUserInfo().getLang_id())));
     LogUtil.logDeubg(HomePage.this, params.toString());
     JSONParser jsonParse = new JSONParser();
     JSONObject jObj = jsonParse.makeHttpRequest(nextwords_url, "POST", params);
     Log.d("Obj", jObj.toString());
     if (jObj.getInt("success") == 1) {
     JSONArray jArr = jObj.getJSONArray("data");
     String abc = Integer.toString(jArr.length());
     Log.d("Array", abc);
     jArrMain = jArr;
     }

     } catch (Exception e) {
     e.printStackTrace();
     }
     }*/
    private void updateWordsOnServer(String conIds, long dTime) {
        LogUtil.logDeubg(HomePage.this, "updateWordsOnServer: " + conIds);
        int user = OpenwordsSharedPreferences.getUserInfo().getUserId();
        int langTwo = OpenwordsSharedPreferences.getUserInfo().getLang_id();
        SetUserWords.request(conIds, Long.toString(dTime), Integer.toString(user), Integer.toString(langTwo), 0, new SetUserWords.AsyncCallback() {

            public void callback(String message, Throwable error) {
                if (error == null) {
                    LogUtil.logDeubg(this, "Message From Server: " + message);
                } else {
                    Toast.makeText(HomePage.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
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

    private void getFirstWords() {
        int langId = OpenwordsSharedPreferences.getUserInfo().getLang_id();
        LogUtil.logDeubg(this, "getFirstWords langId: " + langId);
        List<UserWords> existList = UserWords.findByLanguage(langId);
        if (existList.isEmpty()) {
            LogUtil.logDeubg(this, "existList isEmpty");
            //getFirstWordsFromServer();
            GetWords.request(Integer.toString(OpenwordsSharedPreferences.getUserInfo().getUserId()),
                    "1",//to-do
                    Integer.toString(OpenwordsSharedPreferences.getUserInfo().getLang_id()),
                    0,
                    new GetWords.AsyncCallback() {

                        public void callback(List<ModelWordConnection> data, Throwable error) {
                            if (data != null) {
                                String conIds = "";
                                for (ModelWordConnection w : data) {
                                    try {
                                        //JSONObject c = jArrMain.getJSONObject(i);

                                        UserWords newUw = new UserWords(w.getConnectionId(), w.getWordl1(),
                                                w.getWordl1Text(), w.getWordl2(), w.getWordl2Text(),
                                                w.getL2id(), w.getL2name(), w.getAudio(), 1);
                                        newUw.save();

                                        WordTranscription t = new WordTranscription(w.getWordl2(), w.getTrans());
                                        t.save();

                                        if (conIds.isEmpty()) {
                                            conIds += w.getConnectionId();
                                        } else {
                                            conIds = conIds + "|" + w.getConnectionId();
                                        }

                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                                Toast.makeText(HomePage.this, "Your Words are ready", Toast.LENGTH_SHORT).show();
                                //update on server
                                updateWordsOnServer(conIds, TimeConvertor.getUnixTime());
                            } else {
                                Toast.makeText(HomePage.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
