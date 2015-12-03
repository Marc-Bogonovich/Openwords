package com.openwords.model;

import com.openwords.services.implementations.ServiceGetSets;
import com.openwords.services.implementations.ServiceSetWordSet;
import com.openwords.services.implementations.ServiceUpdateSetItems;
import com.openwords.services.interfaces.HttpResultHandler;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.ArrayList;
import java.util.List;

public class SetInfo extends SugarRecord<SetInfo> {

    /**
     * Make or update Set Info and Set Items together.
     *
     * @param setId Set 0 or less for new set, otherwise will update set info.
     * @param oldName Set null if make new set.
     * @param newName
     * @param items Set null if not update items.
     * @param resultHandler
     */
    public static void saveAll(final long setId, final String oldName, final String newName, final List<SetItem> items, final ResultSetSaveAll resultHandler) {
        if (oldName == null) {
            new ServiceSetWordSet().doRequest(setId, LocalSettings.getUserId(),
                    LocalSettings.getBaseLanguageId(), LocalSettings.getCurrentLearningLanguage(),
                    newName,
                    new HttpResultHandler() {

                        public void hasResult(Object resultObject) {
                            ServiceSetWordSet.Result r = (ServiceSetWordSet.Result) resultObject;
                            DataPool.currentSet.copyAllValues(r.setResult);
                            updateItems(DataPool.currentSet.setId, items, resultHandler);
                        }

                        public void noResult(String errorMessage) {
                            resultHandler.error(errorMessage);
                        }
                    });
        } else {
            if (oldName.equals(newName)) {
                updateItems(setId, items, resultHandler);
            } else {
                new ServiceSetWordSet().doRequest(setId, LocalSettings.getUserId(),
                        LocalSettings.getBaseLanguageId(), LocalSettings.getCurrentLearningLanguage(),
                        newName,
                        new HttpResultHandler() {

                            public void hasResult(Object resultObject) {
                                ServiceSetWordSet.Result r = (ServiceSetWordSet.Result) resultObject;
                                DataPool.currentSet.copyAllValues(r.setResult);
                                updateItems(DataPool.currentSet.setId, items, resultHandler);
                            }

                            public void noResult(String errorMessage) {
                                resultHandler.error(errorMessage);
                            }
                        });
            }
        }
    }

    private static void updateItems(final long setId, final List<SetItem> items, final ResultSetSaveAll resultHandler) {
        if (items == null) {
            resultHandler.ok();
            return;
        }
        List<Long> wordOnes = new ArrayList<Long>(items.size());
        List<Long> wordTwos = new ArrayList<Long>(items.size());
        for (SetItem item : items) {
            wordOnes.add(item.wordOneId);
            wordTwos.add(item.wordTwoId);
        }
        new ServiceUpdateSetItems().doRequest(setId, LocalSettings.getUserId(),
                wordOnes, wordTwos, new HttpResultHandler() {

                    public void hasResult(Object resultObject) {
                        ServiceUpdateSetItems.Result r = (ServiceUpdateSetItems.Result) resultObject;
                        DataPool.currentSetItems.clear();
                        DataPool.currentSetItems.addAll(r.itemsResult);
                        resultHandler.ok();
                    }

                    public void noResult(String errorMessage) {
                        resultHandler.error(errorMessage);
                    }
                });
    }

    private static void refreshAll(List<SetInfo> sets, int langOne, int langTwo) {
        SetInfo.deleteAll(SetInfo.class, "native_lang = ? and learning_lang = ?", String.valueOf(langOne), String.valueOf(langTwo));
        SetInfo.saveInTx(sets);
    }

    public static List<SetInfo> loadAllSets(int langOne, int langTwo) {
        return Select.from(SetInfo.class)
                .where(Condition.prop("native_lang").eq(langOne), Condition.prop("learning_lang").eq(langTwo))
                .list();
    }

    public static void getAllSets(int pageNumber, int pageSize, final int langOne, final int langTwo, final ResultWordSets resultHandler) {
        new ServiceGetSets().doRequest(pageNumber, pageSize, langOne, langTwo, new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                ServiceGetSets.Result r = (ServiceGetSets.Result) resultObject;
                if (r.result.isEmpty()) {
                    resultHandler.result(null);
                } else {
                    refreshAll(r.result, langOne, langTwo);
                    resultHandler.result(r.result);
                }
            }

            public void noResult(String errorMessage) {
                resultHandler.result(null);
            }
        });
    }

    public long setId, userId;
    public int nativeLang, learningLang, setSize;
    public String name;
    public long updatedTimeLong;

    public SetInfo() {
    }

    public SetInfo(long setId) {
        this.setId = setId;
    }

    public SetInfo(String name) {
        this.name = name;
    }

    public void copyAllValues(SetInfo set) {
        setId = set.setId;
        userId = set.userId;
        nativeLang = set.nativeLang;
        learningLang = set.learningLang;
        setSize = set.setSize;
        name = set.name;
        updatedTimeLong = set.updatedTimeLong;
    }
}
