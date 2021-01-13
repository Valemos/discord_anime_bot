package game.cards;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardsGlobalManager {
    List<CardGlobal> cards;
    private int currentCardId = 0;

    public CardsGlobalManager() {
        this(new ArrayList<>());
    }

    public CardsGlobalManager(List<CardGlobal> cards) {
        this.cards = cards;
    }

    public void addCard(CardGlobal card) {
        if (isCardExists(card)) {
            removeCardById(card.getId());
        }

        card.setId(generateNextCardId());
        cards.add(card);
    }

    private boolean isCardExists(CardGlobal card) {
        return cards.contains(card);
    }

    private String generateNextCardId() {
        return String.valueOf(++currentCardId);
    }

    public CardGlobal getCardById(String id) {
        return cards.stream()
                .filter((card)-> card.getId().equals(id))
                .findFirst().orElse(null);
    }

    public List<CardGlobal> getAllCardsByNameAndSeries(String name, String series) {
        return filterCardsByName(name,
                filterCardsBySeries(series,
                        cards.stream()
                )
        ).collect(Collectors.toList());
    }

    public CardGlobal getCardByNameAndSeries(String name, String series) {
        List<CardGlobal> cardsFound = getAllCardsByNameAndSeries(name, series);
        if (cardsFound.size() == 1){
            return cardsFound.get(0);
        }
        return null;
    }

    @NotNull
    private Stream<CardGlobal> filterCardsByName(String name, Stream<CardGlobal> stream) {
        return stream.filter((card) -> containsIgnoreCase(card.getCharacterInfo().characterName, name));
    }

    @NotNull
    private Stream<CardGlobal> filterCardsBySeries(String series, Stream<CardGlobal> stream) {
        if (series != null){
            return stream.filter((card) -> containsIgnoreCase(card.getCharacterInfo().seriesTitle, series));
        }else{
            return stream;
        }
    }

    private boolean containsIgnoreCase(String cardName, String searchName) {
        return cardName.toLowerCase().contains(searchName.toLowerCase());
    }

    public void removeCardById(String id) {
        cards.removeIf((card) -> card.getId().equals(id));
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
