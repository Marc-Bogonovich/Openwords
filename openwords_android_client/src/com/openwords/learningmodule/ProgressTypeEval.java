package com.openwords.learningmodule;

import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardTypeEval;

import java.util.List;

/**
 * @author jiguan
 *
 */
public class ProgressTypeEval {

    private List<LeafCardTypeEval> cardsPool;
    private int currentCard;
    private int languageID;

    public int getLanguageID() {
		return languageID;
	}

	public void setLanguageID(int languageID) {
		this.languageID = languageID;
	}

	public ProgressTypeEval(List<LeafCardTypeEval> cardsPool, int currentCard, int languageID) {
        this.cardsPool = cardsPool;
        this.currentCard = currentCard;
        this.languageID = languageID;
    }

    public List<LeafCardTypeEval> getCardsPool() {
        return cardsPool;
    }

    public void setCardsPool(List<LeafCardTypeEval> cardsPool) {
        this.cardsPool = cardsPool;
    }

    public int getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(int currentCard) {
        this.currentCard = currentCard;
    }
}
