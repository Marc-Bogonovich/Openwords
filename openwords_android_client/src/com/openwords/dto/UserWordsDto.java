package com.openwords.dto;

public class UserWordsDto {
	
	public int connection_id;
	public int wordl2_id;
	public String wordl2;
	public int wordl1_id;
	public String wordl1;
	public int l2_id;
	public String l2_name;
	
	public UserWordsDto()
	{
		this.connection_id = 0;
		this.wordl2_id = 0;
		this.wordl2 = "";
		this.wordl1_id = 0;
		this.wordl1 = "";
		this.l2_id = 0;
		this.l2_name = "";
	}
	
	public UserWordsDto(int connection_id,int wordl2_id,String wordl2,int wordl1_id,String wordl1,int l2_id,String l2_name)
	{
		this.connection_id = connection_id;
		this.wordl2_id = wordl2_id;
		this.wordl2 = wordl2;
		this.wordl1_id = wordl1_id;
		this.wordl1 = wordl1;
		this.l2_id = l2_id;
		this.l2_name = l2_name;
	}
}
