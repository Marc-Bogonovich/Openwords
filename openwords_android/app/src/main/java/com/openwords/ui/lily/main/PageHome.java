package com.openwords.ui.lily.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView;
import com.openwords.R;
import com.openwords.learningmodule.MyPager;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.ui.common.BackButtonBehavior;
import com.openwords.util.log.LogUtil;

public class PageHome extends FragmentActivity {

    private MyPager pager;
    private TextView logo, languageName, buttonCourses, buttonWords;
    private ImageView setting;
    private View line1, line2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lily_page_home);

        logo = (TextView) findViewById(R.id.page_home_root_text1);
        languageName = (TextView) findViewById(R.id.page_home_root_text2);
        setting = (ImageView) findViewById(R.id.page_home_root_image1);
        buttonCourses = (TextView) findViewById(R.id.page_home_root_button1);
        buttonWords = (TextView) findViewById(R.id.page_home_root_button2);
        line1 = findViewById(R.id.page_home_root_view1);
        line2 = findViewById(R.id.page_home_root_view2);

        languageName.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Dialog d = new DialogLearnLang(PageHome.this, new DialogLearnLang.LanguagePicked() {

                    public void done() {
                        Language currentLearn = LocalSettings.getLearningLanguage();
                        if (currentLearn.displayName.isEmpty()) {
                            languageName.setText(currentLearn.name);
                        } else {
                            languageName.setText(currentLearn.displayName);
                        }
                    }
                });
                d.show();
            }
        });

        pager = (MyPager) findViewById(R.id.page_home_pager);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return FragmentCourse.newInstance();
                } else if (position == 1) {
                    return FragmentWords.newInstance();
                } else {
                    return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    line1.setVisibility(View.VISIBLE);
                    line2.setVisibility(View.INVISIBLE);
                } else if (position == 1) {
                    line2.setVisibility(View.VISIBLE);
                    line1.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buttonCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);

            }
        });
        buttonWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });

        logo.setText("Openwords");
        languageName.setText("中文");

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
        Language currentLearn = LocalSettings.getLearningLanguage();
        if (currentLearn.displayName.isEmpty()) {
            languageName.setText(currentLearn.name);
        } else {
            languageName.setText(currentLearn.displayName);
        }
    }

    @Override
    public void onBackPressed() {
        BackButtonBehavior.whenAtMainPages(this, new BackButtonBehavior.BackActionConfirmed() {

            public void callback() {
                PageHome.super.onBackPressed();
            }
        });
    }

}
