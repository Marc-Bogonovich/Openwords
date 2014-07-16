package com.openwords.learningmodule;

import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardHearing;

import java.util.List;

public class ProgressHearing {

    private List<LeafCardHearing> cardsPool;
    private int currentCard;
    private int languageID;

	public int getLanguageID() {
		return languageID;
	}

	public void setLanguageID(int languageID) {
		this.languageID = languageID;
	}

	public ProgressHearing(List<LeafCardHearing> cardsPool, int currentCard, int languageID) {
        this.cardsPool = cardsPool;
        this.currentCard = currentCard;
        this.languageID = languageID;
    }

    public List<LeafCardHearing> getCardsPool() {
        return cardsPool;
    }

    public void setCardsPool(List<LeafCardHearing> cardsPool) {
        this.cardsPool = cardsPool;
    }

    public int getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(int currentCard) {
        this.currentCard = currentCard;
    }
}
