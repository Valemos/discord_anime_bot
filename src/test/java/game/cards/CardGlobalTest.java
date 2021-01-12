package game.cards;

import bot.BotAnimeCards;
import game.AnimeCardsGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardGlobalTest {

    @Test
    void testCardCreated() {
        CardGlobal card = new CardGlobal("", "", "", new CardStatsGlobal());
        AnimeCardsGame game = new AnimeCardsGame(new BotAnimeCards());
        game.addCard(card);

        assertSame(card, game.getGlobalCardById(card.getCardId()));
    }


}