package game.shop.items;

import game.Player;
import game.materials.Material;

import java.util.Map;

public class GrabRefresher extends AbstractShopItem {
    public GrabRefresher(String name, Map<Material, Integer> materials) {
        super(name, materials);
    }

    @Override
    public void useFor(Player player) {
        player.getCooldowns().getGrab().reset();
    }
}
