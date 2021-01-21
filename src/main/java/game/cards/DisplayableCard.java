package game.cards;

public interface DisplayableCard {
    CharacterInfo getCharacterInfo();

    String getName();

    String getSeries();

    String getId();

    static boolean containsName(DisplayableCard c, String filterName){
        return String.valueOf(c.getName()).toLowerCase().contains(filterName.toLowerCase());
    }

    static boolean containsSeries(DisplayableCard c, String filterSeries) {
        return String.valueOf(c.getSeries()).toLowerCase().contains(filterSeries.toLowerCase());
    }
}
