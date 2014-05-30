package com.openwords.view.actionbar;

import com.openwords.R;
import com.openwords.ui.common.ActionBarBuilderForActivity;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.view.HomePage;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

@Deprecated
public class ActionBarIcons {
	static Activity activity;
	public static void builder(Activity act) {
		activity = act;
		ActionBarBuilderForActivity actionBuilder = new ActionBarBuilderForActivity(activity);
		
		if(activity.getClass().getSimpleName().equals("StatsPage")) {
			actionBuilder.highlightSecondItem();
		} else if(activity.getClass().getSimpleName().equals("WordsPage")) {
			actionBuilder.highlightThirdItem();
		} else if(activity.getClass().getSimpleName().equals("HomePage")) {
			actionBuilder.highlightFourthItem();
		}
        //                .showBackButton(new ActionBarBuilderForActivity.ActionBarItemClickAction() {
        //
        //                    public void clicked() {
        //                        Toast.makeText(HomePage.this, "Hi, the Back Button is clicked", Toast.LENGTH_SHORT).show();
        //                    }
        //                })
        //                .showTitle(R.id.actionbar_title, R.string.title_activity_homePage, new ActionBarBuilderForActivity.ActionBarItemClickAction() {
        //
        //                    public void clicked() {
        //                        Toast.makeText(HomePage.this, "Hi, the title is clicked", Toast.LENGTH_SHORT).show();
        //                    }
        //                })
		actionBuilder.showFirstItem(R.drawable.ic_actionbar_others, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

            public void clicked() {
                clickItemOther();
            }
        })
        .showSecondItem(R.drawable.ic_actionbar_stats, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

            public void clicked() {
                statsItemClick();
            }
        })
        .showThirdItem(R.drawable.ic_actionbar_words, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

            public void clicked() {
                wordsItemClick();
            }
        })
        .showFourthItem(R.drawable.ic_actionbar_home, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

            public void clicked() {
            	homesItemClick();
            }
        });
	}
	
	private static void homesItemClick() {
		activity.startActivity(new Intent(activity, HomePage.class));
    }
	
	private static void wordsItemClick() {
		activity.startActivity(new Intent(activity, WordsPage.class));
    }

	private static void statsItemClick() {

    	activity.startActivity(new Intent(activity, StatsPage.class));
    }

	private static void profileItemClick() {
    	activity.startActivity(new Intent(activity, ProfilePage.class));
    }

	private static void settingsItemClick() {
    	activity.startActivity(new Intent(activity, SettingsPage.class));
    }

    public static void tutorialItemClick() {
    	activity.startActivity(new Intent(activity, TutorialPage.class));
    }
    private static void clickItemOther() {
        final DialogForSettingSelection dialog[] = new DialogForSettingSelection[1];
        dialog[0] = new DialogForSettingSelection(activity)
                .addItem("Profile")
                .addItem("Setting")
                .addItem("Tutorial")
                .addItem("Log Out")
                .build(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> av, View view, int position, long l) {
                        switch (position) {
                        	case 0: 
                        		profileItemClick();
                        	break;
                        	case 1:
                        		settingsItemClick();
                        		break;
                        	case 2:
                        		tutorialItemClick();
                        		break;
                        	case 3:
                        		activity.finish();
                        		break;
                        }
                        dialog[0].cancel();
                    }
                }, 10, 60);
        dialog[0].show();
    }
}
