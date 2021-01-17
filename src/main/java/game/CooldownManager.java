package game;

import bot.commands.user.CooldownSet;

public class CooldownManager extends MappedObjectManager<String, CooldownSet> {

    public CooldownManager() {
        super(CooldownSet.class);
    }
}
