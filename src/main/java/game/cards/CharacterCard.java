package game.cards;

import game.CardWeapon;
import game.cards.CardStats;

public class CharacterCard {
    String id = null;
    String characterName;
    String seriesName;
    String imageUrl;

    CardStats stats;

    CardWeapon weaponEquipped;

    public CharacterCard(String characterName, String seriesName, String imageUrl, CardStats stats) {
        this.characterName = characterName;
        this.seriesName = seriesName;
        this.imageUrl = imageUrl;
        this.stats = stats;
        weaponEquipped = null;
    }

    public CardStats getStats() {
        return stats;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getOneLineString() {
        return characterName + " / " + seriesName;
    }
}
