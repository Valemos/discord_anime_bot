package game.shop.items;

import game.AnimeCardsGame;
import game.Player;
import org.hibernate.Session;

public interface UsablePowerUp extends IShopItem {
    void useFor(AnimeCardsGame game, Player player);
    default void buyFor(AnimeCardsGame game, Player player){
        Session session = game.getDatabaseSession();
        session.beginTransaction();

        player.addPowerUp(getClass());
        session.merge(player);

        session.getTransaction().commit();
    }
}
