package game.materials;

import javax.annotation.Nullable;

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

    @Nullable
    public static Material fromString(String stringToFind) {
        try{
            return Material.valueOf(stringToFind.toUpperCase());
        }catch(IllegalArgumentException e){
            return null;
        }
    }

    public String getName() {
        return name;
    }
}
