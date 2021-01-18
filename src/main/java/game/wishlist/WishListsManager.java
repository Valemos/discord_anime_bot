package game.wishlist;

import game.MappedObjectManager;
import game.cards.CardGlobal;

public class WishListsManager extends MappedObjectManager<String, WishList> {

    public WishListsManager() {
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
