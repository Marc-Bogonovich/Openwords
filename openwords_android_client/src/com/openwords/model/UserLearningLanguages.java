package com.openwords.model;

import com.openwords.services.implementations.GetUserLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.gson.MyGson;
import com.openwords.util.log.LogUtil;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.LinkedList;
import java.util.List;

public class UserLearningLanguages extends SugarRecord<UserLearningLanguages> {

    public static UserLearningLanguages getUserLanguageInfo(int baseLang, int learningLang) {
        return Word.find(UserLearningLanguages.class, "base_lang = ? and learning_lang = ?",
                String.valueOf(baseLang),
                String.valueOf(learningLang)
        ).get(0);
    }

    /**
     * Try to load user learning language settings from server first: if it is
     * not successful then load user learning language settings from local; if
     * successful then update the user learning language settings locally.
     *
     * @param userId
     * @param baseLang
     * @param tryRemote If false, not try to connect remote server at all.
     * @param resultHandler Simple callback.
     * @return
     */
    public static List<UserLearningLanguages> loadFreshUserLearningLanguages(int userId, final int baseLang, boolean tryRemote, final ResultUserLearningLanguages resultHandler) {
        if (!tryRemote) {
            return loadUserLearningLanguagesLocal(baseLang);
        }

        new GetUserLanguages().doRequest(
                userId,
                baseLang,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<UserLearningLanguages> langs = (List<UserLearningLanguages>) resultObject;
                        saveOrUpdateAll(baseLang, langs);
                        resultHandler.result(langs);
                    }

                    public void noResult(String errorMessage) {
                        resultHandler.result(loadUserLearningLanguagesLocal(baseLang));
                    }
                });
        return null;
    }

    private static List<UserLearningLanguages> loadUserLearningLanguagesLocal(int baseLang) {
        LogUtil.logDeubg(UserLearningLanguages.class, "loadUserLearningLanguagesLocal()");
        return Select.from(UserLearningLanguages.class)
                .where(Condition.prop("base_lang").eq(String.valueOf(baseLang)))
                .list();
    }

    public static List<Integer> unpackLearningLangIds(List<UserLearningLanguages> langs) {
        List<Integer> l = new LinkedList<Integer>();
        if (langs != null) {
            for (UserLearningLanguages lang : langs) {
                l.add(lang.learningLang);
            }
        }
        return l;
    }

    public static List<UserLearningLanguages> packNewLearningLangs(List<Integer> learningLangs) {
        List<UserLearningLanguages> l = new LinkedList<UserLearningLanguages>();
        if (learningLangs != null) {
            for (Integer lang : learningLangs) {
                l.add(new UserLearningLanguages(-1, lang, 0));//baseLang add later
            }
        }
        return l;
    }

    public static void saveOrUpdateAll(int baseLang, List<UserLearningLanguages> learningLangs) {
        UserLearningLanguages.deleteAll(UserLearningLanguages.class, "base_lang = ?", String.valueOf(baseLang));
        for (UserLearningLanguages lang : learningLangs) {
            lang.baseLang = baseLang;
            lang.save();
        }
        LogUtil.logDeubg(UserLearningLanguages.class, "saveUserLearningLanguagesToLocal():\n"
                + MyGson.toJson(UserLearningLanguages.listAll(UserLearningLanguages.class)));
    }

    public int baseLang, learningLang, page;

    public UserLearningLanguages() {
    }

    public UserLearningLanguages(int baseLang, int learningLang, int page) {
        this.baseLang = baseLang;
        this.learningLang = learningLang;
        this.page = page;
    }
}
