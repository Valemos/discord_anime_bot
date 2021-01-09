package game;

import bot.AccessLevel;
import game.cards.PersonalCollection;

public class Player {
    private final String userId;
    private AccessLevel accessLevel;
    private final PersonalCollection collection;
    private final MaterialInventory materialInventory;


    public Player(String userId, AccessLevel accessLevel) {
        this(userId, accessLevel, new PersonalCollection(), new MaterialInventory());
    }

    public Player(String userId, AccessLevel accessLevel, PersonalCollection collection, MaterialInventory materialInventory) {
        this.userId = userId;
        this.accessLevel = accessLevel;
        this.collection = collection;
        this.materialInventory = materialInventory;
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

    public PersonalCollection getCollection() {
        return collection;
    }

    public MaterialInventory getMaterialInventory() {
        return materialInventory;
    }
}
