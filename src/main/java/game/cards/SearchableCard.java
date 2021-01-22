package game.cards;

public interface SearchableCard {
    CharacterInfo getCharacterInfo();

    String getId();
    String getName();
    String getSeriesName();

    static boolean containsName(SearchableCard c, String filterName){
        return String.valueOf(c.getName()).toLowerCase().contains(filterName.toLowerCase());
    }

    static boolean containsSeries(SearchableCard c, String filterSeries) {
        return String.valueOf(c.getSeriesName()).toLowerCase().contains(filterSeries.toLowerCase());
    }
}
