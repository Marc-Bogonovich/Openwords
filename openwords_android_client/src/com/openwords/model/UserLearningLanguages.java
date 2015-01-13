package com.openwords.model;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openwords.services.implementations.GetUserLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.ui.MyQuickToast;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.List;

public class UserLearningLanguages extends SugarRecord<UserLearningLanguages> {

    public static void loadAndMergeUserLanguagePreferenceRemotely(final Context context, int newUserId, final int baseLang, final HttpResultHandler resultHandler) {
        new GetUserLanguages().doRequest(new RequestParamsBuilder()
                .addParam("userId", String.valueOf(newUserId))
                .addParam("langOneId", String.valueOf(baseLang))
                .getParams(),
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        DataPool.CurrentLearningLanguages.clear();
                        @SuppressWarnings("unchecked")
                        List<Integer> ids = (List<Integer>) resultObject;
                        DataPool.CurrentLearningLanguages.addAll(ids);

                        UserLearningLanguages.deleteAll(UserLearningLanguages.class, "BASE_LANG = ?", String.valueOf(baseLang));
                        new UserLearningLanguages(baseLang, new Gson().toJson(DataPool.CurrentLearningLanguages)).save();

                        MyQuickToast.showShort(context, "CurrentLearningLanguages:\n"
                                + new Gson().toJson(DataPool.CurrentLearningLanguages));
                        MyQuickToast.showLong(context, "database:\n"
                                + new Gson().toJson(UserLearningLanguages.listAll(UserLearningLanguages.class)));
                        resultHandler.hasResult(null);
                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(context, "Cannot retrieve user language preference");
                        resultHandler.noResult(null);
                    }
                });
    }

    public static void loadUserLanguagePreferenceLocally(final Context context, final int baseLang) {
        List<UserLearningLanguages> r = Select.from(UserLearningLanguages.class)
                .where(Condition.prop("BASE_LANG").eq(String.valueOf(baseLang)))
                .list();

        if (r.isEmpty()) {
            MyQuickToast.showShort(context, "No preference for lang " + baseLang);
        } else {
            DataPool.CurrentLearningLanguages.clear();
            DataPool.CurrentLearningLanguages.addAll((List<Integer>) new Gson().fromJson(
                    r.get(0).learningLang,
                    new TypeToken<List<Integer>>() {
                    }.getType()));
            MyQuickToast.showShort(context, "CurrentLearningLanguages:\n"
                    + new Gson().toJson(DataPool.CurrentLearningLanguages));
        }
    }

    public int baseLang;
    public String learningLang;

    public UserLearningLanguages() {
    }

    public UserLearningLanguages(int baseLang, String learningLang) {
        this.baseLang = baseLang;
        this.learningLang = learningLang;
    }

}
