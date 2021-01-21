package game.shop.items;

import game.Player;
import game.materials.Material;

import java.util.Map;

public class PotionHealth extends AbstractShopItem {
    public PotionHealth(String name, Map<Material, Integer> materials) {
        super(name, materials);
    }

    @Override
    public void useFor(Player player) {
        player.getSquadron().healMembers();
    }
}
