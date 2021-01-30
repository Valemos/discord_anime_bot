package game.shop.items;

import game.AnimeCardsGame;
import game.Player;
import game.materials.Material;

import java.util.Map;

public class GrabRefresher extends AbstractShopItem implements UsablePowerUp{
    public GrabRefresher(Map<Material, Integer> materials) {
        super("Extra grab", materials);
    }

    @Override
    public void useFor(AnimeCardsGame game, Player player) {
        player.getCooldowns().getGrab().reset();
    }
}
