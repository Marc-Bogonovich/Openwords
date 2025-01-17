package com.openwords.ui.other;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.openwords.R;
import com.openwords.ui.common.ActionBarBuilderForActivity;
import com.openwords.ui.common.DialogForSettingSelection;
import com.openwords.util.localization.LocalizationManager;

public class ActionBarBuilder {

    public static final int Home_Page = 1;
    public static final int Words_Page = 2;
    public static final int Stats_Page = 3;
    public static final int Portal_Page = 4;
    private final Activity activity;
    private final int currentPage;
    private ActionBarBuilderForActivity builder;

    public ActionBarBuilder(Activity activity, int currentPage) {
        this.activity = activity;
        this.currentPage = currentPage;

        builder = new ActionBarBuilderForActivity(activity);

        builder
                .showButtonItem(0, R.drawable.ic_actionbar_others, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        otherClicked();
                    }
                })
                .showButtonItem(1, R.drawable.ic_actionbar_portal, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        if (ActionBarBuilder.this.currentPage != Portal_Page) {
                            //portalClicked();
                        }
                    }
                })
                .showButtonItem(2, R.drawable.ic_actionbar_stats, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        if (ActionBarBuilder.this.currentPage != Stats_Page) {
                            //statsClicked();
                        }
                    }
                })
                .showButtonItem(3, R.drawable.ic_actionbar_words, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        if (ActionBarBuilder.this.currentPage != Words_Page) {
                            //wordsClicked();
                        }
                    }
                })
                .showButtonItem(4, R.drawable.ic_actionbar_home, new ActionBarBuilderForActivity.ActionBarItemClickAction() {

                    public void clicked() {
                        if (ActionBarBuilder.this.currentPage != Home_Page) {
                            //homeClicked();
                        }
                    }
                });

        switch (this.currentPage) {
            case Home_Page:
                builder.highlightButtonItem(4);
                break;
            case Words_Page:
                builder.highlightButtonItem(3);
                break;
            case Stats_Page:
                builder.highlightButtonItem(2);
                break;
            case Portal_Page:
                builder.highlightButtonItem(1);
                break;
            default:
                break;
        }

        checkSetting();
    }

    public void checkSetting() {
        if (true) {
            builder.hideItem(1);
        } else {
            builder.reshowItem(1);
        }
    }

    private void otherClicked() {
        final DialogForSettingSelection dialog[] = new DialogForSettingSelection[1];
        dialog[0] = new DialogForSettingSelection(activity)
                .addItem(LocalizationManager.getTextSettingProfile())
                .addItem(LocalizationManager.getTextSettingSetting())
                .addItem(LocalizationManager.getTextSettingTutor())
                .addItem(LocalizationManager.getTextSettingLogout())
                .build(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> av, View view, int position, long l) {
                        switch (position) {
                            case 0:
                                Intent i = new Intent(activity, ProfilePage.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                activity.startActivity(i);
                                break;
                            case 1:
                                i = new Intent(activity, SettingsPage.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                activity.startActivity(i);
                                break;
                            case 2:
                                i = new Intent(activity, TutorialPage.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                activity.startActivity(i);
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
