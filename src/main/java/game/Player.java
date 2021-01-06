package game;

import bot.AccessLevel;
import game.cards.PersonalCollection;

public class Player {
    private final String userId;
    private AccessLevel accessLevel;
    private PersonalCollection collection;
    private MaterialInventory materialInventory;

    public Player(String userId, AccessLevel accessLevel) {
        this.userId = userId;
        this.accessLevel = accessLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player){
            return userId.equals(((Player) obj).userId);
        }
        return false;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getId() {
        return userId;
    }
}
