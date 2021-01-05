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

        CardStats stats = new CardStats();
        card = new CharacterCard("Riko", "Made in Abyss", stats);
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
}