package game.shop.items;

import game.AnimeCardsGame;
import game.Player;
import game.materials.Material;

import java.util.Map;

public class DropRefresher extends AbstractShopItem {
    public DropRefresher(Map<Material, Integer> materials) {
        super("Extra drop", materials);
    }

    @Override
    public void useFor(AnimeCardsGame game, Player player){
        player.getCooldowns().getDrop().reset();
    }

}
