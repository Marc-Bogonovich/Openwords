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
import com.openwords.model.WordConnection;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;

/**
 * The Activity class for all LMs, so it applies the same Reverse Navigation,
 * Page Transformer and more.
 *
 * @author hanaldo
 */
public class ActivityLearning extends FragmentActivity {

    private ViewPager pager;
    private WordConnectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm);

        MyDialogHelper.tryShowQuickProgressDialog(this, "Assembling your words...");
        DataPool.LmPool = WordConnection.getConnections(LocalSettings.getBaseLanguageId(), DataPool.LmLearningLang, 3, 2);
        DataPool.LmCurrentCard = 0;
        DataPool.LmReverseNav = false;
        MyDialogHelper.tryDismissQuickProgressDialog();

        LogUtil.logDeubg(this, "reverseNav set to: " + DataPool.LmReverseNav);
        if (DataPool.LmReverseNav) {
            MyQuickToast.showShort(this, "Read from Right to Left");
        } else {
            MyQuickToast.showShort(this, "Read from Left to Right");
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
                    if (i == DataPool.LmPool.size()) {
                        refreshPC();
                    }
                }
            }

            public void onPageScrollStateChanged(int i) {
            }
        });

        adapter = new WordConnectionPagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransformerForLeafCard(DataPool.LmReverseNav));

        if (DataPool.LmReverseNav) {
            if (!(DataPool.LmCurrentCard > -1 && DataPool.LmCurrentCard < DataPool.LmPool.size())) {
                DataPool.LmCurrentCard = 0;
            }
            pager.setCurrentItem(getReversePageIndex(DataPool.LmCurrentCard), true);
        } else {
            if (DataPool.LmCurrentCard > -1 && DataPool.LmCurrentCard < DataPool.LmPool.size()) {
                pager.setCurrentItem(DataPool.LmCurrentCard, true);
            } else {
                DataPool.LmCurrentCard = 0;
            }
        }
    }

    private int getReversePageIndex(int cardIndex) {
        return DataPool.LmPool.size() - cardIndex;
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
        switch (DataPool.LmType) {
            case Learning_Type_Review:
                FragmentPCReview.refreshDetails();
                break;
            default:
                break;
        }

        View focus = getCurrentFocus();
        if (focus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }
}
