package game.wishlist;

import game.MappedObjectManager;
import game.cards.CardGlobal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class WishListsManager extends MappedObjectManager<String, WishList> {

    public WishListsManager(Session dbSession) {
        super(WishList.class);
    }

    public void addCard(String playerId, CardGlobal card) {
        WishList wishList = getElementOrCreate(playerId);
        wishList.add(card);
    }

    public boolean removeCard(String playerId, CardGlobal card) {
        WishList wishList = getElementOrCreate(playerId);
        return wishList.removeById(card.getId());
    }
}
