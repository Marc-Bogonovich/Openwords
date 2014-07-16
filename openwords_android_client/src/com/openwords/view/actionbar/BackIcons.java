	package com.openwords.view.actionbar;

	import com.openwords.R;
import com.openwords.ui.common.ActionBarBuilderForActivity;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.view.HomePage;

	import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
	
public class BackIcons {
	static Activity activity;
	public static void builder(Activity act) {
		activity = act;
		ActionBarBuilderForActivity actionBuilder = new ActionBarBuilderForActivity(activity);
		Integer targetTitle = R.string.title_activity_homePage;
		if(activity.getClass().getSimpleName().equals("ProfilePage")) {
			actionBuilder.highlightButtonItem(1);
			targetTitle = R.string.title_activity_profilePage;
		} else if(activity.getClass().getSimpleName().equals("SettingsPage")) {
			actionBuilder.highlightButtonItem(2);
			targetTitle = R.string.title_activity_settingsPage;
		} else if(activity.getClass().getSimpleName().equals("TutorialPage")) {
			actionBuilder.highlightButtonItem(3);
			targetTitle = R.string.title_activity_tutorial_page;
		}
		actionBuilder.showBackButton(new ActionBarBuilderForActivity.ActionBarItemClickAction() {
            public void clicked() {
            	activity.finish();
            }
        })
        .showTitle(R.id.actionbar_title, targetTitle, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

            public void clicked() {
            	activity.finish();
               //Toast.makeText(activity, "Hi, the title is clicked", Toast.LENGTH_SHORT).show();
            }
        });
	}
}
