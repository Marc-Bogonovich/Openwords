package com.openwords.model;

public class LeafCardSelfEval {

    private String wordLang2, wordLang1, transcription;
    private Boolean userChoice;
    private int connectionId; //for future use when connecting with db

    public LeafCardSelfEval(String wordLang2, String wordLang1, String transcription) {
        this.wordLang2 = wordLang2;
        this.wordLang1 = wordLang1;
        this.transcription = transcription;
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

    public Boolean getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(Boolean userChoice) {
        this.userChoice = userChoice;
    }
}
