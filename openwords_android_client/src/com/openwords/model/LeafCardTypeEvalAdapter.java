package com.openwords.model;

import java.util.ArrayList;
import java.util.List;

public class LeafCardTypeEvalAdapter extends LeafCardAdapter {

    public List<LeafCardTypeEval> getList(int size) {
        List<Integer> connectIDs = wordSelectionAlg.get(algIndex).pickup(size, null);
        List<LeafCardTypeEval> result = new ArrayList<LeafCardTypeEval>(size);
        for (Integer id : connectIDs) {
            // since connectionID is the primary key of UserWord table, only one record is returned
            UserWords raw_card = UserWords.findByConnection(id).get(0);
            String trans = WordTranscription.findByWord(raw_card.wordLTwoId).get(0).transcription;
            LeafCardTypeEval card = new LeafCardTypeEval(raw_card.connectionId, raw_card.wordLTwo, raw_card.wordLTwoId, raw_card.wordLOne, raw_card.wordLOneId, trans);
            result.add(card);
        }
        return result;
    }
}
