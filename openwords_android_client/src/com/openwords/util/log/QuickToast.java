package com.openwords.util.log;

import android.content.Context;
import android.widget.Toast;

public class QuickToast {

    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private QuickToast() {
    }
}
