package game;

public enum Material {
    GOLD("Gold"),

    WOOD("Wood"),
    STONE("Stone"),
    IRON("Iron"),
    DIAMOND("Diamond"),
    EMERALD("Emerald"),

    BLACKSTONE("Blackstone"),
    SHADOW_STEEL("Shadow Steel"),
    SKY_SILKWORM("Sky Silkworm");

    private final String name;

    Material(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
