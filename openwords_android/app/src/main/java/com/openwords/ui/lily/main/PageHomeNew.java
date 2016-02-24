package com.openwords.ui.lily.main;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.openwords.R;
import com.openwords.model.Language;
import com.openwords.model.LocalSettings;
import com.openwords.util.graphics.MapImageColor;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyDialogHelper;


public class PageHomeNew extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private TextView languageName;
    private ImageView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//for testing purpose
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.md_page_home_new);
        setTitle("");

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        MapImageColor.mapColor(myDrawer.findViewById(R.id.drawer_image1), getResources().getColor(R.color.black));
        MapImageColor.mapColor(myDrawer.findViewById(R.id.drawer_image2), getResources().getColor(R.color.black));
        MapImageColor.mapColor(myDrawer.findViewById(R.id.drawer_image3), getResources().getColor(R.color.black));
        MapImageColor.mapColor(myDrawer.findViewById(R.id.drawer_image4), getResources().getColor(R.color.black));
        MapImageColor.mapColor(myDrawer.findViewById(R.id.drawer_image5), getResources().getColor(R.color.black));
        MapImageColor.mapColor(myDrawer.findViewById(R.id.drawer_image6), getResources().getColor(R.color.black));
        MapImageColor.mapColor(myDrawer.findViewById(R.id.drawer_image7), getResources().getColor(R.color.black));
        MapImageColor.mapColor(myDrawer.findViewById(R.id.drawer_image8), getResources().getColor(R.color.black));
        myDrawer.findViewById(R.id.drawer_item8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialogHelper.showMessageDialog(PageHomeNew.this, "Disclaimer", "This software is a non-commercial application only for personal demonstration purposes, all the data content in this software belong to the original distributors.", null);
            }
        });

        toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.md_pager_action_bar, null);

                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setCustomView(actionBarLayout);

                languageName = (TextView) actionBarLayout.findViewById(R.id.bar_home_text1);
                setting = (ImageView) actionBarLayout.findViewById(R.id.bar_home_image1);

                languageName.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Dialog d = new DialogLearnLang(PageHomeNew.this, new DialogLearnLang.LanguagePicked() {

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

                setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myDrawer != null) {
                            myDrawer.openDrawer(Gravity.RIGHT);
                        }
                    }
                });
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, myDrawer, 0, 0);
        myDrawer.setDrawerListener(mDrawerToggle);

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 2) {
                    case 0:
                        return FragmentCourse.newInstance();
                    case 1:
                        return FragmentWords.newInstance();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 2) {
                    case 0:
                        return "courses";
                    case 1:
                        return "words";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "android.resource://com.openwords/" + R.drawable.img_blue);
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.main_app_color,
                                "android.resource://com.openwords/" + R.drawable.img_orange);
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null)
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });

        MyDialogHelper.showMessageDialog(this, "Disclaimer", "This software is a non-commercial application only for personal demonstration purposes, all the data content in this software belong to the original distributors.", null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.logDeubg(this, "onResume");
        if (languageName != null) {
            Language currentLearn = LocalSettings.getLearningLanguage();
            if (currentLearn.displayName.isEmpty()) {
                languageName.setText(currentLearn.name);
            } else {
                languageName.setText(currentLearn.displayName);
            }
        }
    }
}
