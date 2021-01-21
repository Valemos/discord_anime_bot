package game.cards;

public interface DisplayableCard {
    CharacterInfo getCharacterInfo();

    String getName();

    String getSeriesName();

    String getId();

    static boolean containsName(DisplayableCard c, String filterName){
        return String.valueOf(c.getName()).toLowerCase().contains(filterName.toLowerCase());
    }

    static boolean containsSeries(DisplayableCard c, String filterSeries) {
        return String.valueOf(c.getSeriesName()).toLowerCase().contains(filterSeries.toLowerCase());
    }
}
