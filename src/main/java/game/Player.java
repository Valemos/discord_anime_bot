package game;

import bot.CommandPermissions;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.CardPersonal;
import game.cards.CardCollectionPersonal;
import game.items.InventoryItems;
import game.items.MaterialsSet;

public class Player {
    private final String userId;
    private CommandPermissions commandPermissions;
    private final CardCollectionPersonal collection;
    private final InventoryItems inventoryItems;
    private final MaterialsSet materialsSet;
    private Paginator cachedShopViewer;


    public Player(String userId, CommandPermissions commandPermissions) {
        this(userId, commandPermissions, new CardCollectionPersonal(), new MaterialsSet(), new InventoryItems(userId));
    }

    public Player(String userId,
                  CommandPermissions commandPermissions,
                  CardCollectionPersonal collection,
                  MaterialsSet materialsSet,
                  InventoryItems inventoryItems) {
        this.userId = userId;
        this.commandPermissions = commandPermissions;
        this.collection = collection;
        this.materialsSet = materialsSet;
        this.inventoryItems = inventoryItems;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player){
            return userId.equals(((Player) obj).userId);
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
        return userId;
    }

    public CardCollectionPersonal getCollection() {
        return collection;
    }

    public MaterialsSet getMaterials() {
        return materialsSet;
    }

    public void addPersonalCard(CardPersonal card) {
        collection.addCard(card);
    }

    public InventoryItems getInventoryItems() {
        return inventoryItems;
    }

    public void setShopViewer(Paginator shopViewer) {
        cachedShopViewer = shopViewer;
    }
}
