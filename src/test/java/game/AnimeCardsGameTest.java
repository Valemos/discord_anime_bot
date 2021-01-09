package game;

import bot.AccessLevel;
import game.cards.CardStatsGlobal;
import game.cards.CharacterCardGlobal;
import game.cards.CharacterCardPersonal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimeCardsGameTest {
    AnimeCardsGame game;
    Player player1;
    Player player2;
    CharacterCardGlobal card1;
    CharacterCardGlobal card2;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        player1 = new Player("1", AccessLevel.USER);
        player2 = new Player("2", AccessLevel.USER);

        CardStatsGlobal stats = new CardStatsGlobal();
        card1 = new CharacterCardGlobal("Riko", "Made in Abyss", "img", stats);
        card2 = new CharacterCardGlobal("Test", "Test", "img", stats);
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

        assertEquals(2, game.getGlobalCollection().size());
    }

    @Test
    void testCardsUniqueId() {
        game.addCard(card1);
        game.addCard(card2);

        assertNotEquals(card1.getCardId(), card2.getCardId());
    }
}