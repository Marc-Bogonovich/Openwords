package com.openwords.learningmodule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.openwords.R;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Review;
import com.openwords.model.DataPool;
import com.openwords.model.LocalSettings;
import com.openwords.model.Performance;
import com.openwords.model.ResultWordConnections;
import com.openwords.model.UserLearningLanguages;
import com.openwords.model.Word;
import com.openwords.model.WordConnection;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import java.util.List;

/**
 * The Activity class for all LMs, so it applies the same Reverse Navigation,
 * Page Transformer and more.
 *
 * @author hanaldo
 */
public class ActivityLearning extends FragmentActivity {

    private ActivityLearning act;//for referencing purpose only
    private ViewPager pager;
    private WordConnectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm);

        MyDialogHelper.tryShowQuickProgressDialog(act, "Assembling your words...");
        UserLearningLanguages languageInfo = UserLearningLanguages.getUserLanguageInfo(LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang);
        if (languageInfo == null) {
            MyQuickToast.showShort(act, "Error: no language information specified");
            finish();
            return;
        }

        WordConnection.loadWordConnectionsFullPack(true,
                LocalSettings.getUserId(), languageInfo.baseLang, languageInfo.learningLang, languageInfo.page, 10, false, null,
                new ResultWordConnections() {

                    public void result(List<WordConnection> connections, List<Word> words, List<Performance> performance) {
                        if (connections == null) {
                            MyQuickToast.showShort(act, "Error when loading words data");
                            finish();
                            return;
                        }
                        DataPool.addLmPool(connections, performance);
                        init();
                    }
                });
    }

    private void init() {
        DataPool.LmCurrentCard = 0;
        DataPool.LmReverseNav = false;
        MyDialogHelper.tryDismissQuickProgressDialog();

        LogUtil.logDeubg(act, "reverseNav set to: " + DataPool.LmReverseNav);
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
                    }
                } else {
                    DataPool.LmCurrentCard = i;
                    if (i == DataPool.getPoolSize()) {
                        refreshPC();
                    }
                }

                updatePerformanceBasedOnNavigation();
            }

            public void onPageScrollStateChanged(int i) {
            }
        });

        adapter = new WordConnectionPagerAdapter(getSupportFragmentManager(), act);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransformerForLeafCard(DataPool.LmReverseNav));

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
        }
    }

    /**
     * Update performance based on page navigation only.
     */
    private void updatePerformanceBasedOnNavigation() {
        if (DataPool.LmType == Learning_Type_Review) {
            if (DataPool.LmCurrentCard >= 0 && DataPool.LmCurrentCard < DataPool.getPoolSize()) {
                WordConnection wc = DataPool.getWordConnection(DataPool.LmCurrentCard);
                Performance perf = DataPool.getPerformance(wc.connectionId);
                if (perf == null) {
                    MyQuickToast.showShort(act, "No performance data: " + wc.connectionId);
                    return;
                }
                perf.performance = "good";
                perf.tempVersion = perf.version + 1;
                perf.save();
            }
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
        LogUtil.logDeubg(act, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.logDeubg(act, "onDestroy");
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
