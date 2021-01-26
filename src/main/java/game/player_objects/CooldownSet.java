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

    public CooldownSet() {
    }

    @PostLoad
    void postLoad() {
        if (drop == null) drop = createNewDrop();
        if (grab == null) grab = createNewGrab();
    }

    @NotNull
    private Cooldown createNewDrop() {
        return new Cooldown("Drop", 120);
    }

    @NotNull
    private Cooldown createNewGrab() {
        return new Cooldown("Grab", 60);
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

    public void useDrop(Instant time){
        drop.setUsed(time);
    }

    public void useGrab(Instant time){
        grab.setUsed(time);
    }

    public String getDescription(Instant time) {
        return drop.getDescription(time) + "\n" +
                grab.getDescription(time);
    }

    public void reset() {
        drop.reset();
        grab.reset();
    }
}
