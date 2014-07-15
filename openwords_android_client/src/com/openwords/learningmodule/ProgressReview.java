package com.openwords.learningmodule;

import com.openwords.model.LeafCard;

import java.util.List;

public class ProgressReview {

    private List<LeafCard> cardsPool;
    private int currentCard;
    private int langageID;
    
	public int getLangageID() {
		return langageID;
	}

	public void setLangageID(int langageID) {
		this.langageID = langageID;
	}

	public ProgressReview(List<LeafCard> cardsPool, int currentCard, int languageID) {
        this.cardsPool = cardsPool;
        this.currentCard = currentCard;
        this.langageID = languageID;
    }

    public List<LeafCard> getCardsPool() {
        return cardsPool;
    }

    public void setCardsPool(List<LeafCard> cardsPool) {
        this.cardsPool = cardsPool;
    }

    public int getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(int currentCard) {
        this.currentCard = currentCard;
    }
}
