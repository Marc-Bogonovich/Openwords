package com.openwords.dto;

public class WordTranscriptionDto {
	
	public int wordl2_id;
	public String transcription;
	
	public WordTranscriptionDto()
	{
		this.wordl2_id = 0;
		this.transcription = "";
	}
	
	public WordTranscriptionDto(int wordl2_id, String transcription)
	{
		this.wordl2_id=wordl2_id;
		this.transcription=transcription;
	}

}
