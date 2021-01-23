package game.player_objects.squadron;

public enum PowerUpType {
    STRENGTH("Strength potion"),
    DEXTERITY("Dexterity potion"),
    WISDOM("Wisdom potion");

    private final String name;

    PowerUpType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
