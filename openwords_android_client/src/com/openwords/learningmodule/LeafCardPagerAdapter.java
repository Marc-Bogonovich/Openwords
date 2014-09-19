package com.openwords.learningmodule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LeafCardPagerAdapter extends FragmentPagerAdapter {

    private boolean reverseNav;
    private int cardsPoolSize;
    private FragmentMakerInterface fragmentMaker;

    public LeafCardPagerAdapter(FragmentManager fm, boolean reverseNav, int cardsPoolSize, FragmentMakerInterface fragmentMaker) {
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
                return fragmentMaker.makePageFragment(getReverseCardIndex(i));
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

    private int getReverseCardIndex(int pageIndex) {
        if (pageIndex == 0) {
            return -1;//additional page, so invalid card
        }
        return cardsPoolSize - pageIndex;
    }

}
