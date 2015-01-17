package com.openwords.model;

import com.openwords.util.gson.MyGson;
import com.openwords.util.localization.LocalLanguage;
import com.orm.SugarRecord;

public class LocalSettings extends SugarRecord<LocalSettings> {

    private int userId, baseLanguageId;
    private String username, password, localLanguage;
    private boolean remember;

    public LocalSettings() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBaseLanguageId() {
        return baseLanguageId;
    }

    public void setBaseLanguageId(int baseLanguageId) {
        this.baseLanguageId = baseLanguageId;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public LocalLanguage getLocalLanguage() {
        return (LocalLanguage) MyGson.fromJson(localLanguage, LocalLanguage.class);
    }

    public void setLocalLanguage(LocalLanguage lang) {
        localLanguage = MyGson.toJson(lang);
        this.save();
    }
}
