package com.openwords.util.ui;

import android.content.Context;
import android.widget.Toast;

public class MyQuickToast {

    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private MyQuickToast() {
    }
}
