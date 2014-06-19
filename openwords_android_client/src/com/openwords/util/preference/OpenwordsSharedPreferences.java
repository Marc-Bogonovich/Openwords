package com.openwords.util.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.openwords.learningmodule.ActivityReview;
import com.openwords.learningmodule.ActivitySelfEval;
import com.openwords.learningmodule.HearingProgress;
import com.openwords.learningmodule.Progress;
import com.openwords.learningmodule.SelfEvalProgress;
import com.openwords.learningmodule.TypeEvalProgress;
import com.openwords.model.UserInfo;

public class OpenwordsSharedPreferences {

    private static final String SHARED_PREFERENCE_FILE = "openwords_preference";
    private static SharedPreferences pref;
    public static final String APP_STARTED = "app.started";
    public static final String USER_INFO = "user.info";
    public static final String SELF_EVALUATION_PROGRESS = "app.selfeval.progress";
    public static final String REVIEW_PROGRESS = "app.review.progress";
    public static final String HEARING_PROGRESS = "app.hearing.progress";
    public static final String TYPE_EVALUATION_PROGRESS = "app.typeeval.progress";
    
    public static void init(Context context) {
        pref = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
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
    
    public static boolean setReviewProgress(String json) {
        Editor editor = pref.edit();
        editor.putString(REVIEW_PROGRESS, json);
        return editor.commit();
    }

    public static Progress getReviewProgress() {
        String json = pref.getString(REVIEW_PROGRESS, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, Progress.class);
    }
    
    public static SelfEvalProgress getSelfEvaluationProgress() {
    	 String json = pref.getString(SELF_EVALUATION_PROGRESS, null);
         if (json == null) {
             return null;
         }
         return new Gson().fromJson(json, SelfEvalProgress.class);
    }
    
    public static boolean setSelfEvaluationProgress(String json) {
        Editor editor = pref.edit();
        editor.putString(SELF_EVALUATION_PROGRESS, json);
        return editor.commit();
    }
    
    public static TypeEvalProgress getTypeEvaluationProgress() {
        String json = pref.getString(TYPE_EVALUATION_PROGRESS, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, TypeEvalProgress.class);
    }
    
    public static boolean setTypeEvaluationProgress(String json) {
        Editor editor = pref.edit();
        editor.putString(TYPE_EVALUATION_PROGRESS, json);
        return editor.commit();
    }
    public static boolean setHearingProgress(String json) {
        Editor editor = pref.edit();
        editor.putString(HEARING_PROGRESS, json);
        return editor.commit();
    }

    public static HearingProgress getHearingProgress() {
        String json = pref.getString(HEARING_PROGRESS, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, HearingProgress.class);
    }

    public static boolean removePreferenceItem(String itemName) {
        Editor editor = pref.edit();
        editor.remove(itemName);
        return editor.commit();
    }

    private OpenwordsSharedPreferences() {
    }
}
