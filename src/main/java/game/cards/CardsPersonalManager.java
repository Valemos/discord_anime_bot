package game.cards;

import bot.commands.SortingType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardsPersonalManager extends AbstractCardsManager<CardPersonal> {
    private int currentCardId = 0;

    public CardsPersonalManager() {
        this(new ArrayList<>());
    }

    public CardsPersonalManager(List<CardPersonal> cards) {
        super(cards);
    }

    protected Stream<CardPersonal> filterByPlayerId(String playerId, Stream<CardPersonal> stream) {
        return stream.filter(c -> c.getPlayerId().equals(playerId));
    }

    public List<CardPersonal> getCardsPlayer(String playerId) {
        return filterByPlayerId(playerId, cards.stream()).collect(Collectors.toList());
    }

    public CardPersonal getCardById(String cardId) {
        return cards.stream()
                .filter((card) -> card.getCardId().equals(cardId))
                .findFirst().orElse(null);
    }

    public void addCard(String playerId, CardPersonal card) {
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


    @Override
    protected Comparator<CardPersonal> getComparator(SortingType sortingType) {
        if (SortingType.FAVOR == sortingType ||
                SortingType.F == sortingType){

            return (c1, c2) -> Float.compare(c2.getApprovalRating(), c1.getApprovalRating());

        } else if (SortingType.PRINT == sortingType ||
                SortingType.P == sortingType){

            return Comparator.comparingInt(CardPersonal::getCardPrint);
        } else if (SortingType.POWER == sortingType ||
                SortingType.PW == sortingType){
            return (c1, c2) -> Float.compare(c1.getPowerLevel(), c2.getPowerLevel());
        }

        return Comparator.comparing(CardPersonal::getCardId);
    }

    public List<CardPersonal> getCardsSorted(String playerId, String name, String series, List<SortingType> sortingTypes) {
        return sortCards(sortingTypes,
                filterBySeries(series,
                        filterByName(name,
                                filterByPlayerId(playerId,
                                cards.stream()))))
                .collect(Collectors.toList());
    }
}
