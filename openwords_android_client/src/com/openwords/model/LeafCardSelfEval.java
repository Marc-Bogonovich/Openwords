package com.openwords.model;

public class LeafCardSelfEval extends LeafCard {

    private Boolean userChoice;

    public LeafCardSelfEval(int connectionId, String wordLang2, int wordTwoId, String wordLang1, int wordOneId, String transcription) {
        super(connectionId, wordLang2, wordTwoId, wordLang1, wordOneId, transcription);
    }

    public Boolean getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(Boolean userChoice) {
        this.userChoice = userChoice;
    }
}
