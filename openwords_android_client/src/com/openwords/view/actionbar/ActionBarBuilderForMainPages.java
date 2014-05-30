package com.openwords.view.actionbar;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.openwords.R;
import com.openwords.ui.common.ActionBarBuilderForActivity;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.view.HomePage;

public class ActionBarBuilderForMainPages {

    public static final int Home_Page = 1;
    public static final int Words_Page = 2;
    public static final int Stats_Page = 3;
    private final Activity activity;
    private final int currentPage;

    public ActionBarBuilderForMainPages(Activity activity, int currentPage) {
        this.activity = activity;
        this.currentPage = currentPage;

        ActionBarBuilderForActivity builder = new ActionBarBuilderForActivity(activity);

        builder
                .showFirstItem(R.drawable.ic_actionbar_others, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        otherClicked();
                    }
                })
                .showSecondItem(R.drawable.ic_actionbar_stats, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        if (ActionBarBuilderForMainPages.this.currentPage != Stats_Page) {
                            statsClicked();
                        }
                    }
                })
                .showThirdItem(R.drawable.ic_actionbar_words, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        if (ActionBarBuilderForMainPages.this.currentPage != Words_Page) {
                            wordsClicked();
                        }
                    }
                })
                .showFourthItem(R.drawable.ic_actionbar_home, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        if (ActionBarBuilderForMainPages.this.currentPage != Home_Page) {
                            homeClicked();
                        }
                    }
                });

        switch (this.currentPage) {
            case Home_Page:
                builder.highlightFourthItem();
                break;
            case Words_Page:
                builder.highlightThirdItem();
                break;
            case Stats_Page:
                builder.highlightSecondItem();
                break;
            default:
                break;
        }
    }

    private void homeClicked() {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        Intent i = new Intent(activity, HomePage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(i);
    }

    private void wordsClicked() {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        Intent i = new Intent(activity, WordsPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(i);
    }

    private void statsClicked() {
        activity.finish();
        activity.overridePendingTransition(0, 0);
        Intent i = new Intent(activity, StatsPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(i);
    }

    private void otherClicked() {
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
                                activity.startActivity(new Intent(activity, ProfilePage.class));
                                break;
                            case 1:
                                activity.startActivity(new Intent(activity, SettingsPage.class));
                                break;
                            case 2:
                                activity.startActivity(new Intent(activity, TutorialPage.class));
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
