package com.openwords.learningmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Region.Op;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.openwords.R;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;

import java.util.List;

public class ActivityReview extends FragmentActivity {

    private static List<LeafCard> CardsPool;
    private static int CurrentCard = 0;
    private static ActivityReview instance;

    public static ActivityReview getInstance() {
        return instance;
    }

    public static List<LeafCard> getCardsPool() {
        return CardsPool;
    }

    public static void setCardsPool(List<LeafCard> CardsPool) {
        ActivityReview.CardsPool = CardsPool;
    }

    public static void setCurrentCard(int CurrentCard) {
        ActivityReview.CurrentCard = CurrentCard;
    }
    private ViewPager pager;
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        setContentView(R.layout.activity_rev);

        if (CardsPool == null) {
            Toast.makeText(this, "Please give your cards first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pager = (ViewPager) findViewById(R.id.act_review_pager);
        pager.setOffscreenPageLimit(1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int i, float f, int i1) {
            }

            public void onPageSelected(int i) {
                CurrentCard = i;
            }

            public void onPageScrollStateChanged(int i) {

            }
        });
        adapter = new ReviewAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransformerReview());

        if (CurrentCard > -1 && CurrentCard < CardsPool.size()) {
            pager.setCurrentItem(CurrentCard, true);
        } else {
            CurrentCard = 0;
            Toast.makeText(ActivityReview.this, "You have arrived the last", Toast.LENGTH_SHORT).show();
        }
    }

    public ViewPager getPager() {
        return pager;
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
    	 OpenwordsSharedPreferences.setReviewProgress(new Gson().toJson(new Progress(CardsPool, CurrentCard)));
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

    private class ReviewAdapter extends FragmentPagerAdapter {

    	ReviewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            LogUtil.logDeubg(this, "Request fragment: " + i);
            if (i >= CardsPool.size()) {

                return new FragmentPCReview();
            } else {
            	return new FragmentReview(i);
            }
            //return new FragmentReview(i);
        }

        @Override
        public int getCount() {
            return CardsPool.size() + 1;
        }
    }
    private class PageTransformerReview implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
