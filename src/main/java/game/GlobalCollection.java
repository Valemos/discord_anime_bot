package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlobalCollection {
    List<CharacterCard> cards = new ArrayList<>();
    private int currentCardId = 0;

    public GlobalCollection() {
    }

    public void addCard(CharacterCard card) {
        if (isCardExists(card)) {
            removeCardById(card.getId());
        }

        card.setId(generateNextCardId());
        cards.add(card);
    }

    private boolean isCardExists(CharacterCard card) {
        return cards.contains(card);
    }

    private String generateNextCardId() {
        // will be replaced with database query in future
        currentCardId++;
        return String.valueOf(currentCardId);
    }

    public CharacterCard getCardById(String id) {
        return cards.stream()
                .filter((card)-> card.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void removeCardById(String id) {
        cards.removeIf((card) -> card.getId().equals(id));
    }

    public List<CharacterCard> getRandomCards(int amount) {
        List<CharacterCard> resultCards = new ArrayList<>(amount);

        Random random = new Random();
        for(int i = 0; i < amount; i++){
            int randomIndex = random.nextInt(cards.size());
            resultCards.add(cards.get(randomIndex));
        }

        return resultCards;
    }

    public int size() {
        return cards.size();
    }
}
