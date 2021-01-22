package game.cards;

import bot.BotAnimeCards;
import game.AnimeCardsGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardGlobalTest {

    @Test
    void testCardCreated() {
        CardGlobal card = new CardGlobal("", "", "", new CardStatsGlobal());

        BotAnimeCards bot = new BotAnimeCards();
        bot.loadSettings("hibernate_test.cfg.xml");
        AnimeCardsGame game = bot.getGame();
        game.addCard(card);

        assertSame(card, game.getCardsGlobal().getById(card.getId()));
    }


}