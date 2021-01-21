package game.shop.items;

import game.materials.Material;
import game.squadron.Squadron;

import java.util.Map;

public class PotionStrength extends ItemPowerUp {
    public PotionStrength(String name, Map<Material, Integer> materials) {
        super(name, materials);
    }

    @Override
    public float getAdditionalPower(Squadron owner) {
        return 0;
    }

}
