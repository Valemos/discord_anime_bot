package game.shop.items;

import game.materials.Material;
import game.squadron.Squadron;

import java.util.Map;

public class PotionWisdom extends ItemPowerUp {
    public PotionWisdom(String name, Map<Material, Integer> materials) {
        super(name, materials);
    }

    @Override
    public float getAdditionalPower(Squadron owner) {
        return 0;
    }

}
