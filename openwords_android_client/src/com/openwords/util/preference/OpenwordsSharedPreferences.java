package com.openwords.util.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.openwords.util.log.LogUtil;

/**
 *
 * @author hanaldo
 */
public class OpenwordsSharedPreferences {

    private static final String SHARED_PREFERENCE_FILE = "openwords_preference";
    public static final String APP_STARTED = "app.started";
    private static OpenwordsSharedPreferences instance;

    public static OpenwordsSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new OpenwordsSharedPreferences(context);
        }
        return instance;
    }
    private SharedPreferences pref;

    private OpenwordsSharedPreferences(Context context) {
        pref = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
    }

    public void clean() {
        pref = null;
        instance = null;
    }

    public boolean isAppStarted() {
        return pref.getBoolean(APP_STARTED, false);
    }

    public boolean setAppStarted(boolean f) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_STARTED, f);
        return editor.commit();
    }

    public boolean setPreferenceItem(String itemName, PreferenceItem item) {
        LogUtil.logDeubg(this, "Write PreferenceItem: " + item.toString());
        Editor editor = pref.edit();
        String json = new Gson().toJson(item);
        editor.putString(itemName, json);
        return editor.commit();
    }

    public boolean removePreferenceItem(String itemName) {
        Editor editor = pref.edit();
        editor.remove(itemName);
        return editor.commit();
    }

}
