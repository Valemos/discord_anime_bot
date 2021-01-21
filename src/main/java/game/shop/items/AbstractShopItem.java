package game.shop.items;

import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Map;

@Entity
public abstract class AbstractShopItem {

    @Id
    protected String id;
    protected String name;
    protected String description;
    protected MaterialsSet itemCost = new MaterialsSet();

    public AbstractShopItem() {
    }

    public AbstractShopItem(String name, Map<Material, Integer> materials) {
        this(name, null, materials);
    }

    public AbstractShopItem(String name, MaterialsSet materialsSet) {
        this(name, null, materialsSet);
    }

    public AbstractShopItem(String name, String description, Map<Material, Integer> materials) {
        this(name, description, new MaterialsSet(materials));
    }

    public AbstractShopItem(String name, String description, MaterialsSet itemCost) {
        this.name = name;
        this.description = description;
        this.itemCost = itemCost;
    }

    public abstract void useFor(Player player);

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullDescription() {
        return null;
    }
}
