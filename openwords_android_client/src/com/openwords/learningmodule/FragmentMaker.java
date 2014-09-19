package com.openwords.learningmodule;

public abstract class FragmentMaker implements FragmentMakerInterface {

    private ActivitySelfEval activityInstance;

    public void setActivityInstance(ActivitySelfEval act) {
        activityInstance = act;
    }

    public ActivitySelfEval getActivityInstance() {
        return activityInstance;
    }
}
