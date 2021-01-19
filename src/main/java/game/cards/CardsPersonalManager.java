package game.cards;

import bot.commands.SortingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardsPersonalManager extends AbstractCardsManager<CardPersonal> {
    private final Session dbSession;

    public CardsPersonalManager(Session session) {
        super(new ArrayList<>());
        dbSession = session;
    }

    protected Stream<CardPersonal> filterByPlayerId(String playerId, Stream<CardPersonal> stream) {
        return stream.filter(c -> c.getOwner().getId().equals(playerId));
    }

    public CardPersonal getById(String cardId) {
        return dbSession.get(CardPersonal.class, cardId);
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

        return Comparator.comparing(CardPersonal::getId);
    }

    public List<CardPersonal> getSorted(String playerId, String name, String series, List<SortingType> sortingTypes) {
        return sortCards(sortingTypes,
                filterBySeries(series,
                        filterByName(name,
                                filterByPlayerId(playerId,
                                cards.stream()))))
                .collect(Collectors.toList());
    }

    public boolean removeCard(CardPersonal card) {
        dbSession.beginTransaction();
        dbSession.delete(card);
        dbSession.getTransaction().commit();
        return true;
    }
}
