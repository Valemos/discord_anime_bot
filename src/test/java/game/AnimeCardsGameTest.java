package game;

import bot.AccessLevel;
import game.cards.CardStats;
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

        CardStats stats = new CardStats();
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
    void testCardStatsCopied() {
        assertEquals(card1.getStats(), card1.getStats().clone());
        assertNotSame(card1.getStats(), card1.getStats().clone());
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

    @Test
    void testPersonalCardCreatedFromGlobalCard() {
        CharacterCardPersonal card = card1.getConstantCopy(player1.getId());
        game.addCard(card1);

        assertEquals(card.getStats(), card1.getStats().getConstantCopy());
        assertNotSame(card.getStats(), card1.getStats().getConstantCopy());

        CharacterCardPersonal copiedCard = game.getPersonalCard(player1, card1.getCardId());
        assertEquals(card, copiedCard);
        assertNotSame(card, copiedCard);
    }


}