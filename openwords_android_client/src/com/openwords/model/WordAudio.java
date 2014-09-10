package com.openwords.model;

import com.orm.SugarRecord;

public class WordAudio extends SugarRecord<WordAudio> {

    private int wordId;
    private String fileName;

    public WordAudio() {
    }

    public WordAudio(int wordId, String fileName) {
        this.wordId = wordId;
        this.fileName = fileName;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
