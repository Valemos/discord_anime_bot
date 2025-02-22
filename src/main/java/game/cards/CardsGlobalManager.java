package game.cards;

import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardsGlobalManager extends AbstractCardsManager<CardGlobal> {

    public CardsGlobalManager(Session dbSession) {
        super(CardGlobal.class, dbSession);
    }

    public boolean addCard(CardGlobal card) {
        if(!card.getCharacterInfo().isValid()){
            return false;
        }

        dbSession.beginTransaction();

        CardGlobal cardFound = getByCharacter(card.getCharacterInfo());
        if (cardFound != null){
            card.setCardInfo(cardFound);
            dbSession.getTransaction().commit();
            return true;
        }

        SeriesInfo series = findMatchingSeries(card.getCharacterInfo().getSeries());
        if(series != null){
            card.getCharacterInfo().setSeries(series);
        }else{
            dbSession.save(card.getCharacterInfo().getSeries());
        }

        dbSession.save(card.getCharacterInfo());
        dbSession.save(card);

        try{
            dbSession.getTransaction().commit();
            return true;

        }catch (PersistenceException e){
            Logger.getGlobal().log(Level.WARNING, e.getCause().getMessage());
            dbSession.getTransaction().rollback();
            return false;
        }
    }

    public CardGlobal getByCharacter(CharacterInfo character) {
        CriteriaBuilder cb = dbSession.getCriteriaBuilder();
        CriteriaQuery<CardGlobal> q = cb.createQuery(CardGlobal.class);
        Root<CardGlobal> root = q.from(CardGlobal.class);

        Predicate characterEquality;
        if(character.getId() == null){
            characterEquality = cb.equal(cb.lower(root.get("characterInfo").get("name")),
                                        character.getName().toLowerCase());

        }else{
            characterEquality = cb.equal(root.get("characterInfo").get("id"), character.getId());
        }
        
        try {
            return dbSession.createQuery(q.select(root).where(characterEquality)).getSingleResult();
        }catch(NoResultException | NonUniqueResultException e){
            return null;
        }
    }

    public void updateCharacterInfo(CharacterInfo info, CharacterInfo newInfo) {
        dbSession.beginTransaction();

        if (newInfo.getName() != null) info.setName(newInfo.getName());

        if (newInfo.getImageUrl() != null) info.setImageUrl(newInfo.getImageUrl());

        if (newInfo.getSeriesName() != null){
            SeriesInfo series = findMatchingSeries(newInfo.getSeries());
            if (series == null) {
                series = newInfo.getSeries();
                dbSession.save(series);
            }
            info.setSeries(series);
        }

        dbSession.merge(info);
        dbSession.getTransaction().commit();
    }

    private SeriesInfo findMatchingSeries(SeriesInfo series) {
        return findMatchingSeries(series.getName());
    }

    private SeriesInfo findMatchingSeries(String seriesName) {
        CriteriaBuilder cb = dbSession.getCriteriaBuilder();
        CriteriaQuery<SeriesInfo> q = cb.createQuery(SeriesInfo.class);
        Root<SeriesInfo> root = q.from(SeriesInfo.class);

        try {
            return dbSession.createQuery(
                    q.select(root).where(
                            cb.equal(cb.lower(root.get("name")),
                                    String.valueOf(seriesName).toLowerCase())
                    )
            ).getSingleResult();
        }catch(NoResultException | NonUniqueResultException e){
            return null;
        }
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

    public List<SeriesInfo> getAllSeries() {
        CriteriaBuilder cb = dbSession.getCriteriaBuilder();
        CriteriaQuery<SeriesInfo> q = cb.createQuery(SeriesInfo.class);

        return dbSession.createQuery(q.select(q.from(SeriesInfo.class))).list();
    }
}
