package game.cards;

import bot.commands.SortingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardsGlobalManager extends AbstractCardsManager<CardGlobal> {

    private int currentCardId = 0;
    private final Session dbSession;

    public CardsGlobalManager(Session dbSession) {
        super(new ArrayList<>());
        this.dbSession = dbSession;
    }

    @Override
    protected Comparator<CardGlobal> getComparator(SortingType sortingType) {
        if (SortingType.FAVOR == sortingType ||
                SortingType.F == sortingType){

            return (c1, c2) -> Float.compare(c2.getStats().calcApprovalRating(), c1.getStats().calcApprovalRating());

        } else if (SortingType.PRINT == sortingType ||
                SortingType.P == sortingType){

            return Comparator.comparingInt(c -> c.getStats().amountCardsPrinted);
        } else if (SortingType.POWER == sortingType ||
                SortingType.PW == sortingType){
            return CardGlobal::comparatorStats;
        }
        return Comparator.comparing(CardGlobal::getId);
    }

    public void addCard(CardGlobal card) {
        if (isCardExists(card)) {
            removeCardById(card.getId());
        }

        //TODO add fetch query
        cards.add(card);
    }

    private boolean isCardExists(CardGlobal card) {
        if (card.getId() != null){
            return cards.contains(card);
        }
        return false;
    }

    private String generateNextCardId() {
        return String.valueOf(++currentCardId);
    }

    public List<CardGlobal> getFilteredCards(String name, String series) {
        return filterCardsByName(name,
                filterCardsBySeries(series,
                        cards.stream()
                )
        ).collect(Collectors.toList());
    }

    public CardGlobal getFirstCard(String name, String series) {
        List<CardGlobal> cardsFound = getFilteredCards(name, series);
        if (cardsFound.size() == 1){
            return cardsFound.get(0);
        }
        return null;
    }

    @NotNull
    private Stream<CardGlobal> filterCardsByName(String name, Stream<CardGlobal> stream) {
        if (name != null) {
            return stream.filter((card) -> containsIgnoreCase(card.getCharacterInfo().getName(), name));
        }else{
            return stream;
        }
    }

    @NotNull
    private Stream<CardGlobal> filterCardsBySeries(String series, Stream<CardGlobal> stream) {
        if (series != null){
            return stream.filter((card) -> containsIgnoreCase(card.getCharacterInfo().getSeriesName(), series));
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

    public List<CardGlobal> getFilteredCards() {
        return cards;
    }
}
