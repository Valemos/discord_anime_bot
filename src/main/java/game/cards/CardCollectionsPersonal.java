package game.cards;

import java.util.ArrayList;
import java.util.List;

public class CardCollectionsPersonal {
    List<CardPersonal> cards;

    public CardCollectionsPersonal() {
        this(new ArrayList<>());
    }

    public CardCollectionsPersonal(List<CardPersonal> cards) {
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
}
