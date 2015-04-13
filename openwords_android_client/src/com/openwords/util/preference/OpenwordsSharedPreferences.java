package com.openwords.util.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.openwords.util.log.LogUtil;

public class OpenwordsSharedPreferences {

    private static final String SHARED_PREFERENCE_FILE = "openwords_preference";
    private static SharedPreferences pref;
    public static final String APP_STARTED = "app.started";
    public static final String HIDE_PORTAL = "app.hide.portal";

    public static void init(Context context) {
        pref = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        LogUtil.logDeubg(OpenwordsSharedPreferences.class, "OpenwordsSharedPreferences initialized");
    }

    public static void clean() {
        pref = null;
    }

    public static boolean isAppStarted() {
        return pref.getBoolean(APP_STARTED, false);
    }

    public static boolean setAppStarted(boolean f) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_STARTED, f);
        return editor.commit();
    }

    public static boolean getHidePortal() {
        boolean hide = pref.getBoolean(HIDE_PORTAL, false);
        return hide;
    }

    public static boolean setHidePortal(boolean hide) {
        Editor editor = pref.edit();
        editor.putBoolean(HIDE_PORTAL, hide);
        return editor.commit();
    }

    public static boolean removePreferenceItem(String itemName) {
        Editor editor = pref.edit();
        editor.remove(itemName);
        return editor.commit();
    }

    private OpenwordsSharedPreferences() {
    }
}
