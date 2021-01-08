package game.cards;

import game.AnimeCardsGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardGlobalTest {

    @Test
    void testCardCreated() {
        CharacterCardGlobal card = new CharacterCardGlobal("", "", "", new CardStatsGlobal());
        AnimeCardsGame game = new AnimeCardsGame();
        game.addCard(card);

        assertSame(card, game.getGlobalCardById(card.getCardId()));
    }


}