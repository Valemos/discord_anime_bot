package game;

import bot.PlayerAccessLevel;
import game.cards.PersonalCollection;

public class Player {
    private final String userId;
    private PlayerAccessLevel playerAccessLevel;
    private final PersonalCollection collection;
    private final MaterialInventory materialInventory;


    public Player(String userId, PlayerAccessLevel playerAccessLevel) {
        this(userId, playerAccessLevel, new PersonalCollection(), new MaterialInventory());
    }

    public Player(String userId, PlayerAccessLevel playerAccessLevel, PersonalCollection collection, MaterialInventory materialInventory) {
        this.userId = userId;
        this.playerAccessLevel = playerAccessLevel;
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

    public PlayerAccessLevel getAccessLevel() {
        return playerAccessLevel;
    }

    public void setAccessLevel(PlayerAccessLevel playerAccessLevel) {
        this.playerAccessLevel = playerAccessLevel;
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
