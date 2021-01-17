package bot.commands.user;

import game.Cooldown;

import java.time.Instant;

public class CooldownSet {
    private final Cooldown drop;
    private final Cooldown grab;

    public CooldownSet() {
        drop = new Cooldown("Drop", 120);
        grab = new Cooldown("Grab", 60);
    }

    public Cooldown getDrop() {
        return drop;
    }

    public Cooldown getGrab() {
        return grab;
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
        drop.setUsed(null);
        grab.setUsed(null);
    }
}
