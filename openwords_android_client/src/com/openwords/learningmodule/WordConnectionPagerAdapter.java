package com.openwords.learningmodule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.openwords.model.DataPool;

public class WordConnectionPagerAdapter extends FragmentPagerAdapter {

    private ActivityLearning activityInstance;

    public WordConnectionPagerAdapter(FragmentManager fm, ActivityLearning activityInstance) {
        super(fm);
        this.activityInstance = activityInstance;
    }

    @Override
    public Fragment getItem(int i) {
        if (DataPool.LmReverseNav) {
            if (i <= 0) {
                return makePCFragment();
            } else {
                return makePageFragment(getReverseCardIndex(i));
            }
        } else {
            if (i >= DataPool.LmPool.size()) {
                return makePCFragment();
            } else {
                return makePageFragment(i);
            }
        }
    }

    private Fragment makePageFragment(int index) {
        switch (DataPool.LmType) {
            case LM_Review:
                return new FragmentCardReview(index, activityInstance);
            default:
                return null;
        }
    }

    public Fragment makePCFragment() {
        switch (DataPool.LmType) {
            case LM_Review:
                return new FragmentPCReview();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return DataPool.LmPool.size() + 1;
    }

    private int getReverseCardIndex(int pageIndex) {
        if (pageIndex == 0) {
            return -1;//additional page, so invalid card
        }
        return DataPool.LmPool.size() - pageIndex;
    }

}
