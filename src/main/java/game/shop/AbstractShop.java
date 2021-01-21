package game.shop;

import game.shop.items.AbstractShopItem;
import game.Player;
import game.materials.MaterialsSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractShop {

    protected final List<? extends AbstractShopItem> items;
    AbstractShopItem itemBuying;
    private final String messageTitle;

    public AbstractShop(String messageTitle, List<? extends AbstractShopItem> items) {
        this.items = items;
        this.messageTitle = messageTitle;
    }

    public boolean tryBuyItem(Player player, @NotNull AbstractShopItem item) {

        MaterialsSet playerMaterials = player.getMaterials();
        MaterialsSet itemCost = item.getItemCost();

        if (playerMaterials.containsEnough(itemCost)){
            player.getMaterials().subtractMaterials(itemCost);
            item.useFor(player);
            return true;
        }
        return false;
    }

    public AbstractShopItem findShopItem(String itemId) {
        AbstractShopItem item = getItemById(itemId);
        if (item == null)
            item = getUniqueByName(itemId);

        return item;
    }

    private AbstractShopItem getUniqueByName(String itemName) {
        List<? extends AbstractShopItem> items = this.items.stream()
                .filter(i -> String.valueOf(i.getName()).toLowerCase().contains(itemName.toLowerCase()))
                .collect(Collectors.toList());
        return items.size() == 1 ? items.get(0) : null;
    }

    private AbstractShopItem getItemById(String itemId) {
        return items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst().orElse(null);
    }

    public AbstractShopItem getItemBuying() {
        return itemBuying;
    }

    public List<? extends AbstractShopItem> getItems() {
        return items;
    }

    public String getTitle() {
        return messageTitle;
    }
}
