package game.player_objects.squadron;

public enum PatrolType {
    OVERWORLD("Overworld"),
    O(""), // alias to overworld
    UNDERWORLD("Underworld"),
    U(""); // alias to underworld

    private final String typeName;

    PatrolType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static PatrolType getTypeNameFromAlias(PatrolType patrolType){
        if (PatrolType.O == patrolType){
            return  PatrolType.OVERWORLD;

        }else if (PatrolType.U == patrolType){
            return  PatrolType.UNDERWORLD;

        }else{
            return  patrolType;
        }
    }
}
