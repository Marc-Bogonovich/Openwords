package com.openwords.model;

import android.content.Context;
import com.openwords.services.implementations.GetLanguages;
import com.openwords.services.implementations.GetLearnableLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.util.ui.CallbackOkButton;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import com.orm.SugarRecord;
import com.orm.query.Select;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Language extends SugarRecord<Language> {

    public static List<Language> getLearningLanguagesInfo(int baseLang) {
        List<Integer> ids = UserLanguage.unpackLearningLangIds(
                UserLanguage.loadUserLanguage(-1, baseLang, false, null));
        if (ids.isEmpty()) {
            return new LinkedList<Language>();
        }
        String sqlIds = ids.toString().replace("[", "(").replace("]", ")");
        String whereSql = "lang_id in " + sqlIds;
        return Select.from(Language.class).where(whereSql).list();
    }

    public static void syncLanguagesData(final Context context, final int baseLang, final ResultLanguage resultHandler) {
        new GetLearnableLanguages().doRequest(baseLang, new HttpResultHandler() {

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
                MyQuickToast.showShort(context, "total local same: " + localSameLangs.size());

                Set<Integer> newLangIds = new HashSet<Integer>(learnableIds);
                for (Language lang : localSameLangs) {
                    if (newLangIds.contains(lang.langId)) {
                        newLangIds.remove(lang.langId);
                    }
                }
                MyQuickToast.showShort(context, "total new: " + newLangIds.size());

                if (!newLangIds.isEmpty()) {
                    loadLanguagesInfo(context, newLangIds, resultHandler);
                } else {
                    MyQuickToast.showShort(context, "no new learnable languages");
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
        new GetLanguages().doRequest(langIds,
                new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<Language> langs = (List<Language>) resultObject;
                        Language.saveInTx(langs);
                        MyQuickToast.showShort(context, "local total: " + Language.count(Language.class));
                        resultHandler.result(null);

                    }

                    public void noResult(String errorMessage) {
                        MyQuickToast.showShort(context, "not ok: " + errorMessage);
                        resultHandler.result(ResultLanguage.Result_No_Server_Response);
                    }
                });
    }

    public int langId;
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
