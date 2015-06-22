package com.openwords.ui.lily.decks;

import java.util.LinkedList;
import java.util.List;

public class DeckInfo {

    public static List<DeckInfo> getNewTestingDecks() {
        List<DeckInfo> testingDecks = new LinkedList<DeckInfo>();
        testingDecks.add(new DeckInfo("Animal"));
        testingDecks.add(new DeckInfo("Math"));
        testingDecks.add(new DeckInfo("Sport"));
        testingDecks.add(new DeckInfo("Game"));
        testingDecks.add(new DeckInfo("Soccer"));
        testingDecks.add(new DeckInfo("Flower"));
        testingDecks.add(new DeckInfo("Weather"));
        testingDecks.add(new DeckInfo("Greeting"));
        testingDecks.add(new DeckInfo("Color"));
        testingDecks.add(new DeckInfo("Family"));
        testingDecks.add(new DeckInfo("Computer"));
        testingDecks.add(new DeckInfo("Travel"));
        testingDecks.add(new DeckInfo("Cities"));
        return testingDecks;
    }

    public static List<DeckInfo> searchDecks(String pattern) {
        String term = pattern.toLowerCase();
        List<DeckInfo> r = new LinkedList<DeckInfo>();
        for (DeckInfo deck : getNewTestingDecks()) {
            if (deck.name.toLowerCase().contains(term)) {
                r.add(deck);
            }
        }
        return r;
    }

    public String name;
    public boolean isPlusButton;

    public DeckInfo(String name) {
        this.name = name;
    }

    public DeckInfo(String name, boolean isPlusButton) {
        this.name = name;
        this.isPlusButton = isPlusButton;
    }

}
