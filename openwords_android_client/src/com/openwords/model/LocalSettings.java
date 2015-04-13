package com.openwords.model;

import com.openwords.util.gson.MyGson;
import com.openwords.util.localization.LocalLanguage;
import com.orm.SugarRecord;
import java.util.HashMap;
import java.util.List;

public class LocalSettings extends SugarRecord<LocalSettings> {

    private static LocalSettings instance;

    private static void loadLocalSettings() {
        if (instance == null) {
            List<LocalSettings> r = LocalSettings.listAll(LocalSettings.class);
            if (r.isEmpty()) {
                instance = new LocalSettings();
            } else {
                instance = r.get(0);
            }
        }
    }

    public static void clearLocalSettings() {
        LocalSettings.deleteAll(LocalSettings.class);
    }

    public static int getUserId() {
        loadLocalSettings();
        return instance.userId;
    }

    public static void setUserId(int userId) {
        loadLocalSettings();
        instance.userId = userId;
        instance.save();
    }

    public static String getUsername() {
        loadLocalSettings();
        return instance.username;
    }

    public static void setUsername(String username) {
        loadLocalSettings();
        instance.username = username;
        instance.save();
    }

    public static String getPassword() {
        loadLocalSettings();
        return instance.password;
    }

    public static void setPassword(String password) {
        loadLocalSettings();
        instance.password = password;
        instance.save();
    }

    public static int getBaseLanguageId() {
        loadLocalSettings();
        return instance.baseLanguageId;
    }

    public static void setBaseLanguageId(int baseLanguageId) {
        loadLocalSettings();
        instance.baseLanguageId = baseLanguageId;
        instance.save();
    }

    public static boolean isRemember() {
        loadLocalSettings();
        return instance.remember;
    }

    public static void setRemember(boolean remember) {
        loadLocalSettings();
        instance.remember = remember;
        instance.save();
    }

    public static LocalLanguage getLocalLanguage() {
        loadLocalSettings();
        return (LocalLanguage) MyGson.fromJson(instance.localLanguage, LocalLanguage.class);
    }

    public static void setLocalLanguage(LocalLanguage lang) {
        loadLocalSettings();
        instance.localLanguage = MyGson.toJson(lang);
        instance.save();
    }

    public static String getPreviousStatsData() {
        loadLocalSettings();
        if (instance.previousStatsData == null) {
            instance.previousStatsData = MyGson.toJson(new HashMap());
        }
        return instance.previousStatsData;
    }

    public static void setPreviousStatsData(String previousStatsData) {
        instance.previousStatsData = previousStatsData;
        instance.save();
    }

    private int userId, baseLanguageId;
    private String username, password, localLanguage, previousStatsData;
    private boolean remember;

    public LocalSettings() {
    }
}
