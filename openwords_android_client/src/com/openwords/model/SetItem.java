package com.openwords.model;

import com.openwords.services.implementations.ServiceGetSetItems;
import com.openwords.services.interfaces.HttpResultHandler;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetItem extends SugarRecord<SetItem> {

    public static List<SetItem> loadAllItems(long setId) {
        return Select.from(SetItem.class)
                .where(Condition.prop("set_id").eq(setId))
                .list();
    }

    private static void refreshAll(long setId, List<SetItem> items) {
        SetItem.deleteAll(SetItem.class, "set_id = ?", String.valueOf(setId));
        SetItem.saveInTx(items);
    }

    public static void getItems(final long setId, long userId, final ResultSetItems resultHanlder) {
        new ServiceGetSetItems().doRequest(setId, userId, new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                ServiceGetSetItems.Result r = (ServiceGetSetItems.Result) resultObject;
                Word.saveOrUpdateAll(r.words);
                Map<Long, Word> commons = new HashMap<Long, Word>(r.words.size());
                for (Word w : r.words) {
                    commons.put(w.wordId, w);
                }
                for (SetItem item : r.itemsResult) {
                    item.wordOneCommon = commons.get(item.wordOneId).getMeta().commonTranslation;
                    item.wordTwoCommon = commons.get(item.wordTwoId).getMeta().commonTranslation;
                    item.twoTranscription = commons.get(item.wordTwoId).getMeta().nativeTranscription;
                }
                refreshAll(setId, r.itemsResult);
                resultHanlder.result(r.itemsResult);
            }

            public void noResult(String errorMessage) {
                resultHanlder.result(null);
            }
        });
    }

    public long setId, wordOneId, wordTwoId;
    public int itemOrder;
    public String wordOne, wordTwo, wordOneCommon, wordTwoCommon, twoTranscription;
    @Ignore
    public boolean isHead;
    @Ignore
    public boolean isModifying;
    @Ignore
    public boolean isRemoving;
    @Ignore
    public boolean isNew;

    public SetItem() {
    }

    public SetItem(int itemOrder, String wordOne, String wordTwo, boolean isHead) {
        this.itemOrder = itemOrder;
        this.wordOne = wordOne;
        this.wordTwo = wordTwo;
        this.isHead = isHead;
    }

    public SetItem(int itemOrder, String wordOne, String wordTwo, boolean isHead, boolean isModifying) {
        this.itemOrder = itemOrder;
        this.wordOne = wordOne;
        this.wordTwo = wordTwo;
        this.isHead = isHead;
        this.isModifying = isModifying;
    }

    public SetItem(int itemOrder, String wordOne, String wordTwo, boolean isHead, boolean isModifying, boolean isNew) {
        this.itemOrder = itemOrder;
        this.wordOne = wordOne;
        this.wordTwo = wordTwo;
        this.isHead = isHead;
        this.isModifying = isModifying;
        this.isNew = isNew;
    }

    public SetItem(long wordOneId, long wordTwoId, int itemOrder, String wordOne, String wordTwo, String wordOneCommon, String wordTwoCommon, boolean isHead, boolean isModifying, boolean isNew) {
        this.wordOneId = wordOneId;
        this.wordTwoId = wordTwoId;
        this.itemOrder = itemOrder;
        this.wordOne = wordOne;
        this.wordTwo = wordTwo;
        this.wordOneCommon = wordOneCommon;
        this.wordTwoCommon = wordTwoCommon;
        this.isHead = isHead;
        this.isModifying = isModifying;
        this.isNew = isNew;
    }

}
