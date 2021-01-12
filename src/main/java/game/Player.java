package game;

import bot.CommandAccessLevel;
import game.cards.CardPersonal;
import game.cards.CollectionPersonal;

public class Player {
    private final String userId;
    private CommandAccessLevel commandAccessLevel;
    private final CollectionPersonal collection;
    private final InventoryMaterial inventoryMaterial;


    public Player(String userId, CommandAccessLevel commandAccessLevel) {
        this(userId, commandAccessLevel, new CollectionPersonal(), new InventoryMaterial());
    }

    public Player(String userId, CommandAccessLevel commandAccessLevel, CollectionPersonal collection, InventoryMaterial inventoryMaterial) {
        this.userId = userId;
        this.commandAccessLevel = commandAccessLevel;
        this.collection = collection;
        this.inventoryMaterial = inventoryMaterial;
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

    public CollectionPersonal getCollection() {
        return collection;
    }

    public InventoryMaterial getMaterialInventory() {
        return inventoryMaterial;
    }

    public void addPersonalCard(CardPersonal card) {
        collection.addCard(card);
    }
}
