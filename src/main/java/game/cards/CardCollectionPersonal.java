package game.cards;

import java.util.ArrayList;
import java.util.List;

public class CardCollectionPersonal {
    List<CardPersonal> cards;

    public CardCollectionPersonal() {
        this(new ArrayList<>());
    }

    public CardCollectionPersonal(List<CardPersonal> cards) {
        this.cards = cards;
    }

    public List<CardPersonal> getCards() {
        return cards;
    }

    public CardPersonal getCardById(int cardId) {
        return cards.stream()
                .filter((card) -> card.getCardId() == cardId)
                .findFirst().orElse(null);
    }

    public void addCard(CardPersonal card) {
        cards.add(card);
    }
}
