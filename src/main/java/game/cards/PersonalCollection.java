package game.cards;

import java.util.List;

public class PersonalCollection {
    List<CharacterCardPersonal> cards;

    public PersonalCollection(List<CharacterCardPersonal> cards) {
        this.cards = cards;
    }

    public List<CharacterCardPersonal> getCards() {
        return cards;
    }

    public CharacterCardPersonal getCardById(int cardId) {
        return cards.stream()
                .filter((card) -> card.getCardId() == cardId)
                .findFirst().orElse(null);
    }

    public void addCard(CharacterCardPersonal card) {
        cards.add(card);
    }
}
