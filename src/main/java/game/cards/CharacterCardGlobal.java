package game.cards;

public class CharacterCardGlobal {

    CharacterInfo characterInfo;
    CardStatsGlobal stats;

    public CharacterCardGlobal(String characterName, String seriesName, String imageUrl) {
        this(characterName, seriesName, imageUrl, new CardStatsGlobal());
    }

    public CharacterCardGlobal(String characterName, String seriesName, String imageUrl, CardStatsGlobal stats) {
        characterInfo = new CharacterInfo(characterName, seriesName, imageUrl);
        this.stats = stats;
    }

    public CardStatsGlobal getStats() {
        return stats;
    }

    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }

    public void setCardId(int newCardId) {
        characterInfo.setId(newCardId);
    }

    public int getCardId() {
        return characterInfo.getId();
    }

    public String getOneLineString() {
        return characterInfo.characterName + " / " + characterInfo.seriesName;
    }

    public CharacterCardPersonal getPersonalCardForPickDelay(String userId, float pickDelay){
        return new CharacterCardPersonal(userId, characterInfo, stats.getStatsForPickDelay(pickDelay));
    }

    public String getOneLineRepresentationString() {
        return characterInfo.getFullName()
                + ": "
                + stats.getStatsForPickDelay(0).getOneLineString();
    }
}
