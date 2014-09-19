package com.openwords.model;

import java.util.ArrayList;
import java.util.List;

public class LeafCardSelfEvalAdapter extends LeafCardAdapter {

    public List<LeafCard> getList(int size) {
        List<Integer> connectIDs = wordSelectionAlg.get(algIndex).pickup(size, null);
        List<LeafCard> result = new ArrayList<LeafCard>(size);
        for (Integer id : connectIDs) {
            // since connectionID is the primary key of UserWord table, only one record is returned
            UserWords raw_card = UserWords.findByConnection(id).get(0);
            String trans = WordTranscription.findByWord(raw_card.wordLTwoId).get(0).transcription;
            LeafCardSelfEval card = new LeafCardSelfEval(raw_card.connectionId, raw_card.wordLTwo, raw_card.wordLTwoId, raw_card.wordLOne, raw_card.wordLOneId, trans);
            result.add(card);
        }
        return result;
    }
}
