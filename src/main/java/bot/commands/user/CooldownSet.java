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

    public void useDrop(Instant time){
        drop.setLastUse(time);
    }

    public void useGrab(Instant time){
        grab.setLastUse(time);
    }

    public boolean checkDrop(Instant time){
        return drop.isAvailable(time);
    }

    public boolean checkGrab(Instant time){
        return grab.isAvailable(time);
    }

    public String getDescription(Instant time) {
        return drop.getDescription(time) + "\n" +
                grab.getDescription(time);
    }

    public String getDropTimeLeft(Instant time) {
        return drop.getTimeLeftString(time);
    }

    public String getGrabTimeLeft(Instant time) {
        return grab.getTimeLeftString(time);
    }

    public void reset() {
        drop.setLastUse(null);
        grab.setLastUse(null);
    }
}
