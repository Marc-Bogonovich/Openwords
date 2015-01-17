package com.openwords.model;

import android.content.Context;
import com.openwords.services.implementations.GetUserLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.gson.MyGson;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.MyQuickToast;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.LinkedList;
import java.util.List;

public class UserLearningLanguages extends SugarRecord<UserLearningLanguages> {

    public static void loadUserLearningLanguagesFromRemote(final Context context, int userId, final int baseLang, final HttpResultHandler resultHandler) {
        new GetUserLanguages().doRequest(new RequestParamsBuilder()
                .addParam("userId", String.valueOf(userId))
                .addParam("langOneId", String.valueOf(baseLang))
                .getParams(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<Integer> ids = (List<Integer>) resultObject;
                        saveUserLearningLanguagesToLocal(baseLang, ids);
                        resultHandler.hasResult(ids);
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(context, "Cannot retrieve user language preference");
                        resultHandler.noResult(null);
                    }
                });
    }

    public static List<Integer> loadUserLearningLanguagesFromLocal(int baseLang) {
        List<UserLearningLanguages> r = Select.from(UserLearningLanguages.class)
                .where(Condition.prop("base_lang").eq(String.valueOf(baseLang)))
                .list();

        if (r.isEmpty()) {
            LogUtil.logDeubg(UserLearningLanguages.class, "No learning langs for base lang " + baseLang);
            return new LinkedList<Integer>();
        } else {
            List<Integer> ids = r.get(0).getLearningLangs();
            LogUtil.logDeubg(UserLearningLanguages.class, "Got current learning languages for " + baseLang + ":\n"
                    + MyGson.toJson(ids));
            return ids;
        }
    }

    public static void saveUserLearningLanguagesToLocal(int baseLang, List<Integer> learningLangs) {
        UserLearningLanguages.deleteAll(UserLearningLanguages.class, "base_lang = ?", String.valueOf(baseLang));
        new UserLearningLanguages(baseLang, learningLangs).save();
        LogUtil.logDeubg(UserLearningLanguages.class, "UserLearningLanguages data:\n"
                + MyGson.toJson(UserLearningLanguages.listAll(UserLearningLanguages.class)));
    }

    public int baseLang;
    public String learningLangs;

    public UserLearningLanguages() {
    }

    public UserLearningLanguages(int baseLang, String learningLangs) {
        this.baseLang = baseLang;
        this.learningLangs = learningLangs;
    }

    public UserLearningLanguages(int baseLang, List<Integer> learningLangs) {
        this.baseLang = baseLang;
        this.learningLangs = MyGson.toJson(new LearningLanguageIdsPack(learningLangs));
    }

    public List<Integer> getLearningLangs() {
        LearningLanguageIdsPack pack = (LearningLanguageIdsPack) MyGson.fromJson(learningLangs, LearningLanguageIdsPack.class);
        return pack.ids;
    }

}
