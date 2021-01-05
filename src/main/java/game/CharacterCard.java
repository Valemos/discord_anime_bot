package game;

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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
