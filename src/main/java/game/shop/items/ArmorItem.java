package game.shop.items;

import game.ArmorItemPersonal;
import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;

import javax.persistence.*;
import java.util.Map;

@Entity
public class ArmorItem extends AbstractShopItem {

    private float armorValue = 0;

    public ArmorItem(String name, float armorValue, Map<Material, Integer> materials) {
        super(name, materials);
        this.armorValue = armorValue;
    }

    public ArmorItem() {
        super(null, new MaterialsSet());
    }

    @Override
    public void useFor(Player player) {
        player.getArmorItems().add(new ArmorItemPersonal(this));
    }

    public float getArmorValue() {
        return armorValue;
    }

    public void setArmorValue(float armorValue) {
        this.armorValue = armorValue;
    }
}
