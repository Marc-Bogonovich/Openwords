package com.openwords.learningmodule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Hearing;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Review;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Self;
import static com.openwords.learningmodule.InterfaceLearningModule.Learning_Type_Type;
import com.openwords.model.DataPool;
import com.openwords.util.log.LogUtil;

public class WordConnectionPagerAdapter extends FragmentPagerAdapter {

    private ActivityLearning activityInstance;
    private Fragment[] allFragments;
    private final int fragmentSize = 15;

    public WordConnectionPagerAdapter(FragmentManager fm, ActivityLearning activityInstance) {
        super(fm);
        this.activityInstance = activityInstance;
        allFragments = new Fragment[fragmentSize];//10 now
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
        Fragment f;
        //to-do
        //research why make new fragment instance, can we save all of them in memory?
        switch (DataPool.LmType) {
            case Learning_Type_Review:
                f = new FragmentCardReview(index, activityInstance);
                allFragments[index] = f;
                return f;
            case Learning_Type_Self:
                f = new FragmentCardSelfEval(index, activityInstance);
                allFragments[index] = f;
                return f;
            case Learning_Type_Type:
                f = new FragmentCardTypeEval(index, activityInstance);
                allFragments[index] = f;
                return f;
            case Learning_Type_Hearing:
                f = new FragmentCardHearing(index, activityInstance);
                allFragments[index] = f;
                return f;
            default:
                return null;
        }
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
        if (index >= fragmentSize || index < 0) {
            return null;
        }
        LogUtil.logDeubg(this, "getRecentFragment: " + index);
        return allFragments[index];
    }
}
