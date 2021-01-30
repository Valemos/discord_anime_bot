package game.player_objects;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.Instant;

@SuppressWarnings("ALL")
@Embeddable
public class CooldownSet {

    @Embedded
    @AttributeOverride(name = "lastUse", column = @Column(name = "drop_lastuse"))
    private Cooldown drop = createNewDrop();

    @Embedded
    @AttributeOverride(name = "lastUse", column = @Column(name = "grab_lastuse"))
    private Cooldown grab = createNewGrab();

    @Embedded
    @AttributeOverride(name = "lastUse", column = @Column(name = "daily_lastuse"))
    private Cooldown daily = createNewDaily();

    public CooldownSet() {
    }

    @PostLoad
    void postLoad() {
        if (drop == null) drop = createNewDrop();
        if (grab == null) grab = createNewGrab();
        if (daily == null) daily = createNewDaily();
    }

    @NotNull
    private Cooldown createNewDrop() {
        return new Cooldown("Drop", 120);
    }

    @NotNull
    private Cooldown createNewGrab() {
        return new Cooldown("Grab", 60);
    }

    @NotNull
    private Cooldown createNewDaily() {
        // seconds for 24 hours
        return new Cooldown("Daily", 86400);
    }

    public Cooldown getDrop() {
        return drop;
    }

    public void setDrop(Cooldown drop) {
        this.drop = drop;
    }

    public Cooldown getGrab() {
        return grab;
    }

    public void setGrab(Cooldown grab) {
        this.grab = grab;
    }

    public Cooldown getDaily() {
        return daily;
    }

    public void setDaily(Cooldown daily) {
        this.daily = daily;
    }

    public String getDescription(Instant time) {
        return drop.getDescription(time) + "\n" +
                grab.getDescription(time);
    }

    public void reset() {
        drop.reset();
        grab.reset();
        daily.reset();
    }
}
