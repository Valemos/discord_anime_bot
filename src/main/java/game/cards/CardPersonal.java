package game.cards;

import java.util.Objects;

public class CardPersonal {
    String cardId;
    String playerId; // TODO replace with database query in personal collections

    CharacterInfo characterInfo;
    CardStatsConstant stats;
    CardEquipment cardEquipment;
    private float powerLevel;


    public CardPersonal(CharacterInfo characterInfo, CardStatsConstant stats) {
        this(characterInfo, stats, new CardEquipment());
    }

    public CardPersonal(CharacterInfo characterInfo, CardStatsConstant stats, CardEquipment cardEquipment) {
        this.characterInfo = characterInfo;
        this.cardEquipment = cardEquipment;
        setStats(stats);
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public CardStatsConstant getStats() {
        return stats;
    }

    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }

    public void setStats(CardStatsConstant stats) {
        this.stats = stats;
        updatePowerLevel(stats);
    }

    private void updatePowerLevel(CardStatsConstant stats) {
        powerLevel = stats.getPowerLevel() + cardEquipment.getPowerLevel();
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardPersonal){
            CardPersonal other = (CardPersonal) obj;
            return  Objects.equals(characterInfo, other.characterInfo) &&
                    Objects.equals(stats, other.stats);
        }
        return false;
    }

    public float getPowerLevel(){
        return powerLevel;
    }

    public String getOneLineRepresentationString() {
        return characterInfo.getFullName() + ": " + stats.getOneLineString();
    }

    public String getName() {
        return characterInfo.characterName;
    }

    public String getSeries() {
        return characterInfo.seriesTitle;
    }

    public float getApprovalRating(){
        return stats.approvalRating;
    }
}
