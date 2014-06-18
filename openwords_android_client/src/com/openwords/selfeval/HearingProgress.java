package com.openwords.selfeval;

import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardHearing;

import java.util.List;

public class HearingProgress {

    private List<LeafCardHearing> cardsPool;
    private int currentCard;

    public HearingProgress(List<LeafCardHearing> cardsPool, int currentCard) {
        this.cardsPool = cardsPool;
        this.currentCard = currentCard;
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
