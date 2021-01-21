package game.cards;

import bot.BotAnimeCards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardsGlobalManagerTest {

    BotAnimeCards bot;

    @BeforeEach
    void setUp() {
        bot = new BotAnimeCards();
        bot.loadSettings("hibernate_test.cfg.xml");
    }

    @Test
    void testEqualCardsNotDuplicated() {
        CardsGlobalManager cg = bot.getGame().getCardsGlobal();

        CardGlobal card = new CardGlobal("test name", "test series", "url");
        cg.addCard(card);

        List<CardGlobal> allCards = cg.getAllCards();
        int size = allCards.size();
        assertEquals(card, allCards.get(size - 1));

        CardGlobal card2 = new CardGlobal("test name", "test series", "url");
        cg.addCard(card2);

        assertEquals(size, cg.getAllCards().size());
    }

    @Test
    void testSeriesReused() {
        CardsGlobalManager cg = bot.getGame().getCardsGlobal();

        CardGlobal card1 = new CardGlobal("test name", "test series", "url");
        CardGlobal card2 = new CardGlobal("testo namo", "test series", "url");

        cg.addCard(card1);
        assertTrue(cg.getAllCards().contains(card1));

        cg.addCard(card2);
        assertTrue(cg.getAllCards().contains(card2));

        assertEquals(1, cg.getAllSeries().size());
    }
}