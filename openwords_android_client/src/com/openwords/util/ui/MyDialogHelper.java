package com.openwords.util.ui;

import android.app.ProgressDialog;
import android.content.Context;

public class MyDialogHelper {

    private static ProgressDialog progressDialog;

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

    private MyDialogHelper() {
    }
}
