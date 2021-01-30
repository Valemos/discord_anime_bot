package game.shop;

import game.AnimeCardsGame;
import game.shop.items.AbstractShopItem;
import game.Player;
import game.materials.MaterialsSet;
import game.shop.items.IShopItem;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractShop<T extends IShopItem> {

    protected final List<? extends T> items;
    private final String messageTitle;

    public AbstractShop(String messageTitle, List<? extends T> items) {
        this.items = items;
        this.messageTitle = messageTitle;
    }

    public <C extends IShopItem> boolean tryBuyItem(AnimeCardsGame game, Player player, @Nonnull C item) {

        MaterialsSet playerMaterials = player.getMaterials();
        MaterialsSet itemCost = item.getItemCost();

        if (playerMaterials.containsEnough(itemCost)){
            player.getMaterials().subtractMaterials(itemCost);
            item.buyFor(game, player);
            return true;
        }
        return false;
    }

    public T findShopItem(String itemId) {
        T item = getItemById(itemId);
        if (item == null)
            item = getUniqueByName(itemId);

        return item;
    }

    public <C extends T> T findShopItem(Class<C> itemClass) {
        return items.stream()
                .filter(item -> item.getClass().isAssignableFrom(itemClass))
                .findFirst().orElse(null);
    }

    private T getUniqueByName(String itemName) {
        List<? extends T> itemsFound = items.stream()
                .filter(i -> String.valueOf(i.getName()).toLowerCase().contains(itemName.toLowerCase()))
                .collect(Collectors.toList());
        return itemsFound.size() == 1 ? itemsFound.get(0) : null;
    }

    private T getItemById(String itemId) {
        return items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst().orElse(null);
    }

    public List<? extends T> getItems() {
        return items;
    }

    public String getTitle() {
        return messageTitle;
    }

    @Nonnull
    public String getMessageCostIsHigh(T item, Player player) {
        return "cannot buy " + item.getName()
                + "\nyou have:\n"
                + player.getMaterials().getDescriptionMultiline()
                + "\nItem costs:\n"
                + item.getItemCost().getDescriptionMultiline();
    }
}
