package game.shop.items;

import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;
import game.squadron.PowerUpType;
import game.squadron.Squadron;

import java.util.Map;

public abstract class ShopPowerUp extends AbstractShopItem {

    protected PowerUpType powerUpType;

    public ShopPowerUp(PowerUpType powerUpType, Map<Material, Integer> materials) {
        super(null, powerUpType.getName(), new MaterialsSet(materials));
        this.powerUpType = powerUpType;
    }

    @Override
    public void useFor(Player player) {
        if (player.getSquadron() != null){
            player.getSquadron().getPowerUps().add(powerUpType);
        }
    }

}
