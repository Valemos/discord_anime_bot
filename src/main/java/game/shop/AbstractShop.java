package game.shop;

import game.Player;
import game.items.ItemsGlobalManager;
import game.items.ItemGlobal;
import game.items.MaterialsSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractShop {

    protected final ItemsGlobalManager itemCollection;
    ItemGlobal lastItemSelected;
    private final String messageTitle;

    public AbstractShop(ItemsGlobalManager items, String messageTitle) {
        this.itemCollection = items;
        this.messageTitle = messageTitle;
    }

    public boolean tryBuyItem(Player player, String itemId) {
        lastItemSelected = filterItems(itemCollection.getItems().stream())
                .filter(i -> i.getId().equals(itemId))
                .findFirst().orElse(null);

        if (lastItemSelected != null){

            MaterialsSet playerMaterials = player.getMaterials();
            MaterialsSet itemCost = lastItemSelected.getMaterialsCost();

            if (playerMaterials.containsEnough(itemCost)){
                player.getMaterials().subtractMaterials(itemCost);
//                player.addItem(lastItemSelected);
                //TODO add item query after items updated
                return true;
            }
        }
        return false;
    }

    protected abstract Stream<ItemGlobal> filterItems(Stream<ItemGlobal> stream);

    public ItemGlobal getLastItemSelected() {
        return lastItemSelected;
    }

    public List<ItemGlobal> getItems() {
        return filterItems(itemCollection.getItems().stream()).collect(Collectors.toList());
    }

    public String getTitle() {
        return messageTitle;
    }
}
