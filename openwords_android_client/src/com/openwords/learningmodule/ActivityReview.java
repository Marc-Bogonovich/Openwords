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

public class ActivityReview extends FragmentActivity {

    private static boolean ReverseNav = false;
    private static List<LeafCard> CardsPool;
    private static int CurrentCard = 0;
    private static ActivityReview instance;

    public static ActivityReview getInstance() {
        return instance;
    }

    public static List<LeafCard> getCardsPool() {
        return CardsPool;
    }

    public static void setReverseNav(boolean ReverseNav) {
        ActivityReview.ReverseNav = ReverseNav;
    }

    public static void setCardsPool(List<LeafCard> CardsPool, boolean getAudio, Context context) {
        ActivityReview.CardsPool = CardsPool;
        if (getAudio) {
            int[] ids = new int[CardsPool.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = CardsPool.get(i).getWordTwoId();
            }
            WordAudioManager.addAudioFiles(ids, context);
        }
    }

    public static void setCurrentCard(int CurrentCard) {
        ActivityReview.CurrentCard = CurrentCard;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rev);

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

        pager = (ViewPager) findViewById(R.id.act_review_pager);
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
                    return new FragmentReview(getReverseCardIndex(index));
                } else {
                    return new FragmentReview(index);
                }
            }

            public Fragment makePCFragment() {
                return new FragmentPCReview();
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
        int languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
        OpenwordsSharedPreferences.setReviewProgress(new Gson().toJson(new ProgressReview(CardsPool, CurrentCard, languageID)));
        ActivityReview.super.onBackPressed();
        //        new AlertDialog.Builder(this)
//                .setTitle("Really Quit?")
//                .setMessage("Are you sure you want to quite current Evaluation? (You progress will be saved)")
//                .setNegativeButton("No", null)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        OpenwordsSharedPreferences.setReviewProgress(new Gson().toJson(new Progress(CardsPool, CurrentCard)));
//                        ActivityReview.super.onBackPressed();
//                    }
//                }).create().show();
    }
}
