package game.squadron;

public enum PatrolType {
    OVERWORLD("Overworld"),
    UNDERWORLD("Underworld");

    private final String typeName;

    PatrolType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
