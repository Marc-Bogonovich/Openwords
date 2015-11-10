package com.openwords.learningmodule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.openwords.R;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Hearing;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Sentence;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Type;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.Performance;
import com.openwords.model.ResultWordAudio;
import com.openwords.model.SentenceConnection;
import com.openwords.model.SetItem;
import com.openwords.model.WordAudio;
import com.openwords.model.WordConnection;
import com.openwords.ui.lily.lm.PageHear;
import com.openwords.ui.lily.lm.PageSentence;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Activity class for all framgment-based LMs, so it applies the same
 * Reverse Navigation, Page Transformer and more.
 *
 * @author hanaldo
 */
public class ActivityLearning extends FragmentActivity implements InterfaceLearningModule {

    private ActivityLearning act;//for referencing purpose only
    private MyPager pager;
    private WordConnectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act = this;
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        setContentView(R.layout.activity_lm);

        final Set<Long> wordIds = new HashSet<Long>(DataPool.currentSetItems.size());
        DataPool.currentPerformance.clear();
        for (SetItem item : DataPool.currentSetItems) {
            DataPool.currentPerformance.add(new Performance(item.wordTwoId, "test", "new", 1));
            wordIds.add(item.wordTwoId);
        }
        if (DataPool.LmType == Learning_Type_Sentence) {
            DataPool.currentPerformance.clear();
            for (SentenceConnection sen : DataPool.currentSentences) {
                DataPool.currentPerformance.add(new Performance(sen.sentenceId, "test", "new", 1));
            }
            wordIds.clear();
        }

        List<WordAudio> localAudios = WordAudio.getAudios(wordIds);
        PageHear.FirstSoundDone = false;
        if (localAudios.size() != wordIds.size()) {
            MyDialogHelper.tryShowQuickProgressDialog(this, "Downloading audios...");
            WordAudio.downloadNewAudios(wordIds, LocalSettings.getCurrentLearningLanguage(), new ResultWordAudio() {

                public void ok() {
                    MyDialogHelper.tryDismissQuickProgressDialog();
                    if (DataPool.LmType == Learning_Type_Hearing) {
                        List<WordAudio> updatedLocalAudios = WordAudio.getAudios(wordIds);
                        if (updatedLocalAudios.size() != wordIds.size()) {
                            MyQuickToast.showShort(act, "Sorry, the audios for this set is not complete.");
                            finish();
                            return;
                        }
                    }
                    init();
                }

                public void error(String errorMessage) {
                    MyDialogHelper.tryDismissQuickProgressDialog();
                    MyQuickToast.showShort(act, "Error: " + errorMessage);
                    if (DataPool.LmType == Learning_Type_Hearing) {
                        finish();
                        return;
                    }
                    init();
                }
            });
        } else {
            init();
        }
    }

    private void init() {
        DataPool.LmCurrentCard = 0;
        DataPool.LmReverseNav = false;

        LogUtil.logDeubg(this, "reverseNav set to: " + DataPool.LmReverseNav);
        if (DataPool.LmReverseNav) {
            MyQuickToast.showShort(act, "Read from Right to Left");
        } else {
            MyQuickToast.showShort(act, "Read from Left to Right");
        }

        pager = (MyPager) findViewById(R.id.act_lm_pager);
        pager.setOffscreenPageLimit(1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int i, float f, int i1) {
            }

            public void onPageSelected(int i) {
                if (DataPool.LmReverseNav) {
                    DataPool.LmCurrentCard = getReversePageIndex(i);
                    if (i == 0) {
                        refreshPC();
                    } else {
                        if (DataPool.LmType == Learning_Type_Type || DataPool.LmType == Learning_Type_Hearing) {
                            //FragmentLearningModule f = (FragmentLearningModule) adapter.getRecentFragment(DataPool.LmCurrentCard);
                            //f.updateChoiceView();
                            autoPlayAudio();
                        }
                        if (DataPool.LmType == Learning_Type_Sentence) {
                            buildSentenceUI();
                        }
                    }
                } else {
                    DataPool.LmCurrentCard = i;
                    if (i == DataPool.getPoolSize()) {
                        refreshPC();
                    } else {
                        if (DataPool.LmType == Learning_Type_Type || DataPool.LmType == Learning_Type_Hearing) {
                            //FragmentLearningModule f = (FragmentLearningModule) adapter.getRecentFragment(DataPool.LmCurrentCard);
                            //f.updateChoiceView();
                            autoPlayAudio();
                        }
                        if (DataPool.LmType == Learning_Type_Sentence) {
                            buildSentenceUI();
                        }
                    }
                }

                //updatePerformanceBasedOnNavigation();
            }

            public void onPageScrollStateChanged(int i) {
            }
        });

        adapter = new WordConnectionPagerAdapter(getSupportFragmentManager(), act);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new WordConnectionPageTransformer(DataPool.LmReverseNav));

        if (DataPool.LmReverseNav) {
            if (!(DataPool.LmCurrentCard > -1 && DataPool.LmCurrentCard < DataPool.getPoolSize())) {
                DataPool.LmCurrentCard = 0;
            }
            pager.setCurrentItem(getReversePageIndex(DataPool.LmCurrentCard), true);
        } else {
            if (DataPool.LmCurrentCard > -1 && DataPool.LmCurrentCard < DataPool.getPoolSize()) {
                pager.setCurrentItem(DataPool.LmCurrentCard, true);
            } else {
                DataPool.LmCurrentCard = 0;
            }
        }

        if (DataPool.LmCurrentCard == 0) {
            //updatePerformanceBasedOnNavigation();
            //autoPlayAudio();
        }

        if (DataPool.LmType == Learning_Type_Hearing) {
            PageHear.FirstSoundDone = false;
        } else if (DataPool.LmType == Learning_Type_Sentence) {
            PageSentence.FirstPageDone = false;
        }
    }

    private void autoPlayAudio() {
        if (DataPool.LmType == Learning_Type_Hearing) {
            PageHear fh = (PageHear) adapter.getRecentFragment(DataPool.LmCurrentCard);
            fh.playAudio();
        }
    }

    private void buildSentenceUI() {
        PageSentence fs = (PageSentence) adapter.getRecentFragment(DataPool.LmCurrentCard);
        fs.buildUI();
    }

    /**
     * Update performance based on page navigation only.
     */
    private void updatePerformanceBasedOnNavigation() {
        if (DataPool.LmCurrentCard >= 0 && DataPool.LmCurrentCard < DataPool.getPoolSize()) {
            WordConnection wc = DataPool.getWordConnection(DataPool.LmCurrentCard);
            Performance perf = DataPool.getPerformance(wc.connectionId);
            if (perf == null) {
                MyQuickToast.showShort(act, "No performance data: " + wc.connectionId);
                return;
            }
            if (DataPool.LmType == Learning_Type_Review) {
                perf.performance = "good";
            }
            perf.tempVersion = perf.version + 1;
            perf.save();
        }
    }

    private int getReversePageIndex(int cardIndex) {
        return DataPool.getPoolSize() - cardIndex;
    }

    public void goToNextCard() {
        if (DataPool.LmReverseNav) {
            pager.setCurrentItem(getReversePageIndex(DataPool.LmCurrentCard + 1), true);
        } else {
            pager.setCurrentItem(DataPool.LmCurrentCard + 1, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.logDeubg(this, "onDestroy");
    }

    public void refreshPC() {
        FragmentPlateCompletion.refreshDetails();

        View focus = getCurrentFocus();
        if (focus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }
}
