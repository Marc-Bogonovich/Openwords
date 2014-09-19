package com.openwords.learningmodule;

public abstract class FragmentMaker implements FragmentMakerInterface {

    private ActivityLM activityInstance;

    public void setActivityInstance(ActivityLM act) {
        activityInstance = act;
    }

    public ActivityLM getActivityInstance() {
        return activityInstance;
    }
}
