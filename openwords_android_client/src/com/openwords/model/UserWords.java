package com.openwords.model;

//import java.sql.Blob;
import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class UserWords extends SugarRecord<UserWords> {

	public int connectionId;
	public int wordLTwoId;
	public String wordLTwo;
	public int wordLOneId;
	public String wordLOne;
	public int lTwoId;
	public String lTwoName;
	public String audiocall;	
	public boolean fresh;
	public UserWords(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public UserWords(Context ctx, int connectionId,
	int wordL2Id,
	String wordL2,
	int wordL1Id,
	String wordL1,
	int l2Id,
	String l2Name,
	String audiocall)
	{
		super(ctx);
		this.connectionId=connectionId;
		this.wordLTwoId=wordL2Id;
		this.wordLTwo=wordL2;
		this.wordLOneId=wordL1Id;
		this.wordLOne=wordL1;
		this.lTwoId=l2Id;
		this.lTwoName=l2Name;
		this.audiocall=audiocall;
		this.fresh=true;
	}
	
	public static List<UserWords> findByConnection(int connection_id)
	{
		List<UserWords> words = UserWords.find(UserWords.class, "connection_id="+connection_id);
		return words;
	}
	
	public static void setFreshToStale(int connection_id)
	{
		UserWords.executeQuery("UPDATE user_words SET fresh=false where connection_id="+connection_id);
	}

}
