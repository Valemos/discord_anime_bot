package game;

enum Material {
    Wood("Wood"),
    Stone("Stone"),
    Iron("Iron"),
    Gold("Gold"),
    Diamond("Diamond"),
    Emerald("Emerald"),

    Blackstone("Blackstone"),
    ShadowSteel("Shadow Steel"),
    SkySilkworm("Sky Silkworm");

    private final String name;

    Material(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
