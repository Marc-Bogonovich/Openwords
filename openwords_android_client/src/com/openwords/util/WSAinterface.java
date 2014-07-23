package com.openwords.util;

import java.util.List;

import com.openwords.util.preference.OpenwordsSharedPreferences;

public interface WSAinterface {
	public List<Integer> pickup(int size, Boolean hasAudio);
	public String toString();
}
