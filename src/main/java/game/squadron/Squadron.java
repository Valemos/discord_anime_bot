package game.squadron;

import game.HealthState;
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
                .sorted((c1, c2) ->
                        Float.compare(c1.getPowerLevel(), c2.getPowerLevel())
                ).collect(Collectors.toList());
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

    public boolean addCard(CardPersonal card) {
        if (cardsStateMap.size() < sizeMax){
            cardsStateMap.put(card, HealthState.HEALTHY);
            return true;
        }
        return false;
    }
}
