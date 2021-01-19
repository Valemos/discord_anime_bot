package game.cards;

import bot.commands.SortingType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractCardsManager<T extends ComparableCard> {
    List<T> cards;

    public AbstractCardsManager(List<T> cards) {
        this.cards = cards;
    }

    protected Stream<T> filterByName(String name, Stream<T> stream) {
        if (name != null) {
            return stream.filter((card) -> card.getName().toLowerCase().contains(name.toLowerCase()));
        } else {
            return stream;
        }
    }

    protected Stream<T> filterBySeries(String series, Stream<T> stream) {
        if (series != null) {
            return stream.filter((card) -> card.getSeries().toLowerCase().contains(series.toLowerCase()));
        } else {
            return stream;
        }
    }

    public List<T> getCardsFiltered(String name, String series) {
        return getCardsSorted(name, series, null);
    }

    public List<T> getCardsSorted(List<SortingType> sortingTypes) {
        return getCardsSorted(null, null, sortingTypes);
    }

    public List<T> getCardsSorted(String name, String series, List<SortingType> sortingTypes) {
        return sortCards(sortingTypes,
                filterBySeries(series,
                        filterByName(name,
                                cards.stream())))
                .collect(Collectors.toList());
    }

    protected abstract Comparator<T> getComparator(SortingType sortingType);

    protected Stream<T> sortCards(List<SortingType> sortingTypes, Stream<T> cardStream) {
        if (sortingTypes == null || sortingTypes.isEmpty()) {
            return cardStream;
        }

        Comparator<T> comparator = getCardComparators(sortingTypes);

        return cardStream.sorted(comparator);
    }

    private Comparator<T> getCardComparators(List<SortingType> sortingTypes) {
        Comparator<T> comparator = getComparator(sortingTypes.get(0));
        for (int i = 1; i < sortingTypes.size(); i++) {
            comparator.thenComparing(getComparator(sortingTypes.get(1)));
        }
        return comparator;
    }

    public T getById(String id) {
        return cards.stream()
                .filter((card)-> card.getId().equals(id))
                .findFirst().orElse(null);
    }
}
