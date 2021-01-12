package game.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollectionGlobal {
    List<CardGlobal> cards = new ArrayList<>();
    private int currentCardId = 0;

    public CollectionGlobal() {
    }

    public void addCard(CardGlobal card) {
        if (isCardExists(card)) {
            removeCardById(card.getCardId());
        }

        card.setCardId(generateNextCardId());
        cards.add(card);
    }

    private boolean isCardExists(CardGlobal card) {
        return cards.contains(card);
    }

    private int generateNextCardId() {
        // will be replaced with database query in future
        currentCardId++;
        return currentCardId;
    }

    public CardGlobal getCardById(int id) {
        return cards.stream()
                .filter((card)-> card.getCardId() == id)
                .findFirst().orElse(null);
    }

    public CardGlobal getCardByName(String cardName) {
        return cards.stream()
                .filter((card)-> isCardHasName(card, cardName))
                .findFirst().orElse(null);
    }

    private boolean isCardHasName(CardGlobal card, String cardName) {
        return card.getCharacterInfo().getFullName().equalsIgnoreCase(cardName);
    }

    public void removeCardById(int id) {
        cards.removeIf((card) -> card.getCardId() == id);
    }

    public List<CardGlobal> getRandomCards(int amount) {
        List<CardGlobal> resultCards = new ArrayList<>(amount);

        if (cards.isEmpty()){
            return new ArrayList<>();
        }

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

    public List<CardGlobal> getAllCards() {
        return cards;
    }
}
