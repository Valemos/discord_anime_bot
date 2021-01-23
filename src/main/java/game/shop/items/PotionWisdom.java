package game.shop.items;

import game.materials.Material;
import game.player_objects.squadron.PowerUpType;

import java.util.Map;

public class PotionWisdom extends ShopPowerUp {

    public PotionWisdom(Map<Material, Integer> materials) {
        super(PowerUpType.WISDOM, materials);
    }
}
