package game.cooldown;

import game.MappedObjectManager;

public class CooldownManager extends MappedObjectManager<String, CooldownSet> {

    public CooldownManager() {
        super(CooldownSet.class);
    }
}
