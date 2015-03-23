package com.openwords.util.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.openwords.util.WSAinterface;
import com.openwords.util.log.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class OpenwordsSharedPreferences {

    private static final String SHARED_PREFERENCE_FILE = "openwords_preference";
    private static SharedPreferences pref;
    public static final String APP_STARTED = "app.started";
    public static final String HIDE_PORTAL = "app.hide.portal";
    private static int ALG_INDEX = 0;
    private static int LEAF_CARD_SIZE = 10;
    private static List<WSAinterface> wordSelectionAlgList = new ArrayList<WSAinterface>(5);

    public static int getLeafCardSize() {
        return LEAF_CARD_SIZE;
    }

    public static void setLeafCardSize(int lEAF_CARD_SIZE) {
        LEAF_CARD_SIZE = lEAF_CARD_SIZE;
    }

    public static List<WSAinterface> getWordSelectionAlgList() {
        return wordSelectionAlgList;
    }

    //add new algorithm
    public static void addSelectionAlg(
            WSAinterface wordSelectionAlg) {
        OpenwordsSharedPreferences.wordSelectionAlgList.add(wordSelectionAlg);
    }

    public static int getAlgIndex() {
        return ALG_INDEX;
    }

    public static void setAlgIndex(int index) {
        ALG_INDEX = index;
    }

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
