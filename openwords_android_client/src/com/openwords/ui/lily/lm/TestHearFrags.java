package com.openwords.ui.lily.lm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import com.openwords.R;

/**
 *
 * @author hanaldo
 */
public class TestHearFrags extends FragmentActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_hear_frags);

        pager = (ViewPager) findViewById(R.id.test_hear_frags_pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setPageTransformer(true, new TestPageTransformer());
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return new FragmentHear(i);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
