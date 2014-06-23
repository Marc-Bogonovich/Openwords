package com.openwords.model;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.openwords.DAO.UserWordsDbHelper;
import com.openwords.dto.UserWordsDto;
import com.openwords.util.WordSelectionAlg;

public class LeafCardAdapter {
	private Context context;
	
	public LeafCardAdapter(Context context) {
		this.context = context;
	}

	public List<LeafCard> getList(int size) {
		List<Integer> connectIDs = new WordSelectionAlg(context).pickup(size);
		List<LeafCard> result = new ArrayList<LeafCard>();
		for(Integer id : connectIDs) {
			// since connectionID is the primary key of UserWord table, only one record is returned
			UserWordsDto raw_card = new UserWordsDbHelper(context).getByConnectionId(id).get(0);
			String trans = new WordTranscription(context).findByWord(raw_card.wordl2_id).get(0).transcription;
			LeafCard card = new LeafCard(raw_card.wordl2, raw_card.wordl1, trans);
			result.add(card);
		}
		return result;
	}
}
