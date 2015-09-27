package com.openwords.model;

import com.openwords.services.implementations.ServiceGetSetItems;
import com.openwords.services.implementations.ServiceGetSetItems.Result;
import com.openwords.services.interfaces.HttpResultHandler;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class SetItem extends SugarRecord<SetItem> {

    public static void getItems(long setId, long userId, final ResultSetItems resultHanlder) {
        new ServiceGetSetItems().doRequest(setId, userId, new HttpResultHandler() {

            public void hasResult(Object resultObject) {
                Result r = (Result) resultObject;
                resultHanlder.result(r.itemsResult);
            }

            public void noResult(String errorMessage) {
                resultHanlder.result(null);
            }
        });
    }

    public long setId, wordId;
    public int itemOrder;
    public String wordOne, wordTwo;
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

    public SetItem(long setId, long wordId, int itemOrder) {
        this.setId = setId;
        this.wordId = wordId;
        this.itemOrder = itemOrder;
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

}
