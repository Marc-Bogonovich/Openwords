package com.openwords.util.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openwords.learningmodule.LeafCardInstanceCreator;
import com.openwords.learningmodule.LearningModuleType;
import com.openwords.learningmodule.ProgressLM;
import com.openwords.model.LeafCard;
import com.openwords.model.UserInfo;
import com.openwords.util.WSAinterface;
import com.openwords.util.log.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class OpenwordsSharedPreferences {

    private static final String SHARED_PREFERENCE_FILE = "openwords_preference";
    private static SharedPreferences pref;
    public static final String APP_STARTED = "app.started";
    public static final String USER_INFO = "user.info";
    public static final String SELF_EVALUATION_PROGRESS = "app.selfeval.progress";
    public static final String REVIEW_PROGRESS = "app.review.progress";
    public static final String HEARING_PROGRESS = "app.hearing.progress";
    public static final String TYPE_EVALUATION_PROGRESS = "app.typeeval.progress";
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

    public static boolean setUserInfo(UserInfo user) {
        Editor editor = pref.edit();
        editor.putString(USER_INFO, user.toString());
        return editor.commit();
    }

    /**
     *
     * @return If UserInfo not exist will then return null
     */
    public static UserInfo getUserInfo() {
        String json = pref.getString(USER_INFO, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, UserInfo.class);
    }

    public static boolean setLMProgress(String key, String json) {
        Editor editor = pref.edit();
        editor.putString(key, json);
        return editor.commit();
    }

    public static ProgressLM getReviewProgress() {
        String json = pref.getString(REVIEW_PROGRESS, null);
        if (json == null) {
            return null;
        }
        return new GsonBuilder().registerTypeAdapter(LeafCard.class,
                new LeafCardInstanceCreator(LearningModuleType.LM_Review)).create().fromJson(json, ProgressLM.class);
    }

    public static ProgressLM getSelfEvaluationProgress() {
        String json = pref.getString(SELF_EVALUATION_PROGRESS, null);
        if (json == null) {
            return null;
        }
        return new GsonBuilder().registerTypeAdapter(LeafCard.class,
                new LeafCardInstanceCreator(LearningModuleType.LM_SelfEvaluation)).create().fromJson(json, ProgressLM.class);
    }

    public static ProgressLM getTypeEvaluationProgress() {
        String json = pref.getString(TYPE_EVALUATION_PROGRESS, null);
        if (json == null) {
            return null;
        }
        return new GsonBuilder().registerTypeAdapter(LeafCard.class,
                new LeafCardInstanceCreator(LearningModuleType.LM_TypeEvaluation)).create().fromJson(json, ProgressLM.class);
    }

    public static ProgressLM getHearingProgress() {
        String json = pref.getString(HEARING_PROGRESS, null);
        if (json == null) {
            return null;
        }
        return new GsonBuilder().registerTypeAdapter(LeafCard.class,
                new LeafCardInstanceCreator(LearningModuleType.LM_HearingEvaluation)).create().fromJson(json, ProgressLM.class);
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
