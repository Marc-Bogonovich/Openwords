package com.openwords.ui.lily.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import com.openwords.R;
import com.openwords.learningmodule.MyPager;

public class PageHome extends FragmentActivity {

    private MyPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lily_page_home);

        pager = (MyPager) findViewById(R.id.page_home_pager);
        pager.setOffscreenPageLimit(1);

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return FragmentCourse.newInstance();
                } else if (position == 1) {
                    return FragmentCourse.newInstance();
                } else {
                    return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

    }

}
