package com.openwords.model;

import java.sql.Blob;
import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class UserWords extends SugarRecord<UserWords> {

	int connectionId;
	int wordL2Id;
	String wordL2;
	int wordL1Id;
	String wordL1;
	int l2Id;
	String l2Name;
	Blob audiocall;	
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
	Blob audiocall)
	{
		super(ctx);
		this.connectionId=connectionId;
		this.wordL1Id=wordL2Id;
		this.wordL2=wordL2;
		this.wordL1Id=wordL1Id;
		this.wordL1=wordL1;
		this.l2Id=l2Id;
		this.l2Name=l2Name;
		this.audiocall=audiocall;
	}
	
	public static List<UserWords> findByConnection(int connection_id)
	{
		List<UserWords> words = UserWords.find(UserWords.class, "connection_id=", Integer.toString(connection_id));
		return words;
	}

}
