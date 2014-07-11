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
	public int fresh;
	public UserWords() {
		
		// TODO Auto-generated constructor stub
	}
	
	public UserWords(int connectionId,
	int wordL1Id,
	String wordL1,
	int wordL2Id,
	String wordL2,
	int l2Id,
	String l2Name,
	String audiocall)
	{
		
		this.connectionId=connectionId;
		this.wordLTwoId=wordL2Id;
		this.wordLTwo=wordL2;
		this.wordLOneId=wordL1Id;
		this.wordLOne=wordL1;
		this.lTwoId=l2Id;
		this.lTwoName=l2Name;
		this.audiocall=audiocall;
		this.fresh=1;
	}
	
	public UserWords(int connectionId,
			int wordL1Id,
			String wordL1,
			int wordL2Id,
			String wordL2,
			int l2Id,
			String l2Name,
			String audiocall,
			int fresh)
			{
				
				this.connectionId=connectionId;
				this.wordLTwoId=wordL2Id;
				this.wordLTwo=wordL2;
				this.wordLOneId=wordL1Id;
				this.wordLOne=wordL1;
				this.lTwoId=l2Id;
				this.lTwoName=l2Name;
				this.audiocall=audiocall;
				this.fresh=fresh;
			}
	
	public static List<UserWords> findByConnection(int connection_id)
	{
		List<UserWords> words = UserWords.find(UserWords.class, "connection_id="+connection_id);
		return words;
	}
	
	public static List<UserWords> findByLanguage(int language2id)
	{
		List<UserWords> words = UserWords.find(UserWords.class, "l_two_id="+language2id);
		return words;
	}
	
	public static List<UserWords> findByLanguageWithAudio(int language2id)
	{
		List<UserWords> words = UserWords.find(UserWords.class, "l_two_id="+language2id+" and audiocall<>'null'");
		return words;
	}
	
	public static List<UserWords> findFresh()
	{
		List<UserWords> uw = UserWords.find(UserWords.class, "fresh=1");
		return uw;
	}
	
	public static List<UserWords> findFreshWithAudio()
	{
		List<UserWords> uw = UserWords.find(UserWords.class, "fresh=1 and audiocall<>'null'");
		return uw;
	}
	
	public static void setStaleToFresh(int connection_id)
	{
		UserWords.executeQuery("UPDATE user_words SET fresh=1 where connection_id="+connection_id);
	}
	
	public static void setFreshToStale(int connection_id)
	{
		UserWords.executeQuery("UPDATE user_words SET fresh=0 where connection_id="+connection_id);
	}

}
