package com.openwords.model;

import android.content.Context;
import com.google.gson.Gson;
import com.openwords.services.implementations.GetLanguages;
import com.openwords.services.implementations.GetLearnableLanguages;
import com.openwords.services.interfaces.HttpResultHandler;
import com.openwords.services.interfaces.RequestParamsBuilder;
import com.openwords.util.ui.MyDialogHelper;
import com.openwords.util.ui.MyQuickToast;
import com.orm.SugarRecord;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Language extends SugarRecord<Language> {

    public static void checkAndMergeNewLanguages(final Context context, final int baseLang, final HttpResultHandler resultHandler) {
        MyDialogHelper.tryShowQuickProgressDialog(context, "Checking new languages...");

        new GetLearnableLanguages().doRequest(new RequestParamsBuilder()
                .addParam("langOneId", String.valueOf(baseLang))
                .getParams(), new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        @SuppressWarnings("unchecked")
                        List<Integer> learnableIds = (List<Integer>) resultObject;
                        if (learnableIds.isEmpty()) {
                            MyDialogHelper.tryDismissQuickProgressDialog();
                            MyQuickToast.showLong(context,
                                    "Sorry, seems we don't have learnable languages for your local language yet,\n"
                                    + "but please have a look round or change your native language");
                            resultHandler.noResult(null);
                            return;
                        }

                        String sqlIds = learnableIds.toString().replace("[", "(").replace("]", ")");

                        String sql = "SELECT * FROM LANGUAGE WHERE LANG_ID IN " + sqlIds;
                        List<Language> localSameLangs = Language.findWithQuery(Language.class, sql);
                        MyQuickToast.showShort(context, "total local same: " + localSameLangs.size());

                        Set<Integer> newLangIds = new HashSet<Integer>(learnableIds);
                        for (Language lang : localSameLangs) {
                            if (newLangIds.contains(lang.langId)) {
                                newLangIds.remove(lang.langId);
                            }
                        }
                        MyQuickToast.showShort(context, "total new: " + newLangIds.size());

                        if (!newLangIds.isEmpty()) {
                            getAndSaveLanguageInformation(context, newLangIds, resultHandler);
                        } else {
                            MyDialogHelper.tryDismissQuickProgressDialog();
                            MyQuickToast.showShort(context, "no new learnable languages");
                            resultHandler.noResult(null);
                        }
                    }

                    public void noResult(String errorMessage) {
                        MyDialogHelper.tryDismissQuickProgressDialog();
                        MyQuickToast.showShort(context, "Cannot connect to server: " + errorMessage);
                        resultHandler.noResult(null);
                    }
                });
    }

    private static void getAndSaveLanguageInformation(final Context context, Collection<Integer> langIds, final HttpResultHandler resultHandler) {
        new GetLanguages().doRequest(new RequestParamsBuilder()
                .addParam("include", new Gson().toJson(langIds))
                .getParams(), new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        MyDialogHelper.tryDismissQuickProgressDialog();
                        @SuppressWarnings("unchecked")
                        List<Language> langs = (List<Language>) resultObject;
                        Language.saveInTx(langs);
                        MyQuickToast.showShort(context, "local total: " + Language.count(Language.class));
                        resultHandler.hasResult(null);
                    }

                    public void noResult(String errorMessage) {
                        MyDialogHelper.tryDismissQuickProgressDialog();
                        MyQuickToast.showShort(context, "not ok: " + errorMessage);
                        resultHandler.noResult(null);
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
