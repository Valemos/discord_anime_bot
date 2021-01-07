package game.cards;

public class CharacterCardGlobal {

    CharacterInfo characterInfo;
    CardStatsUpdatable stats;

    public CharacterCardGlobal(String characterName, String seriesName, String imageUrl, CardStats stats) {
        characterInfo = new CharacterInfo(characterName, seriesName, imageUrl);
        this.stats = new CardStatsUpdatable(stats);
    }

    public CardStatsUpdatable getStats() {
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

    public CharacterCardPersonal getConstantCopy(String userId) {
        return new CharacterCardPersonal(userId, characterInfo, stats);
    }
}
