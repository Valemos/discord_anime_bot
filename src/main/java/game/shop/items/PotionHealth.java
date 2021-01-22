package game.shop.items;

import game.Player;
import game.materials.Material;
import game.squadron.Squadron;

import java.util.Map;

public class PotionHealth extends PlayerActionItem {
    public PotionHealth(Map<Material, Integer> materials) {
        super("Health potion", materials);
    }

    @Override
    public void useFor(Player player) {
        Squadron squadron = player.getSquadron();
        if (squadron != null) squadron.healMembers();
    }
}
