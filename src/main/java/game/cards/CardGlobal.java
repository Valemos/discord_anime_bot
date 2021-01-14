package game.cards;

public class CardGlobal implements game.DisplayableStats {

    CharacterInfo characterInfo;
    CardStatsGlobal stats;

    public CardGlobal(String characterName, String seriesName, String imageUrl) {
        this(characterName, seriesName, imageUrl, new CardStatsGlobal());
    }

    public CardGlobal(String characterName, String seriesName, String imageUrl, CardStatsGlobal stats) {
        characterInfo = new CharacterInfo(characterName, seriesName, imageUrl);
        this.stats = stats;
    }

    public CardStatsGlobal getStats() {
        return stats;
    }

    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }

    public void setId(String newCardId) {
        characterInfo.setId(newCardId);
    }

    public String getId() {
        return characterInfo.getId();
    }

    public String getFullName() {
        return characterInfo.getFullName();
    }

    @Override
    public String getNameStatsString() {
        return characterInfo.getFullName()
                + ": "
                + stats.getStatsForPickDelay(0).getOneLineString();
    }

    @Override
    public String getIdName() {
        return getId() + " - " + getFullName();
    }
}
