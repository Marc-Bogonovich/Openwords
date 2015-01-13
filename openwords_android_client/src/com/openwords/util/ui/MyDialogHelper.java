package com.openwords.util.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyDialogHelper {

    private static ProgressDialog progressDialog;
    private static Dialog messageDialog;

    public static void tryShowQuickProgressDialog(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(context, null, message, true, false);
        } else {
            MyQuickToast.showShort(context, "The previous dialog is not dismissed yet!");
        }
    }

    public static void tryDismissQuickProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showMessageDialog(Context context, String title, String message, final CallbackOkButton callback) {
        if (messageDialog != null) {
            messageDialog.cancel();
            messageDialog = null;
        }
        messageDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        messageDialog.dismiss();
                        messageDialog = null;
                        if (callback != null) {
                            callback.okPressed();
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface di) {
                        if (callback != null) {
                            callback.okPressed();
                        }
                    }
                })
                .create();
        messageDialog.show();
    }

    private MyDialogHelper() {
    }
}
