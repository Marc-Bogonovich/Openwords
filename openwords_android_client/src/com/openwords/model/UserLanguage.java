package com.openwords.model;

import com.openwords.services.implementations.ServiceGetUserLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.gson.MyGson;
import com.openwords.util.log.LogUtil;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.LinkedList;
import java.util.List;

public class UserLanguage extends SugarRecord<UserLanguage> {

    public static UserLanguage getUserLanguageInfo(int baseLang, int learningLang) {
        return Select.from(UserLanguage.class)
                .where("base_lang = ? and learning_lang = ?",
                        new String[]{String.valueOf(baseLang), String.valueOf(learningLang)})
                .list().get(0);
    }

    /**
     * Try to load user learning language settings from server first: if it is
     * not successful then load user learning language settings from local; if
     * successful then update the user learning language settings locally.
     *
     * @param userId
     * @param baseLang
     * @param resultHandler Simple callback.
     */
    public static void syncUserLanguage(int userId, final int baseLang, final ResultUserLanguage resultHandler) {
        new ServiceGetUserLanguages().doRequest(
                userId,
                baseLang,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<UserLanguage> langs = (List<UserLanguage>) resultObject;
                        for (UserLanguage lang : langs) {
                            if (!lang.use) {
                                langs.remove(lang);
                            }
                        }
                        saveOrUpdateAll(baseLang, langs);
                        resultHandler.result(langs);
                    }

                    public void noResult(String errorMessage) {
                        resultHandler.result(loadUserLanguageLocally(baseLang));
                    }
                });
    }

    public static List<UserLanguage> loadUserLanguageLocally(int baseLang) {
        LogUtil.logDeubg(UserLanguage.class, "loadUserLanguageLocally()");
        return Select.from(UserLanguage.class)
                .where(Condition.prop("base_lang").eq(String.valueOf(baseLang)))
                .list();
    }

    public static List<Integer> unpackLearningLangIds(List<UserLanguage> langs) {
        List<Integer> l = new LinkedList<Integer>();
        if (langs != null) {
            for (UserLanguage lang : langs) {
                l.add(lang.learningLang);
            }
        }
        return l;
    }

    public static List<UserLanguage> packNewLearningLangs(List<Integer> learningLangs) {
        List<UserLanguage> l = new LinkedList<UserLanguage>();
        if (learningLangs != null) {
            for (Integer lang : learningLangs) {
                l.add(new UserLanguage(-1, lang, 0));//baseLang add later
            }
        }
        return l;
    }

    private static void saveOrUpdateAll(int baseLang, List<UserLanguage> learningLangs) {
        UserLanguage.deleteAll(UserLanguage.class, "base_lang = ?", String.valueOf(baseLang));
        for (UserLanguage lang : learningLangs) {
            lang.baseLang = baseLang;
            lang.save();
        }
        LogUtil.logDeubg(UserLanguage.class, "UserLanguage:\n"
                + MyGson.toJson(UserLanguage.listAll(UserLanguage.class)));
    }

    public int baseLang, learningLang, page;
    public boolean use;

    public UserLanguage() {
    }

    public UserLanguage(int baseLang, int learningLang, int page) {
        this.baseLang = baseLang;
        this.learningLang = learningLang;
        this.page = page;
    }
}
