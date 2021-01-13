package game.wishlist;

import game.MappedObjectManager;
import game.cards.CardGlobal;

public class WishListsManager extends MappedObjectManager<String, WishList> {

    public WishListsManager() {
        super(WishList.class);
    }

    public void addCard(String playerId, CardGlobal card) {
        WishList wishList = getElement(playerId);
        wishList.add(card);
    }

    public boolean removeCard(String playerId, CardGlobal card) {
        WishList wishList = getElement(playerId);
        return wishList.removeById(card.getId());
    }
}
