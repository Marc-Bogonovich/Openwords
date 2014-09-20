package com.openwords.learningmodule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.google.gson.Gson;
import com.openwords.model.LeafCard;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import static com.openwords.util.preference.OpenwordsSharedPreferences.HEARING_PROGRESS;
import static com.openwords.util.preference.OpenwordsSharedPreferences.REVIEW_PROGRESS;
import static com.openwords.util.preference.OpenwordsSharedPreferences.SELF_EVALUATION_PROGRESS;
import static com.openwords.util.preference.OpenwordsSharedPreferences.TYPE_EVALUATION_PROGRESS;
import java.util.List;

/**
 * The Activity class for all LMs, so it applies the same Reverse Navigation,
 * Page Transformer and more.
 *
 * @author hanaldo
 */
public class ActivityLM extends FragmentActivity {

    private List<LeafCard> cardsPool;
    private int currentCard = -1;
    private boolean reverseNav = false;
    private ViewPager pager;
    private LeafCardPagerAdapter adapter;
    private LearningModuleType type;
    private int layoutId, pagerId;
    private FragmentMaker fm;
    private RefreshPCCallback refreshPC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object[] bundle;
        try {
            bundle = ActivityInstantiationCallbackBundle.getBundle();
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!unpackBundle(bundle)) {
            finish();
            return;
        }
        setContentView(layoutId);

        LogUtil.logDeubg(this, "reverseNav set to: " + reverseNav);
        if (reverseNav) {
            Toast.makeText(this, "Read from Right to Left", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Read from Left to Right", Toast.LENGTH_SHORT).show();
        }

        pager = (ViewPager) findViewById(pagerId);
        pager.setOffscreenPageLimit(1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int i, float f, int i1) {
            }

            public void onPageSelected(int i) {
                if (reverseNav) {
                    currentCard = getReversePageIndex(i);
                    if (i == 0) {
                        refreshPC.refresh();
                    }
                } else {
                    currentCard = i;
                    if (i == cardsPool.size()) {
                        refreshPC.refresh();
                    }
                }
            }

            public void onPageScrollStateChanged(int i) {
            }
        });

        adapter = new LeafCardPagerAdapter(getSupportFragmentManager(), reverseNav, cardsPool.size(), fm);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransformerForLeafCard(reverseNav));

        if (reverseNav) {
            if (!(currentCard > -1 && currentCard < cardsPool.size())) {
                currentCard = 0;
            }
            pager.setCurrentItem(getReversePageIndex(currentCard), true);
        } else {
            if (currentCard > -1 && currentCard < cardsPool.size()) {
                pager.setCurrentItem(currentCard, true);
            } else {
                currentCard = 0;
            }
        }
    }

    private boolean unpackBundle(Object[] bundle) {
        type = (LearningModuleType) bundle[0];
        if (type == null) {
            Toast.makeText(this, "Please provide the LearningModuleType", Toast.LENGTH_SHORT).show();
            return false;
        }
        layoutId = (Integer) bundle[1];
        if (layoutId <= 0) {
            Toast.makeText(this, "Please provide the layout id", Toast.LENGTH_SHORT).show();
            return false;
        }
        pagerId = (Integer) bundle[2];
        if (pagerId <= 0) {
            Toast.makeText(this, "Please provide the pager id", Toast.LENGTH_SHORT).show();
            return false;
        }
        fm = (FragmentMaker) bundle[3];
        if (fm == null) {
            Toast.makeText(this, "Please provide the fragment maker", Toast.LENGTH_SHORT).show();
            return false;
        }
        fm.setActivityInstance(this);
        reverseNav = (Boolean) bundle[4];
        cardsPool = (List<LeafCard>) bundle[5];
        if (cardsPool == null) {
            Toast.makeText(this, "Please give your cards first", Toast.LENGTH_SHORT).show();
            return false;
        }
        currentCard = (Integer) bundle[6];
        if (currentCard < 0) {
            Toast.makeText(this, "Please give your current card index", Toast.LENGTH_SHORT).show();
            return false;
        }
        refreshPC = (RefreshPCCallback) bundle[7];
        if (refreshPC == null) {
            Toast.makeText(this, "Please provide the callback of refresh PC", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int getReversePageIndex(int cardIndex) {
        return cardsPool.size() - cardIndex;
    }

    public void goToNextCard() {
        if (reverseNav) {
            pager.setCurrentItem(getReversePageIndex(currentCard + 1), true);
        } else {
            pager.setCurrentItem(currentCard + 1, true);
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

    @Override
    public void onBackPressed() {
        ActivityLM.super.onBackPressed();
        int languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
        String progressKey;
        switch (type) {
            case LM_SelfEvaluation:
                progressKey = SELF_EVALUATION_PROGRESS;
                break;
            case LM_Review:
                progressKey = REVIEW_PROGRESS;
                break;
            case LM_TypeEvaluation:
                progressKey = TYPE_EVALUATION_PROGRESS;
                break;
            case LM_HearingEvaluation:
                progressKey = HEARING_PROGRESS;
                break;
            default:
                return;
        }
        OpenwordsSharedPreferences.setLMProgress(progressKey, new Gson().toJson(new ProgressLM(cardsPool, currentCard, languageID)));
        LogUtil.logDeubg(this, "Progress " + type.toString() + " is saved");
    }
}
