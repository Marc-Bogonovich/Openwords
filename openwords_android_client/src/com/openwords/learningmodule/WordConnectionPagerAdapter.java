package com.openwords.learningmodule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.openwords.model.DataPool;
import com.openwords.ui.lily.lm.PageHear;
import com.openwords.ui.lily.lm.PageReview;
import com.openwords.ui.lily.lm.PageSelf;
import com.openwords.util.log.LogUtil;

public class WordConnectionPagerAdapter extends FragmentPagerAdapter implements InterfaceLearningModule {

    private final ActivityLearning activityInstance;
    private final Fragment[] allFragments;
    private final int maxFragmentSize;

    public WordConnectionPagerAdapter(FragmentManager fm, ActivityLearning activityInstance, int maxFragmentSize) {
        super(fm);
        this.activityInstance = activityInstance;
        this.maxFragmentSize = maxFragmentSize;
        allFragments = new Fragment[maxFragmentSize];
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
            if (i >= DataPool.getPoolSize()) {
                return makePCFragment();
            } else {
                return makePageFragment(i);
            }
        }
    }

    private Fragment makePageFragment(int index) {
        //seems ok so far......
        switch (DataPool.LmType) {
            case Learning_Type_Review:
                if (allFragments[index] == null) {
                    allFragments[index] = new PageReview(index, activityInstance);
                }
                break;
            case Learning_Type_Self:
                if (allFragments[index] == null) {
                    allFragments[index] = new PageSelf(index, activityInstance);
                }
                break;
            case Learning_Type_Type:
                if (allFragments[index] == null) {
                    allFragments[index] = new FragmentCardTypeEval(index, activityInstance);
                }
                break;
            case Learning_Type_Hearing:
                if (allFragments[index] == null) {
                    allFragments[index] = new PageHear(index, activityInstance);
                }
                break;
            default:
                break;
        }
        return allFragments[index];
    }

    public Fragment makePCFragment() {
        return new FragmentPlateCompletion();
    }

    @Override
    public int getCount() {
        return DataPool.getPoolSize() + 1;
    }

    private int getReverseCardIndex(int pageIndex) {
        if (pageIndex == 0) {
            return -1;//additional page, so invalid card
        }
        return DataPool.getPoolSize() - pageIndex;
    }

    public Fragment getRecentFragment(int index) {
        if (index >= maxFragmentSize || index < 0) {
            return null;
        }
        LogUtil.logDeubg(this, "getRecentFragment: " + index);
        return allFragments[index];
    }
}
