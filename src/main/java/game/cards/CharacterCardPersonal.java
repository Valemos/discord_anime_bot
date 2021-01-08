package game.cards;

import game.Constitution;

import java.util.Objects;

public class CharacterCardPersonal {
    String userId;
    CharacterInfo characterInfo;
    CardStatsConstant stats;

    Constitution constitution;  // the card is injured or not
    CardEquipment cardEquipment;

    public CharacterCardPersonal(String userId, CharacterInfo characterInfo, CardStatsConstant stats) {
        this(userId, characterInfo, stats, Constitution.HEALTHY, new CardEquipment());
    }

    public CharacterCardPersonal(String userId, CharacterInfo characterInfo, CardStatsConstant stats, Constitution constitution, CardEquipment cardEquipment) {
        this.userId = userId;
        this.characterInfo = characterInfo;
        this.stats = stats;
        this.constitution = constitution;
        this.cardEquipment = cardEquipment;
    }

    public CardStatsConstant getStats() {
        return stats;
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
}
