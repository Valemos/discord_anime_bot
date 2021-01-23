package bot.commands.user.stocks;

import bot.commands.user.shop.MessageSenderTester;
import game.AnimeCardsGame;
import game.cards.CardPersonal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeForStockCommandTest extends MessageSenderTester {

    @BeforeEach
    void setUp() {

    }

    @Test
    void testUnknownCardNotExchanged() {
        send("#exchange 1234567890");
        verify(game(), never()).exchangeCardForStock(any());
    }

    @Test
    void testCardsExchanged() {
        assertEquals(0, tester().getStocks().size());

        CardPersonal testerCard1 = getTesterCard(0);
        CardPersonal testerCard2 = getTesterCard(1);
        send("#exchange " + testerCard1.getId() + " " + testerCard2.getId());

        assertFalse(tester().getCards().contains(testerCard1));
        assertFalse(tester().getCards().contains(testerCard2));
    }

    @Test
    void testCardsFromSameSeries_contributeToOneStockValue() {
        CardPersonal card1 = getTesterCard(0);
        CardPersonal card2 = getTesterCard(1);

        assertEquals(card1.getCharacterInfo().getSeries(),
                    card2.getCharacterInfo().getSeries());
        send("#exchange " + card1.getId() + " " + card2.getId());

        assertEquals(1, tester().getStocks().size());
        assertEquals(card1.getCharacterInfo().getSeries(),
                    tester().getStocks().get(0).getSeries());
    }
}