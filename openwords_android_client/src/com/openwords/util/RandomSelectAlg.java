package com.openwords.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.openwords.model.InitDatabase;
import com.openwords.model.UserPerformance;
import com.openwords.model.UserWords;
import com.openwords.util.preference.OpenwordsSharedPreferences;

public class RandomSelectAlg implements WSAinterface {
	List<UserPerformance> perform;
	List<UserWords> userWord;

	@Override
	public List<Integer> pickup(int size, Boolean hasAudio) {
		
		List<Integer> result = new ArrayList<Integer>();
		
		if(hasAudio!=null && hasAudio.equals(true)) {
			userWord = UserWords.findFreshWithAudio();
			perform = UserPerformance.findByUserLanguageWithAudio(user_id, languageID);
		} else { //no requirement for audio
			userWord = UserWords.findFresh();
			perform = UserPerformance.findByUserLanguage(user_id, languageID);
		}
		
		Log.d("WordSelectAlg","UserWord size:"+userWord.size());
		Log.d("WordSelectAlg","Perform size:"+perform.size());
		if(userWord.size()>=size) {
			for(int i=0;i<size;i++) {
				int random = (int) (Math.random() * userWord.size());
				result.add(userWord.get(random).connectionId);
				UserWords.setFreshToStale(userWord.get(random).connectionId);
				InitDatabase.updateBackUserWords(user_id, userWord.get(i).connectionId, 0);
			}
			return result;
		} else {
			for(int i=0;i<userWord.size();i++) {
				result.add(userWord.get(i).connectionId);
				UserWords.setFreshToStale(userWord.get(i).connectionId);
				InitDatabase.updateBackUserWords(user_id, userWord.get(i).connectionId, 0);
			}
		}
		size = size - result.size();
		for(int i=0;i<size;i++) {
			int random = (int) (Math.random() * perform.size());
			result.add(perform.get(random).connection_id);
		}
		return result;
	}
	
	public String toString() {
		return "Random selected algorithm";
	}

}
