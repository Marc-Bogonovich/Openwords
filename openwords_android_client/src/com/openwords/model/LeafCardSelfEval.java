package com.openwords.model;

public class LeafCardSelfEval extends LeafCard {

    private Boolean userChoice;
    
    public LeafCardSelfEval(int connectionId, String wordLang2, String wordLang1, String transcription) {
    	super(connectionId, wordLang2, wordLang1, transcription);
    }

    public Boolean getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(Boolean userChoice) {
        this.userChoice = userChoice;
    }
}
