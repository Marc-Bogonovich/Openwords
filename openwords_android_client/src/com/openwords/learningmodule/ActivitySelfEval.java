package com.openwords.learningmodule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.google.gson.Gson;
import com.openwords.R;
import com.openwords.learningmodule.LeafCardPagerAdapter.FragmentMaker;
import com.openwords.model.LeafCard;
import com.openwords.sound.WordAudioManager;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.List;

public class ActivitySelfEval extends FragmentActivity {

    //private static Map<LearningModuleTypes, Boolean> LMReverseNavs = new HashMap<LearningModuleTypes, Boolean>(4);
    //private static Map<LearningModuleTypes, List<LeafCardSelfEval>> LMCardsPools = new HashMap<LearningModuleTypes, List<LeafCardSelfEval>>(4);
    //private static Map<LearningModuleTypes, Integer> LMCurrentCards = new HashMap<LearningModuleTypes, Integer>(4);
    //private static Map<LearningModuleTypes, ActivitySelfEval> LMInstances = new HashMap<LearningModuleTypes, ActivitySelfEval>(4);
    private static boolean ReverseNav = false;
    private static List<LeafCard> CardsPool;
    private static int CurrentCard = 0;
    private static ActivitySelfEval instance;

    public static ActivitySelfEval getInstance() {
        return instance;
    }

//    public static ActivitySelfEval getInstance(LearningModuleTypes type) {
//        return LMInstances.get(type);
//    }
    public static List<LeafCard> getCardsPool() {
        return CardsPool;
    }

//    public static List<LeafCardSelfEval> getCardsPool(LearningModuleTypes type) {
//        return LMCardsPools.get(type);
//    }
    public static void setReverseNav(boolean ReverseNav) {
        ActivitySelfEval.ReverseNav = ReverseNav;
    }

//    public static void setReverseNav(boolean ReverseNav, LearningModuleTypes type) {
//        LMReverseNavs.put(type, ReverseNav);
//    }
    /**
     * Set the cards before start this activity.
     *
     * @param CardsPool A list of leaf cards represents each word problem.
     * @param getAudio If this plat require downloading all the audio files
     * first
     * @param context Context for showing some toasts
     */
    public static void setCardsPool(List<LeafCard> CardsPool, boolean getAudio, Context context) {
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
    private LeafCardPagerAdapter adapter;
    //private LearningModuleTypes type;

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

        if (ReverseNav) {
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
                if (ReverseNav) {
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

        adapter = new LeafCardPagerAdapter(getSupportFragmentManager(), ReverseNav, CardsPool.size(), new FragmentMaker() {

            public Fragment makePageFragment(int index) {
                if (ReverseNav) {
                    return new FragmentSelfEval(getReverseCardIndex(index));
                } else {
                    return new FragmentSelfEval(index);
                }
            }

            public Fragment makePCFragment() {
                return new FragmentPCSelfEval();
            }
        });
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransformerForLeafCard(ReverseNav));

        if (ReverseNav) {
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
        if (ReverseNav) {
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
        OpenwordsSharedPreferences.setSelfEvaluationProgress(new Gson().toJson(new ProgressLM(CardsPool, CurrentCard, languageID)));
        LogUtil.logDeubg(this, "ProgressSelfEval is saved");
    }
}
