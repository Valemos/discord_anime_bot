package game.shop.items;

import game.Player;
import game.materials.Material;

import java.util.Map;

public class DropRefresher extends PlayerActionItem {
    public DropRefresher(Map<Material, Integer> materials) {
        super("Extra drop", materials);
    }

    @Override
    public void useFor(Player player){
        player.getCooldowns().getDrop().reset();
    }

}
