package game.shop.items;

import game.Player;
import game.materials.MaterialsSet;

public abstract class AbstractShopItem {
    protected String id;
    protected String name;
    protected MaterialsSet itemCost;

    public AbstractShopItem(String id, String name, MaterialsSet itemCost) {
        this.id = id;
        this.name = name;
        this.itemCost = itemCost;
    }

    public abstract void useFor(Player player);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MaterialsSet getItemCost() {
        return itemCost;
    }

    public void setItemCost(MaterialsSet itemCost) {
        this.itemCost = itemCost;
    }

    public String getFullDescription() {
        return getName() + ": " + getItemCost().getDescriptionMultiline();
    }
}
