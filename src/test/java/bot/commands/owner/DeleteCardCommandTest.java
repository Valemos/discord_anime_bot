package bot.commands.owner;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCardCommandTest extends MessageSenderTester {

    @Test
    void testCardNotDeletedForIncorrectCommand() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getAllCards());

        sender.send("#delcard");

        assertEquals(cards, sender.getGame().getCardsGlobal().getAllCards());
    }

    @Test
    void testCardNameNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getAllCards());

        sender.send("#delcard unknown");

        assertEquals(cards, sender.getGame().getCardsGlobal().getAllCards());
    }


    @Test
    void testCardSeriesNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getAllCards());

        sender.send("#delcard -s \"unknown series\"");

        assertEquals(cards, sender.getGame().getCardsGlobal().getAllCards());
    }

    @Test
    void testCardNameAndSeriesNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getAllCards());

        sender.send("#delcard unknown -s \"unknown series\"");

        assertEquals(cards, sender.getGame().getCardsGlobal().getAllCards());
    }

    @Test
    void testCardIdNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getAllCards());

        sender.send("#delcard -id 123456");

        assertEquals(cards, sender.getGame().getCardsGlobal().getAllCards());
    }

    @Test
    void testNotDeletedWhenNameNotUnique() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getAllCards());

        sender.send("#delcard r");

        assertEquals(cards, sender.getGame().getCardsGlobal().getAllCards());
    }

    @Test
    void testDeletedWhenNameUnique() {
        List<CardGlobal> cards = sender.getGame().getCardsGlobal().getAllCards();
        assertTrue(cards.contains(sender.cardGlobal1));

        sender.send("#delcard riko");

        cards = sender.getGame().getCardsGlobal().getAllCards();
        assertFalse(cards.contains(sender.cardGlobal1));
    }

    @Test
    void testDeleteById() {
        List<CardGlobal> cards = sender.getGame().getCardsGlobal().getAllCards();
        assertTrue(cards.contains(sender.cardGlobal1));

        sender.send("#delcard -id " + sender.cardGlobal1.getId());

        cards = sender.getGame().getCardsGlobal().getAllCards();
        assertFalse(cards.contains(sender.cardGlobal1));
    }
}