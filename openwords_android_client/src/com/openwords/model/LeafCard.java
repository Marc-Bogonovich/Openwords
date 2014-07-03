package com.openwords.model;

public class LeafCard {
	private String wordLang2, wordLang1, transcription;
	private String audioURL;
	private int connectionId; 
	private long lastTime;
	
	public LeafCard(String wordLang2, String wordLang1, String transcription) {
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
	
	
}
