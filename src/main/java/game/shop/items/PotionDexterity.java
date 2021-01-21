package game.shop.items;

import game.materials.Material;
import game.squadron.Squadron;

import java.util.Map;

public class PotionDexterity extends ItemPowerUp {
    public PotionDexterity(String name, Map<Material, Integer> materials) {
        super(name, materials);
    }

    @Override
    public float getAdditionalPower(Squadron owner) {
        return 0;
    }
}
