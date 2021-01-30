package game.player_objects;

import javax.persistence.Embeddable;
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

    protected void set(String name, int amountSeconds) {
        this.name = name;
        this.amountSeconds = amountSeconds;
    }

    public Cooldown(String name, int amountSeconds) {
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

    public boolean isAvailable(Instant time) {
        return getTimeLeft(time) == 0;
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

    public String getDescription(Instant time) {
        return name + ": " + getSecondsLeftString(time);
    }

    public String getVerboseDescription(Instant time) {
        return name + " cooldown is " + getSecondsLeftString(time);
    }

    public String getSecondsLeftString(Instant time) {
        long seconds = getTimeLeft(time);
        return seconds > 0 ? formatSeconds(seconds) : "ready";
    }

    private String formatSeconds(long seconds) {
        if (seconds >= 3600){
            return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);

        }else if (seconds >= 60){
            return String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60);

        }else{
            return String.format("%02d", seconds % 60);
        }
    }

    public long getTimeLeft(Instant time) {
        if (lastUse == null){
            return 0;
        }

        long duration = Duration.between(lastUse, time).toSeconds();
        if (duration > amountSeconds){
            return 0;
        }

        return amountSeconds - duration;
    }
}
