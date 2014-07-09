
package com.openwords.util;
/*
 * @author: Guan
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.openwords.model.InitDatabase;
import com.openwords.model.UserPerformance;
import com.openwords.model.UserWords;
import com.openwords.util.preference.OpenwordsSharedPreferences;
import com.orm.SugarRecord;

public class WordSelectionAlg extends SugarRecord<UserPerformance> {
	private int user_id;
	//Higher the weight more likely the word be selected
	private final double weightOfWrong = 0.3;
	private final double weightOfskip = 0.3;
	private final double weightOfExposure = 0.3;
	private final double weightOfLastTime = 0.3;
	private final double weightOfLastPerformance = 0.3;
	List<UserPerformance> perform;
	List<UserWords> userWord;
	HashMap<Integer, Double> weightTable;
	
	public WordSelectionAlg(Context arg0) {
		
		// TODO Auto-generated constructor stub
		user_id = OpenwordsSharedPreferences.getUserInfo().getUserId();
	}
	
	
	private double calcWeight(UserPerformance perform) {
		double wrongRate = (perform.total_exposure - perform.total_correct) / (double)perform.total_exposure;
		double skipRate = (perform.total_skipped) / (double)perform.total_exposure;
		double lastTimeIndicator = 1; //null
		if(perform.last_performance==3) lastTimeIndicator = -1; //last time is correct
		else if(perform.last_performance==2) lastTimeIndicator = -0.5; //close
		else if(perform.last_performance==1) lastTimeIndicator = 2; //wrong
		
		long currentTime = System.currentTimeMillis() / 1000L;
		long timeGap = currentTime - perform.last_time;
		long dayGap = timeGap/3600/24;
		int timeFactor = (int) (Math.log(dayGap)/Math.log(2));
		double weight = wrongRate*10*weightOfWrong 
				+ skipRate*10*weightOfskip 
				+ lastTimeIndicator*5*weightOfLastPerformance
				+ timeFactor*weightOfLastTime;
		Log.d("wrong", Double.toString(wrongRate*10*weightOfWrong ));
		Log.d("skip", Double.toString(skipRate*10*weightOfskip ));
		Log.d("perfromance", Double.toString(lastTimeIndicator*5*weightOfLastPerformance ));
		Log.d("time", Double.toString( timeFactor*weightOfLastTime ));
		return weight;
	}
	
	public List<Integer> pickup(int size, Boolean hasAudio) {
		int languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
		List<Integer> result = new ArrayList<Integer>();
		
		if(hasAudio!=null && hasAudio.equals(true)) {
			userWord = UserWords.findFreshWithAudio();
			perform = UserPerformance.findByUserLanguageWithAudio(user_id, languageID);
		} else { //no requirement for audio
			userWord = UserWords.findFresh();
			perform = UserPerformance.findByUserLanguage(user_id, languageID);
		}
		
		Log.d("WordSelectAlg","UserWord size:"+userWord.size());
		if(userWord.size()>=size) {
			for(int i=0;i<size;i++) {
				int random = (int) (Math.random() * userWord.size());
				result.add(userWord.get(random).connectionId);
				UserWords.setFreshToStale(userWord.get(random).connectionId);
			}
			return result;
		} else {
			for(int i=0;i<userWord.size();i++) {
				result.add(userWord.get(i).connectionId);
				UserWords.setFreshToStale(userWord.get(i).connectionId);
				InitDatabase.updateBackUserWords(user_id, userWord.get(i).connectionId, false);
			}
		}
		size = size - result.size();
		
		
		if(perform.size()==0) {
			Log.e("WordSelectionAlg","No data in perform uid:"+user_id+" languageID:"+languageID);
			return result;
		}

		Log.e("findByUser return size",""+perform.size()+"--"+perform.get(0).connection_id);
		weightTable = new HashMap<Integer, Double>();
		for(UserPerformance item : perform) {
			double weight = calcWeight(item);
			Log.e("Weight Table",item.connection_id+" "+weight);
			weightTable.put(item.connection_id, weight);
		}
		double totalWeight = 0.0d;
		for (UserPerformance item : perform)
		{
		    totalWeight += weightTable.get(item.connection_id);
		}
		// Now choose a random item


		double lastConnID = -1;
		for(int i=0;i<size;i++) {
			int randomIndex = -1;
			double random = Math.random() * totalWeight;
			Log.e("Random val",random+"");
			Log.e("Total weight",totalWeight+"");
		    outerloop:
			for (int j = 0; j < perform.size(); j++) {
			    random -= weightTable.get(perform.get(j).connection_id);
			    if (random <= 0.0d) {
			        randomIndex = j;
			        if(perform.get(j).connection_id==lastConnID) {
			        	continue outerloop;
			        } else lastConnID = perform.get(j).connection_id;
			        Log.d("Selected connection_id",""+perform.get(j).connection_id);
			        break;
			    }
			}
			result.add(perform.get(randomIndex).connection_id);
		}
		return result;
	}
	
	
}

