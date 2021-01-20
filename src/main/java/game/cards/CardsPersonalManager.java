package game.cards;

import bot.commands.SortingType;
import org.hibernate.Session;

import java.util.Comparator;
import java.util.stream.Stream;

public class CardsPersonalManager extends AbstractCardsManager<CardPersonal> {

    public CardsPersonalManager(Session dbSession) {
        super(CardPersonal.class, dbSession);
    }

    public void removeCard(CardPersonal card) {
        dbSession.beginTransaction();
        dbSession.delete(card);
        dbSession.getTransaction().commit();
    }
}
