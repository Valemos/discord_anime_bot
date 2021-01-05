package game;

import bot.AccessLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimeCardsGameTest {
    AnimeCardsGame game;
    Player player;
    CharacterCard card1;
    CharacterCard card2;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        player = new Player("1", AccessLevel.USER);

        CardStats stats = new CardStats(0, 5, 0, CharismaState.CHARISMATIC, Constitution.HEALTHY);
        card1 = new CharacterCard("Riko", "Made in Abyss", "img", stats);
        card2 = new CharacterCard("Test", "Test", "img", stats.clone());
    }

    @Test
    void testNoUserFound() {
        assertNull(game.getPlayerById("1"));
    }

    @Test
    void testPlayerFound() {
        game.addPlayer(player);
        assertEquals(player, game.getPlayerById(player.getId()));
    }

    @Test
    void testCardAdded() {
        game.addCard(card1);
        assertSame(card1, game.getCardById(card1.getId()));
    }

    @Test
    void testCardRemoved() {
        game.addCard(card1);
        game.removeCardById(card1.getId());

        assertNull(game.getCardById(card1.getId()));
    }

    @Test
    void testCardStatsCopied() {
        assertEquals(card1.stats, card1.stats.clone());
        assertNotSame(card1.stats, card1.stats.clone());
    }

    @Test
    void testTwoCardsAdded() {
        game.addCard(card1);
        game.addCard(card2);

        assertEquals(2, game.getGlobalCollection().size());
    }

    @Test
    void testCardsUniqueId() {
        game.addCard(card1);
        game.addCard(card2);

        assertNotEquals(card1.getId(), card2.getId());
    }
}