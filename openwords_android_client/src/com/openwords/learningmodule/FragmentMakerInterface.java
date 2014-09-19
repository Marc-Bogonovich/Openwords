package com.openwords.learningmodule;

import android.support.v4.app.Fragment;

public interface FragmentMakerInterface {

    public Fragment makePageFragment(int index);

    public Fragment makePCFragment();
}
