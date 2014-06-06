package com.openwords.dto;

public class PlateDbDto {
	public int connection_id;
	public String wordL2;
	public String wordL1;
	public String transcription;
	public int perf;
	public int exposure;
	
	public PlateDbDto()
	{
		this.connection_id = 0;
		this.wordL1 = "";
		this.wordL2 = "";
		this.transcription = "";
		this.perf = 0;
		this.exposure=0;
	}
	
	public PlateDbDto(int connection_id, String wordL1, String wordL2, String transcription, int perf, int exposure)
	{
		this.connection_id = connection_id;
		this.wordL1 = wordL1;
		this.wordL2 = wordL2;
		this.transcription = transcription;
		this.perf = perf;
		this.exposure = exposure;
	}

}
