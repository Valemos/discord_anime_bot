package game.player_objects.squadron;

public enum HealthState {
    HEALTHY, INJURED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
