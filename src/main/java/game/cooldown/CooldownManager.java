package game.cooldown;

import game.MappedObjectManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CooldownManager extends MappedObjectManager<String, CooldownSet> {

    public CooldownManager(Session dbSession) {
        super(CooldownSet.class);
    }
}
