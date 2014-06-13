package com.openwords.test;

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
import com.openwords.model.LeafCardSelfEval;
import com.openwords.util.log.LogUtil;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import java.util.List;

public class ActivitySelfEval extends FragmentActivity {

    private static List<LeafCardSelfEval> CardsPool;
    private static int CurrentCard = -1;

    public static List<LeafCardSelfEval> getCardsPool() {
        return CardsPool;
    }

    public static void setCardsPool(List<LeafCardSelfEval> CardsPool) {
        ActivitySelfEval.CardsPool = CardsPool;
    }

    public static void setCurrentCard(int CurrentCard) {
        ActivitySelfEval.CurrentCard = CurrentCard;
    }

    private ViewPager pager;
    private SelfEvaluatePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        setContentView(R.layout.activity_self_eval);

        if (CardsPool == null) {
            Toast.makeText(this, "Please give your cards first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pager = (ViewPager) findViewById(R.id.act_self_eval_pager);
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
        adapter = new SelfEvaluatePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new PageTransformerSelfEval());

        if (CurrentCard > -1 && CurrentCard < CardsPool.size()) {
            pager.setCurrentItem(CurrentCard, true);
        } else {
            CurrentCard = 0;
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
        new AlertDialog.Builder(this)
                .setTitle("Really Quit?")
                .setMessage("Are you sure you want to quite current Evaluation? (You progress will be saved)")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        OpenwordsSharedPreferences.setSelfEvaluationProgress(new Gson().toJson(new Progress(CardsPool, CurrentCard)));
                        ActivitySelfEval.super.onBackPressed();
                    }
                }).create().show();
    }

    private class SelfEvaluatePagerAdapter extends FragmentPagerAdapter {

        SelfEvaluatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            LogUtil.logDeubg(this, "Request fragment: " + i);
            if (i >= CardsPool.size()) {
                return new FragmentPlateCompletion();
            } else {
                return new FragmentSelfEval(i);
            }
        }

        @Override
        public int getCount() {
            return CardsPool.size() + 1;
        }
    }
}
