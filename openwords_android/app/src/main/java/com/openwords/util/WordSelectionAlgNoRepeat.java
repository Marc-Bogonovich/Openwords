
package com.openwords.util;
/*
 * @author: Guan
 */

public class WordSelectionAlgNoRepeat  {
	//Higher the weight more likely the word be selected
	private final double weightOfWrong = 0.3;
	private final double weightOfskip = 0.3;
	private final double weightOfExposure = 0.3;
	private final double weightOfLastTime = 0.3;
	private final double weightOfLastPerformance = 0.3;
//	List<UserPerformance> perform;
//	List<UserWords> userWord;
//	HashMap<Integer, Double> weightTable;
//	
//	//calculate the weight for each word
//	private double calcWeight(UserPerformance perform) {
//		double wrongRate = (perform.total_exposure - perform.total_correct) / (double)perform.total_exposure;
//		double skipRate = (perform.total_skipped) / (double)perform.total_exposure;
//		double lastTimeIndicator = 1; //null
//		if(perform.last_performance==3) lastTimeIndicator = 1; //last time is correct
//		else if(perform.last_performance==2) lastTimeIndicator = 2; //close
//		else if(perform.last_performance==1) lastTimeIndicator = 4; //wrong
//		
//		long currentTime = TimeConvertor.getUnixTime() / 1000L;
//		long timeGap = currentTime - perform.last_time;
//		long dayGap = Math.max(timeGap/3600/24, 1);
//		int timeFactor = (int) (Math.log(dayGap)/Math.log(2));
//		double weight = wrongRate*10*weightOfWrong 
//				+ skipRate*10*weightOfskip 
//				+ lastTimeIndicator*5*weightOfLastPerformance
//				+ timeFactor*weightOfLastTime;
////		Log.d("wrong", Double.toString(wrongRate*10*weightOfWrong ));
////		Log.d("skip", Double.toString(skipRate*10*weightOfskip ));
////		Log.d("perfromance", Double.toString(lastTimeIndicator*5*weightOfLastPerformance ));
////		Log.d("time", Double.toString( timeFactor*weightOfLastTime ));
//		return weight;
//	}
//	
//	public List<Integer> pickup(int size, Boolean hasAudio) {
//		int user_id = OpenwordsSharedPreferences.getUserInfo().getUserId();
//		int languageID = OpenwordsSharedPreferences.getUserInfo().getLang_id();
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
//		Log.d("WordSelectAlg","Fresh word size:"+userWord.size());
//		Log.d("WordSelectAlg","Perform size:"+perform.size());
//		if(userWord.size()>=size) { //the size of fresh word is larger than what is required
//			Collections.shuffle(userWord); //randomize userword
//			
//			for(int i=0;i<size;i++) {
//				result.add(userWord.get(i).connectionId);
//				UserWords.setFreshToStale(userWord.get(i).connectionId);
//			}
//			return result;
//		} else {
//			Collections.shuffle(userWord); //randomize userword
//			for(int i=0;i<userWord.size();i++) {
//				result.add(userWord.get(i).connectionId);
//				UserWords.setFreshToStale(userWord.get(i).connectionId);
//			}
//		}
//		//if the fresh word is not enough
//		size = size - result.size();
//		if(perform.size()==0) {
//			Log.e("WordSelectionAlg","No data in perform uid:"+user_id+" languageID:"+languageID);
//			return result;
//		} else if (perform.size() < size) { //even adding all records cannot fill slots
//			Collections.shuffle(perform); //randomize perform
//			for(int i=0;i<perform.size();i++) {
//				result.add(perform.get(i).connection_id);
//				Log.d("WSA_no repeat", ""+perform.get(i).connection_id);
//			}
//			OpenwordsSharedPreferences.setLeafCardSize(result.size()); //set leaf card size to be result size
//			return result;
//		}
//		//the size of perfom is larger than what is required
//		//select indicated size randomly from perform with no repeat
//		
//		weightTable = new HashMap<Integer, Double>();
//		//assemble the word-weight table
//		for(UserPerformance item : perform) {
//			double weight = calcWeight(item);
//			Log.e("Weight Table",item.connection_id+" "+weight);
//			weightTable.put(item.connection_id, weight);
//		}
//		double totalWeight = 0.0d;
//		//get the total weight over all words
//		for (UserPerformance item : perform)
//		{
//		    totalWeight += weightTable.get(item.connection_id);
//		}
//		// Now choose a random item
//
//		for(int i=0;i<size;i++) {
//			int randomIndex = -1;
//			double random = Math.random() * totalWeight;
//			Log.e("Random val",random+"");
//			Log.e("Total weight",totalWeight+"");
//		    outerloop:
//			for (int j = 0; j < perform.size(); j++) {
//			    random -= weightTable.get(perform.get(j).connection_id);
//			    if (random <= 0.0d) {
//			        randomIndex = j;
//			        if(result.contains(perform.get(j).connection_id)) {
//			        	continue outerloop; //re-run the inner loop to get another random index
//			        }
//			        break;
//			    }
//			}
//			result.add(perform.get(randomIndex).connection_id);
//		}
//		return result;
//	}
	
	//this method is used to create the algorithm drop list in settings page
	public String toString() {
		return "Word Selection Alg -- no repeat";
	}
	
}

