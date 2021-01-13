package game.shop;

import game.items.ItemsGlobalManager;
import game.items.ItemGlobal;

import java.util.stream.Stream;

public class ItemsShop extends AbstractShop {
    public ItemsShop(ItemsGlobalManager itemsCollection) {
        super(itemsCollection, "All items shop");
    }

    @Override
    protected Stream<ItemGlobal> filterItems(Stream<ItemGlobal> stream) {
        return stream;
    }
}
