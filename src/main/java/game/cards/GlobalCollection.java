package game.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlobalCollection {
    List<CharacterCardGlobal> cards = new ArrayList<>();
    private int currentCardId = 0;

    public GlobalCollection() {
    }

    public void addCard(CharacterCardGlobal card) {
        if (isCardExists(card)) {
            removeCardById(card.getGlobalCardId());
        }

        card.setGlobalCardId(generateNextCardId());
        cards.add(card);
    }

    private boolean isCardExists(CharacterCardGlobal card) {
        return cards.contains(card);
    }

    private int generateNextCardId() {
        // will be replaced with database query in future
        currentCardId++;
        return currentCardId;
    }

    public CharacterCardGlobal getCardById(int id) {
        return cards.stream()
                .filter((card)-> card.getGlobalCardId() == id)
                .findFirst().orElse(null);
    }

    public void removeCardById(int id) {
        cards.removeIf((card) -> card.getGlobalCardId() == id);
    }

    public List<CharacterCardGlobal> getRandomCards(int amount) {
        List<CharacterCardGlobal> resultCards = new ArrayList<>(amount);

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
