package game;

import java.util.ArrayList;
import java.util.List;

public class GlobalCollection {
    List<CharacterCard> cards = new ArrayList<>();

    public GlobalCollection() {
    }

    public void addCard(CharacterCard card) {
        if (!cards.contains(card)) {
            card.setId(generateNextCardId());
            cards.add(card);
        }
    }

    private String generateNextCardId() {
        return "1";
    }

    public CharacterCard getCardById(String id) {
        return cards.stream()
                .filter((card)-> card.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void removeCardById(String id) {
        cards.removeIf((card) -> card.getId().equals(id));
    }
}
