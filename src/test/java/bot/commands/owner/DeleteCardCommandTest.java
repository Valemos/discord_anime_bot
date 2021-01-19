package bot.commands.owner;

import bot.BotMessageSenderMock;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCardCommandTest {

    static BotMessageSenderMock sender;

    @BeforeAll
    static void setSender() throws Exception {
        sender = new BotMessageSenderMock();
    }

    @BeforeEach
    void setUp() {
        sender.reset();
    }

    @Test
    void testCardNotDeletedForIncorrectCommand() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getFilteredCards());

        sender.send("#delcard");

        assertEquals(cards, sender.getGame().getCardsGlobal().getFilteredCards());
    }

    @Test
    void testCardNameNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getFilteredCards());

        sender.send("#delcard unknown");

        assertEquals(cards, sender.getGame().getCardsGlobal().getFilteredCards());
    }


    @Test
    void testCardSeriesNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getFilteredCards());

        sender.send("#delcard -s \"unknown series\"");

        assertEquals(cards, sender.getGame().getCardsGlobal().getFilteredCards());
    }

    @Test
    void testCardNameAndSeriesNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getFilteredCards());

        sender.send("#delcard unknown -s \"unknown series\"");

        assertEquals(cards, sender.getGame().getCardsGlobal().getFilteredCards());
    }

    @Test
    void testCardIdNotFoundAndNotDeleted() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getFilteredCards());

        sender.send("#delcard -id 123456");

        assertEquals(cards, sender.getGame().getCardsGlobal().getFilteredCards());
    }

    @Test
    void testNotDeletedWhenNameNotUnique() {
        List<CardGlobal> cards = new ArrayList<>(sender.getGame().getCardsGlobal().getFilteredCards());

        sender.send("#delcard r");

        assertEquals(cards, sender.getGame().getCardsGlobal().getFilteredCards());
    }

    @Test
    void testDeletedWhenNameUnique() {
        List<CardGlobal> cards = sender.getGame().getCardsGlobal().getFilteredCards();
        assertTrue(cards.contains(sender.cardGlobal1));

        sender.send("#delcard riko");

        cards = sender.getGame().getCardsGlobal().getFilteredCards();
        assertFalse(cards.contains(sender.cardGlobal1));
    }

    @Test
    void testDeleteById() {
        List<CardGlobal> cards = sender.getGame().getCardsGlobal().getFilteredCards();
        assertTrue(cards.contains(sender.cardGlobal1));

        sender.send("#delcard -id " + sender.cardGlobal1.getId());

        cards = sender.getGame().getCardsGlobal().getFilteredCards();
        assertFalse(cards.contains(sender.cardGlobal1));
    }
}