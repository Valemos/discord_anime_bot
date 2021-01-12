package game;

import bot.BotAnimeCards;
import bot.CommandPermissions;
import game.cards.CardStatsGlobal;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimeCardsGameTest {
    AnimeCardsGame game;
    Player player1;
    Player player2;
    CardGlobal card1;
    CardGlobal card2;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame(new BotAnimeCards());
        player1 = new Player("1", CommandPermissions.USER);
        player2 = new Player("2", CommandPermissions.USER);

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
        assertSame(card1, game.getGlobalCardById(card1.getCardId()));
    }

    @Test
    void testCardRemoved() {
        game.addCard(card1);
        game.removeCardById(card1.getCardId());

        assertNull(game.getGlobalCardById(card1.getCardId()));
    }

    @Test
    void testTwoCardsAdded() {
        game.addCard(card1);
        game.addCard(card2);

        assertEquals(2, game.getCollection().size());
    }

    @Test
    void testCardsUniqueId() {
        game.addCard(card1);
        game.addCard(card2);

        assertNotEquals(card1.getCardId(), card2.getCardId());
    }
}