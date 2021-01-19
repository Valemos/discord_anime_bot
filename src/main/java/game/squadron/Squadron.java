package game.squadron;

import game.cards.CardPersonal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Integer.min;


public class Squadron {
    String playerId;
    private final int sizeMax;
    HashMap<CardPersonal, HealthState> cardsStateMap;


    public Squadron(int sizeMax) {
        this(null, sizeMax);
    }

    public Squadron(String playerId, int sizeMax) {
        this.playerId = playerId;
        this.sizeMax = sizeMax;
        setCards(new ArrayList<>(sizeMax));
    }

    public Squadron(String playerId, List<CardPersonal> cards) {
        this.playerId = playerId;
        sizeMax = cards.size();
        setCards(cards);
    }

    public void setCards(List<CardPersonal> cards) {
        cardsStateMap = new HashMap<>();
        for (int i = 0; i < min(cards.size(), sizeMax); i++) {
            cardsStateMap.put(cards.get(i), HealthState.HEALTHY);
        }
    }

    public Set<CardPersonal> getCards() {
        return cardsStateMap.keySet();
    }

    public List<CardPersonal> getSortedCards() {
        return cardsStateMap.keySet().stream()
                .sorted(CardPersonal::comparatorPowerLevel)
                .collect(Collectors.toList());
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public boolean isEmpty() {
        return cardsStateMap.isEmpty();
    }

    public void addCard(CardPersonal card) {
        cardsStateMap.put(card, HealthState.HEALTHY);
    }

    public boolean isFull() {
        return cardsStateMap.size() == sizeMax;
    }

    public float getPowerLevel() {
        float totalPowerLevel = 0;
        for (CardPersonal card : getCards()){
            float cardPowerLevel = card.getPowerLevel();
            HealthState cardHealth = cardsStateMap.get(card);
            totalPowerLevel += cardHealth == HealthState.INJURED ? cardPowerLevel * 0.2 : cardPowerLevel;
        }
        return totalPowerLevel;
    }
}
