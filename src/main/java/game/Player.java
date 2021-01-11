package game;

import bot.CommandAccessLevel;
import game.cards.PersonalCollection;

public class Player {
    private final String userId;
    private CommandAccessLevel commandAccessLevel;
    private final PersonalCollection collection;
    private final MaterialInventory materialInventory;


    public Player(String userId, CommandAccessLevel commandAccessLevel) {
        this(userId, commandAccessLevel, new PersonalCollection(), new MaterialInventory());
    }

    public Player(String userId, CommandAccessLevel commandAccessLevel, PersonalCollection collection, MaterialInventory materialInventory) {
        this.userId = userId;
        this.commandAccessLevel = commandAccessLevel;
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

    public CommandAccessLevel getAccessLevel() {
        return commandAccessLevel;
    }

    public void setAccessLevel(CommandAccessLevel commandAccessLevel) {
        this.commandAccessLevel = commandAccessLevel;
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
