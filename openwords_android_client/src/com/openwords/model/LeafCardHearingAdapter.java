package com.openwords.model;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.openwords.util.WordSelectionAlg;

public class LeafCardHearingAdapter {
	private Context context;
	
	public LeafCardHearingAdapter(Context context) {
		this.context = context;
	}

	public List<LeafCardHearing> getList(int size) {
		List<Integer> connectIDs = new WordSelectionAlg(context).pickup(size);
		List<LeafCardHearing> result = new ArrayList<LeafCardHearing>();
		for(Integer id : connectIDs) {
			// since connectionID is the primary key of UserWord table, only one record is returned
			UserWords raw_card = UserWords.findByConnection(id).get(0);
			String trans = new WordTranscription(context).findByWord(raw_card.wordLTwoId).get(0).transcription;
			LeafCardHearing card = new LeafCardHearing(raw_card.wordLTwo, raw_card.wordLOne, trans);
			result.add(card);
		}
		return result;
	}
}