package com.openwords.learningmodule;

import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardSelfEval;

import java.util.List;

public class ProgressSelfEval {

    private List<LeafCardSelfEval> cardsPool;
    private int currentCard;
    private int languageID;

    public int getLanguageID() {
		return languageID;
	}

	public void setLanguageID(int languageID) {
		this.languageID = languageID;
	}

	public ProgressSelfEval(List<LeafCardSelfEval> cardsPool, int currentCard, int languageID ) {
        this.cardsPool = cardsPool;
        this.currentCard = currentCard;
        this.languageID = languageID;
    }

    public List<LeafCardSelfEval> getCardsPool() {
        return cardsPool;
    }

    public void setCardsPool(List<LeafCardSelfEval> cardsPool) {
        this.cardsPool = cardsPool;
    }

    public int getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(int currentCard) {
        this.currentCard = currentCard;
    }
}
