package game.shop.items;

import game.AnimeCardsGame;
import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;
import org.hibernate.Session;

import java.util.Map;

public abstract class AbstractShopItem implements IShopItem {
    protected String id;
    protected String name;
    protected MaterialsSet itemCost;

    public AbstractShopItem(String name, Map<Material, Integer> materials) {
        this(null, name, new MaterialsSet(materials));
    }

    public AbstractShopItem(String id, String name, MaterialsSet itemCost) {
        this.id = id;
        this.name = name;
        this.itemCost = itemCost;
    }

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
