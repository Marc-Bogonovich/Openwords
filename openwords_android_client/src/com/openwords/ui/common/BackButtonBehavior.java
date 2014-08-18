package com.openwords.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class BackButtonBehavior {

    public static void whenAtMainPages(final Activity activity, final BackActionConfirmed callback) {
        new AlertDialog.Builder(activity)
                .setTitle("Really?")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (callback != null) {
                            callback.callback();
                        }
                    }
                }).create().show();
    }

    public static void whenAtFirstPage(final Activity activity, final BackActionConfirmed callback) {
        new AlertDialog.Builder(activity)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
