package game.shop.items;

import game.materials.Material;
import game.materials.MaterialsSet;

import java.util.Map;

public abstract class PlayerActionItem extends AbstractShopItem {
    public PlayerActionItem(String name, Map<Material, Integer> materials) {
        super(null, name, new MaterialsSet(materials));
    }
}
