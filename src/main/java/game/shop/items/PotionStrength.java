package game.shop.items;

import game.materials.Material;
import game.squadron.PowerUpType;

import java.util.Map;

public class PotionStrength extends ShopPowerUp {

    public PotionStrength(Map<Material, Integer> materials) {
        super(PowerUpType.STRENGTH, materials);
    }
}
