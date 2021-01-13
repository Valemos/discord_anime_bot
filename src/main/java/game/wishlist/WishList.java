package game.wishlist;

import game.cards.CardGlobal;

import java.util.ArrayList;
import java.util.List;

public class WishList {
    List<CardGlobal> cards = new ArrayList<>();

    public List<CardGlobal> getCards() {
        return cards;
    }

    public void add(CardGlobal card) {
        cards.add(card);
    }

    public boolean removeById(String cardId) {
        return cards.removeIf(c -> c.getId().equals(cardId));
    }
}
