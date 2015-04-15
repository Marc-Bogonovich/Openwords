package com.openwords.ui.other;

import android.app.Activity;
import com.openwords.R;
import com.openwords.ui.common.ActionBarBuilderForActivity;

public class BackIcons {

    private static Activity activity;

    public static void enable(Activity act) {
        activity = act;
        ActionBarBuilderForActivity actionBuilder = new ActionBarBuilderForActivity(activity);
        int targetTitle = R.string.app_name;
        if (activity.getClass().getSimpleName().contains("ProfilePage")) {
            targetTitle = R.string.title_activity_profilePage;
        } else if (activity.getClass().getSimpleName().contains("SettingsPage")) {
            targetTitle = R.string.title_activity_settingsPage;
        } else if (activity.getClass().getSimpleName().contains("TutorialPage")) {
            targetTitle = R.string.title_activity_tutorial_page;
        }
        actionBuilder.showBackButton(new ActionBarBuilderForActivity.ActionBarItemClickAction() {
            public void clicked() {
                activity.finish();
            }
        })
                .showTitle(R.id.actionbar_title, targetTitle, null);
    }

    private BackIcons() {
    }
}
