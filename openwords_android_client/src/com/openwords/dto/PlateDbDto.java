package com.openwords.dto;

public class PlateDbDto {
	private int connection_id;
	private String wordL2;
	private String wordL1;
	private String transcription;
	private int perf;
	
	public PlateDbDto()
	{
		this.connection_id = 0;
		this.wordL1 = "";
		this.wordL2 = "";
		this.transcription = "";
		this.perf = 0;
	}
	
	public PlateDbDto(int connection_id, String wordL1, String wordL2, String transcription, int perf)
	{
		this.connection_id = connection_id;
		this.wordL1 = wordL1;
		this.wordL2 = wordL2;
		this.transcription = transcription;
		this.perf = perf;
	}

}
