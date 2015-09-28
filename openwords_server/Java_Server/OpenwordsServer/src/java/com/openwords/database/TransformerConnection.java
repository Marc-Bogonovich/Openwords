package com.openwords.database;

public class TransformerConnection {

    private long uniWord, targetWord, searchWord;

    public TransformerConnection() {
    }

    public TransformerConnection(long uniWord, long targetWord, long searchWord) {
        this.uniWord = uniWord;
        this.targetWord = targetWord;
        this.searchWord = searchWord;
    }

    public long getUniWord() {
        return uniWord;
    }

    public void setUniWord(long uniWord) {
        this.uniWord = uniWord;
    }

    public long getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(long targetWord) {
        this.targetWord = targetWord;
    }

    public long getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(long searchWord) {
        this.searchWord = searchWord;
    }

}
