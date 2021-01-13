package game.cards;

import java.util.ArrayList;
import java.util.List;

public class CardsPersonalManager {
    List<CardPersonal> cards;

    public CardsPersonalManager() {
        this(new ArrayList<>());
    }

    public CardsPersonalManager(List<CardPersonal> cards) {
        this.cards = cards;
    }

    public List<CardPersonal> getCards() {
        return cards;
    }

    public CardPersonal getCardById(String cardId) {
        return cards.stream()
                .filter((card) -> card.getCardId().equals(cardId))
                .findFirst().orElse(null);
    }

    public void addCard(String playerId, CardPersonal card) {
        // TODO add database query to get card id
        card.setPlayerId(playerId);
        cards.add(card);
    }

    public boolean removeCard(String playerId, CardPersonal card) {
        return cards.removeIf(c ->
                c.getCardId().equals(card.getCardId()) &&
                c.getPlayerId().equals(playerId)
        );
    }
}
