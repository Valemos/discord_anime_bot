package game.shop;

import game.materials.Material;
import game.shop.items.*;

import java.util.List;
import java.util.Map;

public class PowerUpsShop extends AbstractShop<UsablePowerUp> {
    public PowerUpsShop() {
        super("Power ups shop", List.of(
                new DropRefresher(  Map.of(Material.GOLD,   500)),
                new GrabRefresher(  Map.of(Material.GOLD,   300)),
                new PotionHealth(   Map.of(Material.DIAMOND,20)),
                new PotionStrength( Map.of(Material.GOLD,   100)),
                new PotionDexterity(Map.of(Material.GOLD,   100)),
                new PotionWisdom(   Map.of(Material.GOLD,   100))
        ));

        assignIndices(items);
    }

    private void assignIndices(List<? extends UsablePowerUp> items) {
        int index = 1;
        for (UsablePowerUp item : items){
            item.setId(String.valueOf(index++));
        }
    }
}
