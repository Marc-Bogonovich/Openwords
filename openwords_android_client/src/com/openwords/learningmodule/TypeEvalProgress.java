package com.openwords.learningmodule;

import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardTypeEval;

import java.util.List;

public class TypeEvalProgress {

    private List<LeafCardTypeEval> cardsPool;
    private int currentCard;

    public TypeEvalProgress(List<LeafCardTypeEval> cardsPool, int currentCard) {
        this.cardsPool = cardsPool;
        this.currentCard = currentCard;
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
