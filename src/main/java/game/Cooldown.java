package game;

import java.time.Duration;
import java.time.Instant;

public class Cooldown {

    private final String name;
    int amountSeconds;
    Instant lastUse = null;

    public Cooldown(String name, int amountSeconds) {
        this.name = name;
        this.amountSeconds = amountSeconds;
    }

    public void setLastUse(Instant lastUse) {
        this.lastUse = lastUse;
    }

    public Instant getLastUse() {
        return lastUse;
    }

    public String getDescription(Instant time) {
        return name + ": " + getSecondsLeftString(time);
    }

    public String getSecondsLeftString(Instant time) {
        long seconds = getSecondsLeft(time);
        return seconds > 0 ? String.valueOf(seconds) : "ready";
    }

    public long getSecondsLeft(Instant time) {
        if (lastUse == null){
            return 0;
        }

        long duration = Duration.between(lastUse, time).toSeconds();
        if (duration > amountSeconds){
            return 0;
        }

        return amountSeconds - duration;
    }

    public boolean isAvailable(Instant time) {
        return getSecondsLeft(time) == 0;
    }
}
