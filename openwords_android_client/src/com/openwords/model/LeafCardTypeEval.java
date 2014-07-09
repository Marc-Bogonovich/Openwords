package com.openwords.model;

public class LeafCardTypeEval extends LeafCard {
	private Integer userChoice = 0;
	private String userInput;
	public LeafCardTypeEval(int connectionId, String wordLang2, String wordLang1,
			String transcription) {
		super(connectionId, wordLang2, wordLang1, transcription);
		// TODO Auto-generated constructor stub
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
