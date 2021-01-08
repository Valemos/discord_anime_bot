package game;

public enum Charisma {
    CHARISMATIC(1), NEUTRAL(0), AWKWARD(-1);

    private final int state;

    Charisma(int state) {
        this.state = state;
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
}
