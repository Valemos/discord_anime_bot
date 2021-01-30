package game.shop.items;

import game.AnimeCardsGame;
import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;
import game.player_objects.squadron.PowerUpType;

import java.util.Map;

public abstract class ShopPowerUp extends AbstractShopItem implements UsablePowerUp {

    protected PowerUpType powerUpType;

    public ShopPowerUp(PowerUpType powerUpType, Map<Material, Integer> materials) {
        super(null, powerUpType.getName(), new MaterialsSet(materials));
        this.powerUpType = powerUpType;
    }

    @Override
    public void useFor(AnimeCardsGame game, Player player) {
        if (player.getSquadron() != null){
            player.getSquadron().addPowerUp(powerUpType);
        }
    }

    public PowerUpType getType() {
        return powerUpType;
    }
}
