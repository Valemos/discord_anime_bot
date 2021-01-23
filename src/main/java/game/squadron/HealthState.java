package game.squadron;

public enum HealthState {
    HEALTHY, INJURED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
