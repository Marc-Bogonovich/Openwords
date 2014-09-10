package com.openwords.model;

public class LeafCard {

    private String wordLang2, wordLang1, transcription;
    private String audioURL;
    private int connectionId, wordOneId, wordTwoId;
    private long lastTime;

    public LeafCard(int connectionId, String wordLang2, int wordTwoId, String wordLang1, int wordOneId, String transcription) {
        this.connectionId = connectionId;
        this.wordLang2 = wordLang2;
        this.wordLang1 = wordLang1;
        this.transcription = transcription;
        this.wordOneId = wordOneId;
        this.wordTwoId = wordTwoId;
    }

    public String getWordLang2() {
        return wordLang2;
    }

    public void setWordLang2(String wordLang2) {
        this.wordLang2 = wordLang2;
    }

    public String getWordLang1() {
        return wordLang1;
    }

    public void setWordLang1(String wordLang1) {
        this.wordLang1 = wordLang1;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getWordOneId() {
        return wordOneId;
    }

    public void setWordOneId(int wordOneId) {
        this.wordOneId = wordOneId;
    }

    public int getWordTwoId() {
        return wordTwoId;
    }

    public void setWordTwoId(int wordTwoId) {
        this.wordTwoId = wordTwoId;
    }

}
