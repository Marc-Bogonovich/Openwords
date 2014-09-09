package com.openwords.model;

public class LeafCardTypeEval extends LeafCard {

    private Integer userChoice = 0;
    private String userInput;

    public LeafCardTypeEval(int connectionId, String wordLang2, int wordTwoId, String wordLang1, int wordOneId,
            String transcription) {
        super(connectionId, wordLang2, wordTwoId, wordLang1, wordOneId, transcription);
    }

    public Integer getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(Integer userChoice) {
        this.userChoice = userChoice;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}
