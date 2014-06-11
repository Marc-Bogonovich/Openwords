package com.openwords.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.openwords.R;
import com.openwords.model.Plate;
import com.openwords.util.log.LogUtil;
import java.util.List;

public class ActivitySelfEval extends FragmentActivity {

    private static List<Plate> ProblemPool;
    private static int CurrentQuestion;

    public static List<Plate> getProblemPool() {
        return ProblemPool;
    }

    public static void setProblemPool(List<Plate> ProblemPool) {
        ActivitySelfEval.ProblemPool = ProblemPool;
    }

    public static int getCurrentQuestion() {
        return CurrentQuestion;
    }

    public static void setCurrentQuestion(int CurrentQuestion) {
        ActivitySelfEval.CurrentQuestion = CurrentQuestion;
    }

    private ViewPager pager;
    private SelfEvaluatePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.logDeubg(this, "onCreate");
        setContentView(R.layout.activity_self_eval);

        if (getProblemPool() == null) {
            Toast.makeText(this, "Please give your cards first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setCurrentQuestion(0);
        pager = (ViewPager) findViewById(R.id.act_self_eval_pager);
        pager.setOffscreenPageLimit(1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int i, float f, int i1) {
            }

            public void onPageSelected(int i) {
                if (i == getProblemPool().size()) {
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
            if (i >= getProblemPool().size()) {
                return new FragmentPlateCompletion();
            } else {
                return new FragmentSelfEval(i);
            }
        }

        @Override
        public int getCount() {
            return getProblemPool().size() + 1;
        }

    }
}
