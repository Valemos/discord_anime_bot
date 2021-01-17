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
        return name + ": " + getTimeLeftString(time);
    }

    public String getTimeLeftString(Instant time) {
        if (lastUse == null){
            return "available";
        }
        return Duration.between(lastUse, time).toString().substring(2);
    }

    public boolean isAvailable(Instant time) {
        if (lastUse == null){
            return true;
        }
        return Duration.between(lastUse, time).toSeconds() < amountSeconds;
    }
}
