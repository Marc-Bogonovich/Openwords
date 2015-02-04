package com.openwords.learningmodule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.openwords.model.LeafCard;
import java.util.List;

public class LeafCardPagerAdapter extends FragmentPagerAdapter {

    private boolean reverseNav;
    private List<LeafCard> cards;
    private LearningModuleType type;
    private ActivityLM activityInstance;

    public LeafCardPagerAdapter(FragmentManager fm, LearningModuleType type, ActivityLM activityInstance, boolean reverseNav, List<LeafCard> cards) {
        super(fm);
        this.reverseNav = reverseNav;
        this.cards = cards;
        this.type = type;
        this.activityInstance = activityInstance;
    }

    @Override
    public Fragment getItem(int i) {
        if (reverseNav) {
            if (i <= 0) {
                return makePCFragment();
            } else {
                return makePageFragment(getReverseCardIndex(i));
            }
        } else {
            if (i >= cards.size()) {
                return makePCFragment();
            } else {
                return makePageFragment(i);
            }
        }
    }

    private Fragment makePageFragment(int index) {
        switch (type) {
            case LM_Review:
                return null;
            case LM_SelfEvaluation:
                return new FragmentCardSelfEval(index, cards, activityInstance);
            case LM_TypeEvaluation:
                return new FragmentCardTypeEval(index, cards, activityInstance);
            case LM_HearingEvaluation:
                return new FragmentCardHearing(index, cards, activityInstance);
            default:
                return null;
        }
    }

    public Fragment makePCFragment() {
        switch (type) {
            case LM_Review:
                return new FragmentPCReview();
            case LM_SelfEvaluation:
                return new FragmentPCSelfEval(cards);
            case LM_TypeEvaluation:
                return new FragmentPCTypeEval(cards);
            case LM_HearingEvaluation:
                return new FragmentPCHearing(cards);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return cards.size() + 1;
    }

    private int getReverseCardIndex(int pageIndex) {
        if (pageIndex == 0) {
            return -1;//additional page, so invalid card
        }
        return cards.size() - pageIndex;
    }

}
