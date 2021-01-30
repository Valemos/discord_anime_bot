package game.cards;

import bot.commands.SortingType;
import game.CollectionTransformer;
import org.hibernate.Session;

import javax.annotation.Nonnull;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCardsManager<T extends SearchableCard & ComparableCard> {

    private final Class<T> cardClass;
    protected final Session dbSession;
    private CriteriaQuery<T> filterQuery;

    public AbstractCardsManager(Class<T> cardClass, Session dbSession) {
        this.cardClass = cardClass;
        this.dbSession = dbSession;
    }

    public List<T> getCardsSorted(List<SortingType> sortingTypes) {
        return getCardsSorted(null, null, sortingTypes);
    }

    public List<T> getCardsSorted(String name, String series, List<SortingType> sortingTypes) {
        return new CollectionTransformer<>(
                getFiltered(name, series).stream())
                .sort(sortingTypes).toList();
    }

    public void update(T element) {
        dbSession.merge(element);
    }

    public T getById(String id) {
        try{
            return dbSession.get(cardClass, id);
        }catch(NoResultException e){
            return null;
        }
    }

    protected List<T> getFilterResultList(Root<T> root, List<Predicate> predicates) {
        return dbSession.createQuery(filterQuery.select(root).where(predicates.toArray(new Predicate[0]))).getResultList();
    }

    protected Root<T> getQueryRoot(CriteriaBuilder criteriaBuilder) {
        filterQuery = criteriaBuilder.createQuery(cardClass);
        return filterQuery.from(cardClass);
    }

    @Nonnull
    protected List<Predicate> getNameSeriesPredicates(CriteriaBuilder cb, Root<T> root, String name, String series) {
        List<Predicate> predicates = new ArrayList<>(2);

        Path<Object> characterInfo = root.get("characterInfo");
        if(name != null)
            predicates.add(cb.like(cb.lower(characterInfo.get("name")), name.toLowerCase()));

        if(series != null)
            predicates.add(cb.like(cb.lower(characterInfo.get("series").get("name")), series.toLowerCase()));

        return predicates;
    }

    public List<T> getFiltered(String name, String series) {
        CriteriaBuilder cb = dbSession.getCriteriaBuilder();
        Root<T> root = getQueryRoot(cb);
        List<Predicate> predicates = getNameSeriesPredicates(cb, root, name, series);
        return getFilterResultList(root, predicates);
    }

    public T getUnique(String name, String series) {
        List<T> cardsFound = getFiltered(name, series);
        return cardsFound.size() == 1 ? cardsFound.get(0) : null;
    }

    public List<T> getAllCards() {
        CriteriaBuilder cb = dbSession.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(cardClass);
        return dbSession.createQuery(q.select(q.from(cardClass))).getResultList();
    }

    public void removeAll(){
        dbSession.beginTransaction();

        for (T card : getAllCards()){
            dbSession.delete(card);
        }

        dbSession.getTransaction().commit();
    }
}
