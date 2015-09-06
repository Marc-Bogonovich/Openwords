package com.openwords.learningmodule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.openwords.R;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Hearing;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Type;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.Performance;
import com.openwords.model.ResultWordAudio;
import com.openwords.model.ResultWordConnections;
import com.openwords.model.UserLanguage;
import com.openwords.model.Word;
import com.openwords.model.WordAudio;
import com.openwords.model.WordConnection;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Activity class for all LMs, so it applies the same Reverse Navigation,
 * Page Transformer and more.
 *
 * @author hanaldo
 */
public class ActivityLearning extends FragmentActivity implements InterfaceLearningModule {

    private ActivityLearning act;//for referencing purpose only
    private ViewPager pager;
    private WordConnectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm);

        MyDialogHelper.tryShowQuickProgressDialog(act, "Assembling your words...");
        final UserLanguage userLanguageInfo = UserLanguage.getUserLanguageInfo(LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang);
        if (userLanguageInfo == null) {
            MyDialogHelper.tryDismissQuickProgressDialog();
            MyQuickToast.showShort(act, "Error: no language information specified");
            finish();
            return;
        }

        WordConnection.loadWordConnectionsFullPack(true,
                LocalSettings.getUserId(), userLanguageInfo.baseLang, userLanguageInfo.learningLang, userLanguageInfo.page, DataPool.PageSize, false, null,
                new ResultWordConnections() {

                    public void result(final List<WordConnection> connections, List<Word> words, final List<Performance> performance) {
                        if (connections == null) {
                            MyDialogHelper.tryDismissQuickProgressDialog();
                            MyQuickToast.showShort(act, "Error when loading words data");
                            finish();
                            return;
                        }
                        if (connections.isEmpty()) {
                            MyDialogHelper.tryDismissQuickProgressDialog();
                            MyQuickToast.showShort(act, "No data");
                            finish();
                            return;
                        }

                        //try loading audios
                        final Set<Integer> wordIds = new HashSet<Integer>(words.size());
                        for (Word word : words) {
                            if (word.languageId == userLanguageInfo.learningLang) {
                                wordIds.add(word.wordId);
                            }
                        }
                        List<WordAudio> localAudios = WordAudio.getAudios(wordIds);
                        if (localAudios.size() != wordIds.size()) {
                            WordAudio.downloadNewAudios(wordIds, userLanguageInfo.learningLang, new ResultWordAudio() {

                                public void ok() {
                                    if (DataPool.LmType == Learning_Type_Hearing) {
                                        List<WordAudio> newAudios = WordAudio.getAudios(wordIds);
                                        if (newAudios.size() != wordIds.size()) {
                                            MyQuickToast.showShort(act, "Sorry some words don't have audio yet");
                                            finish();
                                            MyDialogHelper.tryDismissQuickProgressDialog();
                                            return;
                                        }
                                    }
                                    DataPool.addLmPool(connections, performance);
                                    init();
                                }

                                public void error(String errorMessage) {
                                    if (DataPool.LmType == Learning_Type_Hearing) {
                                        MyQuickToast.showShort(act, "Sorry we cannot get the audios: " + errorMessage);
                                        finish();
                                        MyDialogHelper.tryDismissQuickProgressDialog();
                                    } else {
                                        DataPool.addLmPool(connections, performance);
                                        init();
                                    }
                                }
                            });
                        } else {
                            DataPool.addLmPool(connections, performance);
                            init();
                        }
                    }
                });
    }

    private void init() {
        DataPool.LmCurrentCard = 0;
        DataPool.LmReverseNav = false;
        MyDialogHelper.tryDismissQuickProgressDialog();

        LogUtil.logDeubg(this, "reverseNav set to: " + DataPool.LmReverseNav);
        if (DataPool.LmReverseNav) {
            MyQuickToast.showShort(act, "Read from Right to Left");
        } else {
            MyQuickToast.showShort(act, "Read from Left to Right");
        }

        pager = (ViewPager) findViewById(R.id.act_lm_pager);
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
                            FragmentLearningModule f = (FragmentLearningModule) adapter.getRecentFragment(DataPool.LmCurrentCard);
                            f.updateChoiceView();
                            autoPlayAudio();
                        }
                    }
                } else {
                    DataPool.LmCurrentCard = i;
                    if (i == DataPool.getPoolSize()) {
                        refreshPC();
                    } else {
                        if (DataPool.LmType == Learning_Type_Type || DataPool.LmType == Learning_Type_Hearing) {
                            FragmentLearningModule f = (FragmentLearningModule) adapter.getRecentFragment(DataPool.LmCurrentCard);
                            f.updateChoiceView();
                            autoPlayAudio();
                        }
                    }
                }

                updatePerformanceBasedOnNavigation();
            }

            public void onPageScrollStateChanged(int i) {
            }
        });

        adapter = new WordConnectionPagerAdapter(getSupportFragmentManager(), act, 20);
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
            updatePerformanceBasedOnNavigation();
            autoPlayAudio();
        }
    }

    private void autoPlayAudio() {
        if (DataPool.LmType == Learning_Type_Hearing) {
            FragmentCardHearing fh = (FragmentCardHearing) adapter.getRecentFragment(DataPool.LmCurrentCard);
            fh.playAudio();
        }
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
