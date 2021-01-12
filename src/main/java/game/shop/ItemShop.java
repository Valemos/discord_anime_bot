package game.shop;

import game.Player;
import game.items.MaterialsSet;
import game.items.ItemGlobal;
import game.items.ItemCollectionGlobal;

public class ItemShop {

    private final ItemCollectionGlobal itemsCollection;

    public ItemShop(ItemCollectionGlobal itemsCollection) {
        this.itemsCollection = itemsCollection;
    }

    public boolean tryBuyItem(Player player, String itemId) {
        ItemGlobal item = itemsCollection.getById(itemId);
        if (item != null){

            MaterialsSet inventoryCost = player.getMaterials();
            MaterialsSet itemCost = item.getMaterialsCost();

            if (inventoryCost.containsNotLessThan(itemCost)){
                player.getMaterials().subtractMaterials(itemCost);
                player.getInventoryItems().addItem(item);
                return true;
            }
        }
        return false;
    }
}
