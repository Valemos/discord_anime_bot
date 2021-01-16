package game.cards;

import game.DisplayableStats;

import java.util.Objects;

public class CardPersonal implements DisplayableStats, ComparableCard {
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

    @Override
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

    public float getApprovalRating(){
        return stats.approvalRating;
    }

    public int getCardPrint() {
        return stats.cardPrint;
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

    @Override
    public String getName() {
        return characterInfo.characterName;
    }

    @Override
    public String getSeries() {
        return characterInfo.seriesTitle;
    }

    @Override
    public String getId() {
        return characterInfo.getId();
    }

    @Override
    public String getNameStats() {
        return characterInfo.getFullName() + ": " + stats.getDescription();
    }

    @Override
    public String getIdName() {
        return cardId + " - " + characterInfo.getFullName();
    }

    @Override
    public String getIdNameStats() {
        return getIdName() + ": " + stats.getDescription();
    }

    @Override
    public String getFullDescription() {
        return getIdNameStats();
    }

    public static int comparatorPowerLevel(CardPersonal card1, CardPersonal card2) {
        return Float.compare(card2.getPowerLevel(), card1.getPowerLevel());
    }
}
