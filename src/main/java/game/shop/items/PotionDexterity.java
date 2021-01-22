package game.shop.items;

import game.materials.Material;
import game.squadron.PowerUpType;

import java.util.Map;

public class PotionDexterity extends ShopPowerUp {

    public PotionDexterity(Map<Material, Integer> materials) {
        super(PowerUpType.DEXTERITY, materials);
    }
}
