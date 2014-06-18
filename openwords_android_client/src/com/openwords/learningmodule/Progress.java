package com.openwords.learningmodule;

import com.openwords.model.LeafCard;

import java.util.List;

public class Progress {

    private List<LeafCard> cardsPool;
    private int currentCard;

    public Progress(List<LeafCard> cardsPool, int currentCard) {
        this.cardsPool = cardsPool;
        this.currentCard = currentCard;
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
