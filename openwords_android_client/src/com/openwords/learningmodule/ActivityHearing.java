package com.openwords.learningmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.google.gson.Gson;
import com.openwords.R;
import com.openwords.model.LeafCardHearing;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.List;

public class ActivityHearing extends FragmentActivity {

    private static List<LeafCardHearing> CardsPool;
    private static int CurrentCard = -1;
    private static ActivityHearing instance;

    public static ActivityHearing getInstance() {
        return instance;
    }

    public static List<LeafCardHearing> getCardsPool() {
        return CardsPool;
    }

    public static void setCardsPool(List<LeafCardHearing> CardsPool) {
        ActivityHearing.CardsPool = CardsPool;
    }

    public static void setCurrentCard(int CurrentCard) {
        ActivityHearing.CurrentCard = CurrentCard;
    }

    private ViewPager pager;
    private HearingPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        setContentView(R.layout.activity_hear);

        if (CardsPool == null) {
            Toast.makeText(this, "Please give your cards first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pager = (ViewPager) findViewById(R.id.act_hearing_pager);
        pager.setOffscreenPageLimit(1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int i, float f, int i1) {
            }

            public void onPageSelected(int i) {
                CurrentCard = i;
                if (i == CardsPool.size()) {
                    FragmentPlateCompletion.refreshDetails();
                }
            }

            public void onPageScrollStateChanged(int i) {

            }
        });
        adapter = new HearingPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransformerSelfEval());

        if (CurrentCard > -1 && CurrentCard < CardsPool.size()) {
            pager.setCurrentItem(CurrentCard, true);
        } else {
            CurrentCard = 0;
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
    	OpenwordsSharedPreferences.setHearingProgress(new Gson().toJson(new HearingProgress(CardsPool, CurrentCard)));
    	ActivityHearing.super.onBackPressed();
    	//        new AlertDialog.Builder(this)
//                .setTitle("Really Quit?")
//                .setMessage("Are you sure you want to quite current Evaluation? (You progress will be saved)")
//                .setNegativeButton("No", null)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        OpenwordsSharedPreferences.setHearingProgress(new Gson().toJson(new HearingProgress(CardsPool, CurrentCard)));
//                        ActivityHearing.super.onBackPressed();
//                    }
//                }).create().show();
    }

    private class HearingPagerAdapter extends FragmentPagerAdapter {

    	HearingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            LogUtil.logDeubg(this, "Request fragment: " + i);
            if (i >= CardsPool.size()) {
                return new FragmentPlateCompletion();
            } else {
                return new FragmentHearing(i);
            }
        }

        @Override
        public int getCount() {
            return CardsPool.size();
        }
    }
}
