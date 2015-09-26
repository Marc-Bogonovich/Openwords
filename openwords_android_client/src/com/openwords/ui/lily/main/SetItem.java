package com.openwords.ui.lily.main;

import com.orm.dsl.Ignore;

public class SetItem {

    public long setId, wordId;
    public int itemOrder;
    public String wordOne, wordTwo;
    @Ignore
    public boolean isHead;
    @Ignore
    public boolean isModifying;
    @Ignore
    public boolean isRemoving;

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

}
