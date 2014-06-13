package com.openwords.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.util.log.LogUtil;
import java.util.List;

public class ActivitySelfEval extends FragmentActivity {

    private static List<LeafCardSelfEval> CardsPool;
    private static int CurrentCard;

    public static List<LeafCardSelfEval> getCardsPool() {
        return CardsPool;
    }

    public static void setCardsPool(List<LeafCardSelfEval> CardsPool) {
        ActivitySelfEval.CardsPool = CardsPool;
    }

    public static int getCurrentCard() {
        return CurrentCard;
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

        CurrentCard = 0;
        pager = (ViewPager) findViewById(R.id.act_self_eval_pager);
        pager.setOffscreenPageLimit(1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int i, float f, int i1) {
            }

            public void onPageSelected(int i) {
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
