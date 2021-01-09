package game;

public enum Charisma {
    CHARISMATIC(1, "Charismatic"),
    NEUTRAL(0, "Neutral"),
    AWKWARD(-1, "Awkward");

    private final int state;
    private final String name;

    Charisma(int state, String name) {
        this.state = state;
        this.name = name;
    }

    public static Charisma fromValue(int state){
        for (Charisma c : values()){
            if (state == c.getState()){
                return c;
            }
        }
        throw new RuntimeException("cannot instantiate charisma state");
    }

    public int getState() {
        return state;
    }

    public String getName() {
        return name;
    }
}
