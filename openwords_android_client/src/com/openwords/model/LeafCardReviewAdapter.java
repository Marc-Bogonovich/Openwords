package com.openwords.model;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class LeafCardReviewAdapter extends LeafCardAdapter {

    public List<LeafCard> getList(int size) {
        List<Integer> connectIDs = wordSelectionAlg.get(algIndex).pickup(size, null);
        Log.d("Selected list", connectIDs.toString());
        List<LeafCard> result = new ArrayList<LeafCard>(size);
        for (Integer id : connectIDs) {
            // since connectionID is the primary key of UserWord table, only one record is returned
            UserWords raw_card = UserWords.findByConnection(id).get(0);
            String trans = WordTranscription.findByWord(raw_card.wordLTwoId).get(0).transcription;
            Log.d("Trans", raw_card.wordLOne + "");
            LeafCard card = new LeafCard(raw_card.connectionId, raw_card.wordLTwo, raw_card.wordLTwoId, raw_card.wordLOne, raw_card.wordLOneId, trans);
            card.setAudioURL(raw_card.audiocall);
            result.add(card);
        }
        return result;
    }
}
