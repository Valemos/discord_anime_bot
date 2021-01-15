package game.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardsPersonalManager {
    List<CardPersonal> cards;
    private int currentCardId = 0;

    public CardsPersonalManager() {
        this(new ArrayList<>());
    }

    public CardsPersonalManager(List<CardPersonal> cards) {
        this.cards = cards;
    }

    public List<CardPersonal> getCards(String playerId) {
        return filterByPlayerId(playerId, cards.stream()).collect(Collectors.toList());
    }

    private Stream<CardPersonal> filterByPlayerId(String playerId, Stream<CardPersonal> stream) {
        return stream.filter(c -> c.getPlayerId().equals(playerId));
    }

    public CardPersonal getCardById(String cardId) {
        return cards.stream()
                .filter((card) -> card.getCardId().equals(cardId))
                .findFirst().orElse(null);
    }

    public void addCard(String playerId, CardPersonal card) {
        // TODO add database query to get card id
        card.setCardId(getNextCardId());
        card.setPlayerId(playerId);
        cards.add(card);
    }

    private String getNextCardId() {
        return String.valueOf(++currentCardId);
    }

    public boolean removeCard(String playerId, String cardId) {
        return cards.removeIf(c ->
                c.getCardId().equals(cardId) &&
                c.getPlayerId().equals(playerId)
        );
    }

    public boolean isCardExists(String playerId, String cardId) {
        return filterByPlayerId(playerId, cards.stream())
                .allMatch(card -> card.getCardId().equals(cardId));
    }

    public List<CardPersonal> getCards(String playerId, String name, String series) {
        return filterBySeries(series,
                filterByName(name,
                    filterByPlayerId(playerId,
                            cards.stream()
                    )
                )
        ).collect(Collectors.toList());
    }

    private Stream<CardPersonal> filterByName(String name, Stream<CardPersonal> stream) {
        if (name != null){
            return stream.filter((card) -> card.getName().toLowerCase().contains(name.toLowerCase()));
        }else{
            return stream;
        }
    }

    private Stream<CardPersonal> filterBySeries(String series, Stream<CardPersonal> stream) {
        if (series != null){
            return stream.filter((card) -> card.getSeries().toLowerCase().contains(series.toLowerCase()));
        }else{
            return stream;
        }
    }

}
