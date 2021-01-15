package game;

import bot.BotAnimeCards;
import game.cards.CardStatsGlobal;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimeCardsGameTest {
    BotAnimeCards bot;
    AnimeCardsGame game;
    Player player1;
    Player player2;
    CardGlobal card1;
    CardGlobal card2;

    @BeforeEach
    void setUp() {
        bot = new BotAnimeCards();
        game = bot.getGame();
        player1 = game.createNewPlayer("1");
        player2 = game.createNewPlayer("2");

        CardStatsGlobal stats = new CardStatsGlobal();
        card1 = new CardGlobal("Riko", "Made in Abyss", "img", stats);
        card2 = new CardGlobal("Test", "Test", "img", stats);
    }

    @Test
    void testNoUserFound() {
        assertNull(game.getPlayerById("1"));
    }

    @Test
    void testPlayerFound() {
        game.addPlayer(player1);
        assertEquals(player1, game.getPlayerById(player1.getId()));
    }

    @Test
    void testCardAdded() {
        game.addCard(card1);
        assertSame(card1, game.getCardGlobalById(card1.getId()));
    }

    @Test
    void testCardRemoved() {
        game.addCard(card1);
        game.removeCardById(card1.getId());

        assertNull(game.getCardGlobalById(card1.getId()));
    }

    @Test
    void testTwoCardsAdded() {
        game.addCard(card1);
        game.addCard(card2);

        assertEquals(2, game.getCardsGlobalManager().size());
    }

    @Test
    void testCardsUniqueId() {
        game.addCard(card1);
        game.addCard(card2);

        assertNotEquals(card1.getId(), card2.getId());
    }
}