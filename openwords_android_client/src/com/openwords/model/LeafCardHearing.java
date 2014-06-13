package com.openwords.model;

public class LeafCardHearing {
	private String wordLang2, wordLang1, transcription;
	private Integer userChoice = 0;
	private String userInput;
	private String audioURL;
	private String connectionID; //for future use when connecting with db
	public LeafCardHearing(String wordLang2, String wordLang1, String transcription) {
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
	public String getAudioURL() {
		return audioURL;
	}
	public void setAudioURL(String audioURL) {
		this.audioURL = audioURL;
	}
}
