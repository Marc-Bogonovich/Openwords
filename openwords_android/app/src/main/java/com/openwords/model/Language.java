package com.openwords.model;

import android.content.Context;

import com.openwords.services.implementations.ServiceGetLanguages;
import com.openwords.services.implementations.ServiceGetLanguagesForLearn;
import com.openwords.services.implementations.ServiceGetLearnableLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.log.LogUtil;
import com.openwords.util.ui.CallbackOkButton;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Language extends SugarRecord<Language> {

    public static Language getLanguageInfo(int langId) {
        List<Language> r = Select.from(Language.class)
                .where(Condition.prop("lang_id").eq(langId))
                .list();
        if (r.isEmpty()) {
            return null;
        } else {
            return r.get(0);
        }
    }

    public static List<Language> getLearningLanguagesInfo(int baseLang) {
        List<Integer> ids = UserLanguage.unpackLearningLangIds(
                UserLanguage.loadUserLanguageLocally(baseLang));
        if (ids.isEmpty()) {
            return new LinkedList<Language>();
        }
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        String whereSql = "lang_id in " + sqlIds;
        return Select.from(Language.class).where(whereSql).list();
    }

    public static void syncLanguagesData(final Context context, final ResultLanguage resultHandler) {
        Language.deleteAll(Language.class);
        new ServiceGetLanguagesForLearn().doRequest(new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                ServiceGetLanguagesForLearn.Result r = (ServiceGetLanguagesForLearn.Result) resultObject;
                Language.saveInTx(r.result);
                LogUtil.logDeubg(this, "local total Language: " + Language.count(Language.class));
                resultHandler.result(null);
            }

            public void noResult(String errorMessage) {
                resultHandler.result(errorMessage);
            }
        });
    }

    @Deprecated
    public static void syncLanguagesData_old(final Context context, final int baseLang, final ResultLanguage resultHandler) {
        new ServiceGetLearnableLanguages().doRequest(baseLang, new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                @SuppressWarnings("unchecked")
                List<Integer> learnableIds = (List<Integer>) resultObject;
                if (learnableIds.isEmpty()) {
                    MyDialogHelper.tryDismissQuickProgressDialog();
                    MyDialogHelper.showMessageDialog(context, null,
                            "Sorry, seems we don't have learnable languages for your local language yet,\n"
                                    + "but please have a look round or change your native language",
                            new CallbackOkButton() {

                                public void okPressed() {
                                    resultHandler.result(ResultLanguage.Result_No_Language_Data);
                                }
                            });
                    return;
                }

                String sqlIds = learnableIds.toString().replace("[", "(").replace("]", ")");
                String whereSql = "lang_id in " + sqlIds;

                List<Language> localSameLangs = Select.from(Language.class).where(whereSql).list();
                LogUtil.logDeubg(this, "total local same: " + localSameLangs.size());

                Set<Integer> newLangIds = new HashSet<Integer>(learnableIds);
                for (Language lang : localSameLangs) {
                    if (newLangIds.contains(lang.langId)) {
                        newLangIds.remove(lang.langId);
                    }
                }
                LogUtil.logDeubg(this, "total new: " + newLangIds.size());

                if (!newLangIds.isEmpty()) {
                    loadLanguagesInfo(context, newLangIds, resultHandler);
                } else {
                    LogUtil.logDeubg(this, "no new learnable languages");
                    resultHandler.result(null);
                }
            }

            public void noResult(String errorMessage) {
                MyQuickToast.showShort(context, "Cannot connect to server: " + errorMessage);
                resultHandler.result(ResultLanguage.Result_No_Server_Response);
            }
        });
    }

    private static void loadLanguagesInfo(final Context context, Collection<Integer> langIds, final ResultLanguage resultHandler) {
        new ServiceGetLanguages().doRequest(langIds,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<Language> langs = (List<Language>) resultObject;
                        Language.saveInTx(langs);
                        LogUtil.logDeubg(this, "local total: " + Language.count(Language.class));
                        resultHandler.result(null);

                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(context, "Error: " + errorMessage);
                        resultHandler.result(ResultLanguage.Result_No_Server_Response);
                    }
                });
    }

    public int langId, totalWords, totalConnections, totalSounds;
    public String name, code, meta, displayName;

    public Language() {
    }

    public Language(int langId, String name, String code, String meta, String displayName) {
        this.langId = langId;
        this.name = name;
        this.code = code;
        this.meta = meta;
        this.displayName = displayName;
    }

}
