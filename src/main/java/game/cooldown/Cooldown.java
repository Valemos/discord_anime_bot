package game.cooldown;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import java.time.Duration;
import java.time.Instant;

@Embeddable
public class Cooldown {

    @Transient
    private String name;
    @Transient
    int amountSeconds;

    Instant lastUse = null;

    public Cooldown() {
    }

    public Cooldown(String name, int amountSeconds) {
        set(name, amountSeconds);
    }

    protected void set(String name, int amountSeconds) {
        this.name = name;
        this.amountSeconds = amountSeconds;
    }

    public void setUsed(Instant lastUse) {
        this.lastUse = lastUse;
    }

    public Instant getLastUse() {
        return lastUse;
    }

    public void setLastUse(Instant lastUse) {
        this.lastUse = lastUse;
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

    public void reset() {
        lastUse = null;
    }

    public boolean tryUse(Instant time) {
        if (isAvailable(time)){
            setUsed(time);
            return true;
        }
        return false;
    }

    public String getVerboseDescription(Instant time) {
        return "your " + name + " cooldown is " + getSecondsLeftString(time);
    }
}
