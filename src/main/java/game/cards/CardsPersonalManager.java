package game.cards;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CardsPersonalManager extends AbstractCardsManager<CardPersonal> {

    public CardsPersonalManager(Session dbSession) {
        super(CardPersonal.class, dbSession);
    }

    public void removeCard(CardPersonal card) {
        dbSession.beginTransaction();
        card.getOwner().getCards().remove(card);
        dbSession.remove(card);
        dbSession.getTransaction().commit();
    }

    public void removeCharacterCards(CharacterInfo characterInfo) {
        dbSession.beginTransaction();

        for (CardPersonal card : getCharacterCards(characterInfo)){
            dbSession.delete(card);
        }

        dbSession.getTransaction().commit();
    }

    private List<CardPersonal> getCharacterCards(CharacterInfo characterInfo) {
        CriteriaBuilder cb = dbSession.getCriteriaBuilder();
        CriteriaQuery<CardPersonal> q = cb.createQuery(CardPersonal.class);
        Root<CardPersonal> root = q.from(CardPersonal.class);

        return dbSession.createQuery(q.select(root).where(
                cb.equal(root.get("characterInfo").get("id"), characterInfo.getId())
        )).list();
    }
}
