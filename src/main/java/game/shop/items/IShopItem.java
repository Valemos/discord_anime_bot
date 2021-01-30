package game.shop.items;

import game.AnimeCardsGame;
import game.Player;
import game.materials.MaterialsSet;

public interface IShopItem {
    void buyFor(AnimeCardsGame game, Player player);

    String getName();
    String getId();
    void setId(String newId);
    MaterialsSet getItemCost();
}
