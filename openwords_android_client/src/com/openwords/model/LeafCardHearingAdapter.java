package com.openwords.model;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.openwords.util.WordSelectionAlg;

public class LeafCardHearingAdapter extends LeafCardAdapter {

	public List<LeafCardHearing> getList(int size) {
		List<Integer> connectIDs = wordSelectionAlg.get(algIndex).pickup(size, true);
		List<LeafCardHearing> result = new ArrayList<LeafCardHearing>();
		for(Integer id : connectIDs) {
			// since connectionID is the primary key of UserWord table, only one record is returned
			UserWords raw_card = UserWords.findByConnection(id).get(0);
			String trans = new WordTranscription().findByWord(raw_card.wordLTwoId).get(0).transcription;
			LeafCardHearing card = new LeafCardHearing(raw_card.connectionId, raw_card.wordLTwo, raw_card.wordLOne, trans);
			result.add(card);
		}
		return result;
	}
}
