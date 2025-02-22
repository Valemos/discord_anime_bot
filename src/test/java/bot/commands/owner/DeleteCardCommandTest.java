package bot.commands.owner;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardGlobal;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCardCommandTest extends MessageSenderTester {

    @Test
    void testCardNotDeletedForIncorrectCommand() {
        List<CardGlobal> cards = new ArrayList<>(game().getCardsGlobal().getAllCards());

        send("#delcard");

        assertEquals(cards, game().getCardsGlobal().getAllCards());
    }

    @Test
    void testCardNameNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(game().getCardsGlobal().getAllCards());

        send("#delcard unknown");

        assertEquals(cards, game().getCardsGlobal().getAllCards());
    }


    @Test
    void testCardSeriesNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(game().getCardsGlobal().getAllCards());

        send("#delcard -s \"unknown series\"");

        assertEquals(cards, game().getCardsGlobal().getAllCards());
    }

    @Test
    void testCardNameAndSeriesNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(game().getCardsGlobal().getAllCards());

        send("#delcard unknown -s \"unknown series\"");

        assertEquals(cards, game().getCardsGlobal().getAllCards());
    }

    @Test
    void testCardIdNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(game().getCardsGlobal().getAllCards());

        send("#delcard -id 123456");

        assertEquals(cards, game().getCardsGlobal().getAllCards());
    }

    @Test
    void testNotDeletedWhenNameNotUnique() {
        List<CardGlobal> cards = new ArrayList<>(game().getCardsGlobal().getAllCards());

        send("#delcard r");

        assertEquals(cards, game().getCardsGlobal().getAllCards());
    }

    @Test
    void testDeletedWhenNameUnique() {
        List<CardGlobal> cards = game().getCardsGlobal().getAllCards();
        assertTrue(cards.contains(sender.cardGlobal1));

        send("#delcard riko");

        cards = game().getCardsGlobal().getAllCards();
        assertFalse(cards.contains(sender.cardGlobal1));
    }

    @Test
    void testDeleteById() {
        List<CardGlobal> cards = game().getCardsGlobal().getAllCards();
        assertTrue(cards.contains(sender.cardGlobal1));

        send("#delcard -id " + sender.cardGlobal1.getId());

        cards = game().getCardsGlobal().getAllCards();
        assertFalse(cards.contains(sender.cardGlobal1));
    }
}