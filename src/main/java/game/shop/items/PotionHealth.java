package game.shop.items;

import game.AnimeCardsGame;
import game.Player;
import game.materials.Material;
import game.player_objects.squadron.Squadron;

import java.util.Map;

public class PotionHealth extends AbstractShopItem implements UsablePowerUp{
    public PotionHealth(Map<Material, Integer> materials) {
        super("Health potion", materials);
    }

    @Override
    public void useFor(AnimeCardsGame game, Player player) {
        Squadron squadron = player.getSquadron();
        if (squadron != null) squadron.healMembers();
    }
}
