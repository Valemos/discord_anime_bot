package game.cards;

import bot.commands.SortingType;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionTransformer<T extends SearchableCard & ComparableCard> {

    private Stream<T> stream;

    public CollectionTransformer(Stream<T> cards) {
        Objects.requireNonNull(cards);
        stream = cards;
    }

    public CollectionTransformer<T> sort(List<SortingType> sortingTypes) {
        if (sortingTypes != null && !sortingTypes.isEmpty()) {
            stream = stream.sorted(getCardComparators(sortingTypes));
        }
        return this;
    }

    private Comparator<T> getCardComparators(List<SortingType> sortingTypes) {
        Comparator<T> comparator = getComparator(sortingTypes.get(0));
        for (int i = 1; i < sortingTypes.size(); i++) {
            comparator.thenComparing(getComparator(sortingTypes.get(1)));
        }
        return comparator;
    }

    protected Comparator<T> getComparator(SortingType sortingType){
        if (SortingType.FAVOR == sortingType ||
                SortingType.F == sortingType){

            return ComparableCard::comparatorFavor;

        } else if (SortingType.PRINT == sortingType ||
                SortingType.P == sortingType){

            return ComparableCard::comparatorPrint;

        } else if (SortingType.POWER == sortingType ||
                SortingType.PW == sortingType){

            return ComparableCard::comparatorPower;
        }
        return (c1, c2) -> 0;
    }

    public List<T> toList() {
        if (stream != null){
            return stream.collect(Collectors.toList());
        }
        return null;
    }

    public CollectionTransformer<T> filterName(String name) {
        if(name != null) stream = stream.filter(c -> SearchableCard.containsName(c, name));
        return this;
    }

    public CollectionTransformer<T> filterSeries(String series) {
        if(series != null) stream = stream.filter(c -> SearchableCard.containsSeries(c, series));
        return this;
    }
}
