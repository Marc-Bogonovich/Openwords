package com.openwords.learningmodule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.openwords.model.DataPool;
import com.openwords.ui.lily.lm.PageHear;
import com.openwords.ui.lily.lm.PageReview;
import com.openwords.ui.lily.lm.PageSelf;
import com.openwords.ui.lily.lm.PageSentence;
import com.openwords.util.log.LogUtil;
import java.util.LinkedList;
import java.util.List;

public class WordConnectionPagerAdapter extends FragmentPagerAdapter implements InterfaceLearningModule {

    private final ActivityLearning activityInstance;
    private final List<Fragment> allFragments;

    public WordConnectionPagerAdapter(FragmentManager fm, ActivityLearning activityInstance) {
        super(fm);
        this.activityInstance = activityInstance;
        allFragments = new LinkedList<Fragment>();
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
        LogUtil.logDeubg(this, "make fragment " + index);
        //seems ok so far......try not reuse the reference
        switch (DataPool.LmType) {
            case Learning_Type_Review:
                allFragments.add(index, new PageReview(index, activityInstance));
                break;
            case Learning_Type_Self:
                allFragments.add(index, new PageSelf(index, activityInstance));
                break;
            case Learning_Type_Type:
                allFragments.add(index, new FragmentCardTypeEval(index, activityInstance));
                break;
            case Learning_Type_Hearing:
                allFragments.add(index, new PageHear(index, activityInstance));
                break;
            case Learning_Type_Sentence:
                allFragments.add(index, new PageSentence(index, activityInstance));
                break;
            default:
                break;
        }
        return allFragments.get(index);
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
        if (index < 0) {
            return null;
        }
        LogUtil.logDeubg(this, "getRecentFragment: " + index);
        return allFragments.get(index);
    }
}
