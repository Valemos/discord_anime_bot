package game.shop.items;

import game.Player;
import game.materials.Material;

import java.util.Map;

public class GrabRefresher extends PlayerActionItem {
    public GrabRefresher(Map<Material, Integer> materials) {
        super("Extra grab", materials);
    }

    @Override
    public void useFor(Player player) {
        player.getCooldowns().getGrab().reset();
    }
}
