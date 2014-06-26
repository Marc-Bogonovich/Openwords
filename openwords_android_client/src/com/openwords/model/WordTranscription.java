package com.openwords.model;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class WordTranscription extends SugarRecord<WordTranscription>{

	int wordLTwoId;
	String transcription;
	public WordTranscription(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public WordTranscription(Context ctx, int wordl2Id, String transcription)
	{
		super(ctx);
		this.wordLTwoId=wordl2Id;
		this.transcription=transcription;
	}
	
	public List<WordTranscription> findByWord(int wordL2Id)
	{
		List<WordTranscription> trans = WordTranscription.find(WordTranscription.class, "word_l_two_id="+wordL2Id);
		return trans;
	}

}
