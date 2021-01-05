package game;

import bot.AccessLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimeCardsGameTest {
    AnimeCardsGame game;
    Player player;
    CharacterCard card;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        player = new Player("1", AccessLevel.USER);

        CardStats stats = new CardStats(0, 5, 0, CharismaState.CHARISMATIC, Constitution.HEALTHY);
        card = new CharacterCard("Riko", "Made in Abyss", "img", stats);
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
        game.addCard(card);
        assertSame(card, game.getCardById(card.getId()));
    }

    @Test
    void testCardRemoved() {
        game.addCard(card);
        game.removeCardById(card.getId());

        assertNull(game.getCardById(card.getId()));
    }

    @Test
    void testCardStatsCopied() {
        assertEquals(card.stats, card.stats.clone());
        assertNotSame(card.stats, card.stats.clone());
    }
}