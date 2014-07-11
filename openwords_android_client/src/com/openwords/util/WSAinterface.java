package com.openwords.util;

import java.util.List;

import com.openwords.util.preference.OpenwordsSharedPreferences;

public interface WSAinterface {
	int user_id = OpenwordsSharedPreferences.getUserInfo().getUserId();
	int languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
	public List<Integer> pickup(int size, Boolean hasAudio);
}
