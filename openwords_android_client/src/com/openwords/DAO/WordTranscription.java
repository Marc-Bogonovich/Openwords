package com.openwords.DAO;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class WordTranscription extends SugarRecord<WordTranscription>{

	int wordL2Id;
	String transcription;
	public WordTranscription(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public WordTranscription(Context ctx, int wordl2Id, String transcription)
	{
		super(ctx);
		this.wordL2Id=wordl2Id;
		this.transcription=transcription;
	}
	
	public List<WordTranscription> findByWord(int wordL2Id)
	{
		List<WordTranscription> trans = WordTranscription.find(WordTranscription.class, "word_l2_id=?", Integer.toString(wordL2Id));
		return trans;
	}

}
