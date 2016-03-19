package com.openwords.ui.lily.lm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.openwords.R;

import java.util.LinkedList;
import java.util.List;

public class ActLesson extends AppCompatActivity {

    public static ActLesson instance;
    private List<Fragment> allFragments;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        allFragments = new LinkedList<>();

        pager = (ViewPager) findViewById(R.id.act_lesson_pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                PageLessonSection frag = new PageLessonSection();
                Bundle args = new Bundle();
                args.putInt("index", position);
                frag.setArguments(args);
                allFragments.add(position, frag);
                return allFragments.get(position);
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                PageLessonSection frag = (PageLessonSection) allFragments.get(position);
                frag.buildUI();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void goToNextPage() {
        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
    }
}
