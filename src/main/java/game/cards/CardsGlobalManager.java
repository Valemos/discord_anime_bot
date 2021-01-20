package game.cards;

import bot.commands.SortingType;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class CardsGlobalManager extends AbstractCardsManager<CardGlobal> {

    public CardsGlobalManager(Session dbSession) {
        super(CardGlobal.class, dbSession);
    }

    public void addCard(CardGlobal card) {
        dbSession.beginTransaction();
        dbSession.save(card.getCharacterInfo().getSeries()); // TODO add finding the same series
        dbSession.save(card.getCharacterInfo());
        dbSession.save(card);
        dbSession.getTransaction().commit();
    }

    public void removeCard(CardGlobal card) {
        dbSession.beginTransaction();
        dbSession.delete(card);
        dbSession.getTransaction().commit();
    }

    public List<CardGlobal> getRandomCards(int amount) {
        List<CardGlobal> cards = getAllCards();

        if (cards.isEmpty()){
            return new ArrayList<>();
        }

        List<CardGlobal> resultCards = new ArrayList<>(amount);

        Random random = new Random();
        for(int i = 0; i < amount; i++){
            int randomIndex = random.nextInt(cards.size());
            resultCards.add(cards.get(randomIndex));
        }

        return resultCards;
    }
}
