package game.shop.items;

import bot.ShortUUID;
import game.ArmorItemPersonal;
import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;

import javax.persistence.*;
import java.util.Map;

@Entity
public class ArmorItem extends AbstractShopItem {

    @Id
    private String id;
    private String name;
    private float armorValue = 0;

    @Embedded
    private MaterialsSet itemCost = new MaterialsSet();

    public ArmorItem() {
        super(null, null, null);
    }

    public ArmorItem(String name, float armorValue, Map<Material, Integer> materials) {
        this(name, armorValue, new MaterialsSet(materials));
    }

    public ArmorItem(String name, float armorValue, MaterialsSet itemCost) {
        super(null, null, null);
        this.name = name;
        this.armorValue = armorValue;
        this.itemCost = itemCost;
    }

    @Override
    public void useFor(Player player) {
        player.getArmorItems().add(new ArmorItemPersonal(this));
    }

    @PrePersist
    void prePersist() {
        id = ShortUUID.generate();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public float getArmorValue() {
        return armorValue;
    }

    public void setArmorValue(float armorValue) {
        this.armorValue = armorValue;
    }

    @Override
    public MaterialsSet getItemCost() {
        return itemCost;
    }

    @Override
    public void setItemCost(MaterialsSet itemCost) {
        this.itemCost = itemCost;
    }
}
