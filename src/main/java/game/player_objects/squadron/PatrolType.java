package game.player_objects.squadron;

public enum PatrolType {
    OVERWORLD("Overworld"),
    O(""),
    UNDERWORLD("Underworld"),
    U("");

    private final String typeName;

    PatrolType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
