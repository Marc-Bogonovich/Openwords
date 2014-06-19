package com.openwords.util;

public class WordsPageTool {

	private int id;
	private String word1;
	private String word2;
	private boolean checked_word;
	
	public WordsPageTool(int id, String word1, String word2, boolean checked_word)
	{
		super();
		this.setId(id);
		this.setWord1(word1);
		this.setWord2(word2);
		this.setChecked_word(checked_word);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord1() {
		return word1;
	}

	public void setWord1(String word1) {
		this.word1 = word1;
	}

	public String getWord2() {
		return word2;
	}

	public void setWord2(String word2) {
		this.word2 = word2;
	}

	public boolean isChecked_word() {
		return checked_word;
	}

	public void setChecked_word(boolean checked_word) {
		this.checked_word = checked_word;
	}
}
