package game.shop.items;

import game.Player;
import game.materials.Material;

import java.util.Map;

public class DropRefresher extends AbstractShopItem {
    public DropRefresher(String name, Map<Material, Integer> materials) {
        super(name, materials);
    }

    @Override
    public void useFor(Player player){
        player.getCooldowns().getDrop().reset();
    }
}
