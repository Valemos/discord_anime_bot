package game;

import bot.CommandPermissions;
import game.cards.CardPersonal;
import game.cards.CardCollectionsPersonal;
import game.items.ItemCollectionsPersonal;
import game.items.MaterialsSet;
import game.squadron.Squadron;

public class Player {
    private final String id;
    private CommandPermissions commandPermissions;
    private final CardCollectionsPersonal collection;
    private final ItemCollectionsPersonal itemCollectionsPersonal;
    private final MaterialsSet materialsSet;
    private Squadron squadron;

    public Player(String id,
                  CommandPermissions commandPermissions,
                  CardCollectionsPersonal collection,
                  MaterialsSet materialsSet,
                  ItemCollectionsPersonal itemCollectionsPersonal) {
        this.id = id;
        this.commandPermissions = commandPermissions;
        this.collection = collection;
        this.materialsSet = materialsSet;
        this.itemCollectionsPersonal = itemCollectionsPersonal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player){
            return id.equals(((Player) obj).id);
        }
        return false;
    }

    public CommandPermissions getCommandPermissions() {
        return commandPermissions;
    }

    public void setAccessLevel(CommandPermissions commandPermissions) {
        this.commandPermissions = commandPermissions;
    }

    public String getId() {
        return id;
    }

    public CardCollectionsPersonal getCardsCollection() {
        return collection;
    }

    public MaterialsSet getMaterials() {
        return materialsSet;
    }

    public void addPersonalCard(CardPersonal card) {
        collection.addCard(id, card);
    }

    public ItemCollectionsPersonal getInventoryItems() {
        return itemCollectionsPersonal;
    }

    public void setSquadron(Squadron squadron) {
        this.squadron = squadron;
        squadron.setPlayerId(id);
    }

    public Squadron getSquadron() {
        return squadron;
    }
}
