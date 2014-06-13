package com.openwords.view.learningModule;

import com.openwords.dto.UserPerfDto;

public class WordSelection {
	private double weight_correctness = 0.35d;
	private double weight_lastTime = 0.35d;
	private double weight_lastPerformance = 0.3d;
	private double screwUpSize = 10;
	private double reviewSize = 5;
	private UserPerfDto userPerformance;
	
	public void getDate() {
		 double totoalWeight = 0.0d;
		 //sample algorithm http://stackoverflow.com/questions/6737283/weighted-randomness-in-java
	}
	
	private double getCorrectness() {
		return userPerformance.total_correct/(double)userPerformance.total_exposure;
	}
	
	private class UserWordDB {
		int connectionID;
		String wordL2;
		String wordL1;
		String transcriptionL2;
		String audioURL;
	}
}
