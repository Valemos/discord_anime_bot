package game.cards;

import game.HealthState;

import java.util.Objects;

public class CharacterCardPersonal {
    String userId;
    int cardId;

    CharacterInfo characterInfo;
    CardStatsConstant stats;
    CardEquipment cardEquipment;

    HealthState healthState;

    public CharacterCardPersonal(String userId, CharacterInfo characterInfo, CardStatsConstant stats) {
        this(userId, characterInfo, stats, HealthState.HEALTHY, new CardEquipment());
    }

    public CharacterCardPersonal(String userId, CharacterInfo characterInfo, CardStatsConstant stats, HealthState healthState, CardEquipment cardEquipment) {
        cardId = -1;
        this.userId = userId;
        this.characterInfo = characterInfo;
        this.stats = stats;
        this.healthState = healthState;
        this.cardEquipment = cardEquipment;
    }

    public CardStatsConstant getStats() {
        return stats;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CharacterCardPersonal){
            CharacterCardPersonal other = (CharacterCardPersonal) obj;
            return  Objects.equals(userId, other.userId) &&
                    Objects.equals(characterInfo, other.characterInfo) &&
                    Objects.equals(stats, other.stats);
        }
        return false;
    }

    public float calculatePowerLevel(){
        float powerLevel = stats.getPowerLevel() + cardEquipment.getPowerLevel();
        return healthState == HealthState.INJURED ? powerLevel * 0.2f : powerLevel;
    }

    public String getOneLineRepresentationString() {
        return characterInfo.getFullName() + ": " + stats.getOneLineString();
    }
}
