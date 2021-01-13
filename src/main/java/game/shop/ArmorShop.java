package game.shop;

import game.items.ItemsGlobalManager;
import game.items.ItemGlobal;

import java.util.stream.Stream;

public class ArmorShop extends AbstractShop {
    public ArmorShop(ItemsGlobalManager itemsCollection) {
        super(itemsCollection, "Armor shop");
    }

    @Override
    protected Stream<ItemGlobal> filterItems(Stream<ItemGlobal> stream) {
        return stream.filter(ArmorShop::isArmorItem);
    }

    private static boolean isArmorItem(ItemGlobal item) {
        return item.getDefense() > 0;
    }
}
