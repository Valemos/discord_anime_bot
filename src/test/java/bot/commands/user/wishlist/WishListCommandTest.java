package bot.commands.user.wishlist;

import bot.commands.user.shop.MessageSenderTester;
import game.Player;
import game.cards.CardGlobal;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class WishListCommandTest extends MessageSenderTester {

    @AfterEach
    void tearDown() {
        Session s = sender.getGame().getDatabaseSession();
        s.beginTransaction();
        s.persist(tester());
        tester().getWishList().clear();
        s.getTransaction().commit();
    }

    @Test
    void testerHasEmptyWishList() {
        assertTrue(tester().getWishList().isEmpty());
    }

    @Test
    void testUnknownCardWishById() {
        send("#wishid 1234567890");
        verify(game(), never()).addToWishlist(any(), any());
    }

    @Test
    void testUnknownCardName() {
        send("#wish unknown guy multi word");
        verify(game(), never()).addToWishlist(any(), any());
    }

    @Test
    void testWishAddedByName() {
        send("#wish " + sender.cardGlobal1.getName().toLowerCase());

        assertEquals(1, tester().getWishList().size());
        assertTrue(tester().getWishList().contains(sender.cardGlobal1));
    }

    @Test
    void testWishCardAddedTwice_butWishListUnchanged() {
        send("#wish " + sender.cardGlobal1.getName().toLowerCase());
        assertEquals(1, tester().getWishList().size());

        send("#wish " + sender.cardGlobal1.getName().toLowerCase());
        assertEquals(1, tester().getWishList().size());
    }

    @Test
    void testAddedToWishListMultipleIds() {
        assertFalse(tester().getWishList().contains(sender.cardsGlobal.get(0)));
        assertFalse(tester().getWishList().contains(sender.cardsGlobal.get(1)));

        send("#wishid 1234567890 " + sender.cardsGlobal.get(0).getId() + " " + sender.cardsGlobal.get(1).getId());
        assertEquals(2, tester().getWishList().size());

        assertTrue(tester().getWishList().contains(sender.cardsGlobal.get(0)));
        assertTrue(tester().getWishList().contains(sender.cardsGlobal.get(1)));
    }

    @Test
    void testRemoveUnknownWish() {
        send("#wish " + sender.cardGlobal1.getName());
        assertTrue(tester().getWishList().contains(sender.cardGlobal1));

        send("#wishremove unknown");
        verify(game(), never()).removeFromWishlist(any(Player.class), anyString());
        verify(game(), never()).removeFromWishlist(any(Player.class), any(CardGlobal.class));
        assertTrue(tester().getWishList().contains(sender.cardGlobal1));
    }

    @Test
    void testRemoveUnknownWishId() {
        send("#wish " + sender.cardGlobal1.getName());
        assertTrue(tester().getWishList().contains(sender.cardGlobal1));

        send("#wishremoveid 1234567890");
        assertTrue(tester().getWishList().contains(sender.cardGlobal1));
    }

    @Test
    void testCannotRemoveOtherPlayerWish() {
        sender.setTesterDefault(sender.tester2);
        assertFalse(tester().getWishList().contains(sender.cardGlobal1));
        send("#wish " + sender.cardGlobal1.getName());
        assertTrue(tester().getWishList().contains(sender.cardGlobal1));

        sender.setTesterDefault(sender.tester1);
        send("#wishremoveid " + sender.cardGlobal1.getId());

        sender.setTesterDefault(sender.tester2);
        assertTrue(tester().getWishList().contains(sender.cardGlobal1));
    }
}