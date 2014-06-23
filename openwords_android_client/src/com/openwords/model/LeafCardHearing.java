package com.openwords.model;

public class LeafCardHearing extends LeafCard {
	private Integer userChoice = 0;
	private String userInput;

	public LeafCardHearing(String wordLang2, String wordLang1,
			String transcription) {
		super(wordLang2, wordLang1, transcription);
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
