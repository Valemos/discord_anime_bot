package game.shop.items;

import game.materials.Material;
import game.squadron.PowerUpType;
import game.squadron.Squadron;

import javax.persistence.Entity;
import java.util.Map;

public class PotionWisdom extends ShopPowerUp {

    public PotionWisdom(Map<Material, Integer> materials) {
        super(PowerUpType.WISDOM, materials);
    }
}
