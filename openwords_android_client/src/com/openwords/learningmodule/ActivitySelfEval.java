package com.openwords.learningmodule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.google.gson.Gson;
import com.openwords.R;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.sound.WordAudioManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.List;

/**
 * The activity class of Self Evaluation.<br/>
 * The Boolean Extra value "KEY_REVERSE_NAV" indicates that whether reading the
 * card from Right to Left.
 *
 * @author hanaldo
 */
public class ActivitySelfEval extends FragmentActivity {

    public static final String EXTRA_REVERSE_NAV = "reverse.nav";
    private static List<LeafCardSelfEval> CardsPool;
    private static int CurrentCard = 0;
    private static ActivitySelfEval instance;

    public static ActivitySelfEval getInstance() {
        return instance;
    }

    public static List<LeafCardSelfEval> getCardsPool() {
        return CardsPool;
    }

    /**
     * Set the cards before start this activity.
     *
     * @param CardsPool A list of leaf cards represents each word problem.
     * @param getAudio If this plat require downloading all the audio files
     * first
     * @param context Context for showing some toasts
     */
    public static void setCardsPool(List<LeafCardSelfEval> CardsPool, boolean getAudio, Context context) {
        ActivitySelfEval.CardsPool = CardsPool;
        if (getAudio) {
            int[] ids = new int[CardsPool.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = CardsPool.get(i).getWordTwoId();
            }
            WordAudioManager.addAudioFiles(ids, context);
        }
    }

    public static void setCurrentCard(int CurrentCard) {
        ActivitySelfEval.CurrentCard = CurrentCard;
    }

    public static int getReverseCardIndex(int pageIndex) {
        if (pageIndex == 0) {
            return -1;//additional page, so invalid card
        }
        return CardsPool.size() - pageIndex;
    }

    public static int getReversePageIndex(int cardIndex) {
        return CardsPool.size() - cardIndex;
    }

    private ViewPager pager;
    private SelfEvaluatePagerAdapter adapter;
    private boolean reverseNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_eval);

        if (CardsPool == null) {
            Toast.makeText(this, "Please give your cards first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reverseNav = getIntent().getBooleanExtra(EXTRA_REVERSE_NAV, false);
        LogUtil.logDeubg(this, "reverseNav set to: " + reverseNav);
        if (reverseNav) {
            Toast.makeText(this, "Read from Right to Left", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Read from Left to Right", Toast.LENGTH_SHORT).show();
        }

        pager = (ViewPager) findViewById(R.id.act_self_eval_pager);
        pager.setOffscreenPageLimit(1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int i, float f, int i1) {
            }

            public void onPageSelected(int i) {
                if (reverseNav) {
                    CurrentCard = getReverseCardIndex(i);
                    if (i == 0) {
                        FragmentPCSelfEval.refreshDetails();
                    }
                } else {
                    CurrentCard = i;
                    if (i == CardsPool.size()) {
                        FragmentPCSelfEval.refreshDetails();
                    }
                }
            }

            public void onPageScrollStateChanged(int i) {

            }
        });
        adapter = new SelfEvaluatePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransformerForLeafCard(reverseNav));

        if (reverseNav) {
            if (!(CurrentCard > -1 && CurrentCard < CardsPool.size())) {
                CurrentCard = 0;
            }
            pager.setCurrentItem(getReversePageIndex(CurrentCard), true);
        } else {
            if (CurrentCard > -1 && CurrentCard < CardsPool.size()) {
                pager.setCurrentItem(CurrentCard, true);
            } else {
                CurrentCard = 0;
            }
        }
    }

    public void goToNextCard() {
        if (reverseNav) {
            pager.setCurrentItem(getReversePageIndex(CurrentCard + 1), true);
        } else {
            pager.setCurrentItem(CurrentCard + 1, true);
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
        ActivitySelfEval.super.onBackPressed();
        int languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
        OpenwordsSharedPreferences.setSelfEvaluationProgress(new Gson().toJson(new ProgressSelfEval(CardsPool, CurrentCard, languageID)));
        LogUtil.logDeubg(this, "ProgressSelfEval is saved");
    }

    private class SelfEvaluatePagerAdapter extends FragmentPagerAdapter {

        SelfEvaluatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (reverseNav) {
                if (i <= 0) {
                    return new FragmentPCSelfEval();
                } else {
                    return new FragmentSelfEval(getReverseCardIndex(i));
                }
            } else {
                if (i >= CardsPool.size()) {
                    return new FragmentPCSelfEval();
                } else {
                    return new FragmentSelfEval(i);
                }
            }
        }

        @Override
        public int getCount() {
            return CardsPool.size() + 1;
        }
    }
}
