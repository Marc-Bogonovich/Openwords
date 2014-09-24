package com.openwords.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.openwords.util.localization.LocalizationManager;

public class BackButtonBehavior {

    public static void whenAtMainPages(final Activity activity, final BackActionConfirmed callback) {
        new AlertDialog.Builder(activity)
                .setTitle(LocalizationManager.getTextAreYouSure())
                .setMessage(LocalizationManager.getTextLogoutContent())
                .setNegativeButton(LocalizationManager.getTextNo(), null)
                .setPositiveButton(LocalizationManager.getTextYes(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (callback != null) {
                            callback.callback();
                        }
                    }
                }).create().show();
    }

    public static void whenAtFirstPage(final Activity activity, final BackActionConfirmed callback) {
        new AlertDialog.Builder(activity)
                .setTitle(LocalizationManager.getTextAreYouSure())
                .setMessage(LocalizationManager.getTextExitContent())
                .setNegativeButton(LocalizationManager.getTextNo(), null)
                .setPositiveButton(LocalizationManager.getTextYes(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (callback != null) {
                            callback.callback();
                        }
                    }
                }).create().show();
    }

    private BackButtonBehavior() {
    }

    public interface BackActionConfirmed {

        public void callback();
    }
}
