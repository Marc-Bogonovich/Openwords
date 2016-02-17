package com.openwords.util;

import java.util.List;

public class RandomSelectAlg implements WSAinterface {
	
	@Override
	public List<Integer> pickup(int size, Boolean hasAudio) {
//		int user_id = OpenwordsSharedPreferences.getUserInfo().getUserId();
//		int languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
//		Log.e("RandomAlg","LangID: "+languageID);
//		List<Integer> result = new ArrayList<Integer>();
//		
//		if(hasAudio!=null && hasAudio.equals(true)) {
//			userWord = UserWords.findFreshWithAudio(languageID);
//			perform = UserPerformance.findByUserLanguageWithAudio(user_id, languageID);
//		} else { //no requirement for audio
//			userWord = UserWords.findFresh(languageID);
//			perform = UserPerformance.findByUserLanguage(user_id, languageID);
//		}
//		
//		Log.d("WordSelectAlg","UserWord size:"+userWord.size());
//		Log.d("WordSelectAlg","Perform size:"+perform.size());
//		if(userWord.size()>=size) {
//			for(int i=0;i<size;i++) {
//				int random = (int) (Math.random() * userWord.size());
//				result.add(userWord.get(random).connectionId);
//				UserWords.setFreshToStale(userWord.get(random).connectionId);
//			}
//			return result;
//		} else {
//			for(int i=0;i<userWord.size();i++) {
//				result.add(userWord.get(i).connectionId);
//				UserWords.setFreshToStale(userWord.get(i).connectionId);
//			}
//		}
//		size = size - result.size();
//		for(int i=0;i<size;i++) {
//			int random = (int) (Math.random() * perform.size());
//			result.add(perform.get(random).connection_id);
//		}
		return null;
	}
	
	public String toString() {
		return "Random selection algorithm";
	}

}
