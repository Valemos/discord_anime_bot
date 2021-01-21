package game.shop;

import game.shop.items.ArmorItem;

import java.util.List;
import java.util.stream.Stream;

public class ArmorShop extends AbstractShop {
    public ArmorShop(List<ArmorItem> armorItems) {
        super("Armor shop", armorItems);
    }
}
