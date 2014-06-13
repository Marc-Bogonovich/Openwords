package com.openwords.test;

import com.openwords.model.LeafCardSelfEval;
import java.util.List;

public class Progress {

    private List<LeafCardSelfEval> cardsPool;
    private int currentCard;

    public Progress(List<LeafCardSelfEval> cardsPool, int currentCard) {
        this.cardsPool = cardsPool;
        this.currentCard = currentCard;
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
