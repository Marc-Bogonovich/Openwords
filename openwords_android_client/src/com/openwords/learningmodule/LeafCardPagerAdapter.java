package com.openwords.learningmodule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LeafCardPagerAdapter extends FragmentPagerAdapter {

    private boolean reverseNav;
    private int cardsPoolSize;
    private FragmentMaker fragmentMaker;

    public LeafCardPagerAdapter(FragmentManager fm, boolean reverseNav, int cardsPoolSize, FragmentMaker fragmentMaker) {
        super(fm);
        this.reverseNav = reverseNav;
        this.cardsPoolSize = cardsPoolSize;
        this.fragmentMaker = fragmentMaker;
    }

    @Override
    public Fragment getItem(int i) {
        if (reverseNav) {
            if (i <= 0) {
                return fragmentMaker.makePCFragment();
            } else {
                return fragmentMaker.makePageFragment(i);
            }
        } else {
            if (i >= cardsPoolSize) {
                return fragmentMaker.makePCFragment();
            } else {
                return fragmentMaker.makePageFragment(i);
            }
        }
    }

    @Override
    public int getCount() {
        return cardsPoolSize + 1;
    }

    public interface FragmentMaker {

        public Fragment makePageFragment(int index);

        public Fragment makePCFragment();
    }
}
