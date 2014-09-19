package com.openwords.model;

public class LeafCardHearing extends LeafCard {

    private Integer userChoice = 0;
    private String userInput;

    public LeafCardHearing() {
    }

    public LeafCardHearing(int connectionId, String wordLang2, int wordTwoId, String wordLang1, int wordOneId,
            String transcription) {
        super(connectionId, wordLang2, wordTwoId, wordLang1, wordTwoId, transcription);
    }

    public Integer getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(Integer userChoice) {
        this.userChoice = userChoice;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public String getUserInput() {
        return userInput;
    }
}
