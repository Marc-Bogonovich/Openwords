package com.openwords.model;

import com.orm.SugarRecord;

public class WordMeaning extends SugarRecord<WordMeaning> {

    private int wordId;
    private String meaning;

    public WordMeaning() {
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

}
