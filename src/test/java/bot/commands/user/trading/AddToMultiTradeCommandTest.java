package bot.commands.user.trading;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardPersonal;
import game.contract.MultiTradeContract;
import game.materials.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddToMultiTradeCommandTest extends MessageSenderTester implements InterfaceMultiTradeTest {

    @BeforeEach
    private void setUp() {
        sendAndCapture("#multitrade " + tester2().getId());
        game().pickPersonalCard(tester2().getId(), sender.cardGlobal1, 5);
    }

    @Test
    void testTradeUnknownMaterial() {
        send("#addtrade -m unknown=100");

        MultiTradeContract contract = getMultiTrade();

        assertTrue(contract.getSenderMaterials().getMaterials().isEmpty());
    }

    @Test
    void testGoldAddedToTrade() {
        MultiTradeContract contract = getMultiTrade();

        send("#addtrade -m gold=100");
        assertEquals(100, contract.getSenderMaterials().getMaterials().get(Material.GOLD));
    }

    @Test
    void testMultiTradeMaterialAmountNotInteger() {
        
        send("#addtrade -m gold=hello");
        assertTrue(getMultiTrade().getSenderMaterials().isEmpty());

        send("#addtrade -m gold=\"multi words\"");
        assertTrue(getMultiTrade().getSenderMaterials().isEmpty());

        send("#addtrade -m gold=100.568");
        assertTrue(getMultiTrade().getSenderMaterials().isEmpty());

        send("#addtrade -m gold=100,568");
        assertTrue(getMultiTrade().getSenderMaterials().isEmpty());

        send("#addtrade -m gold= -m hello= 20");
        assertTrue(getMultiTrade().getSenderMaterials().isEmpty());
    }

    @Test
    void testNotUserCardsNotAdded() {
        send("#addtrade -c unknown -c \"multi word unknown\"");
        assertTrue(getMultiTrade().getSenderCards().isEmpty());

        send("#addtrade -c "+ tester2().getCards().get(0).getId());
        assertTrue(getMultiTrade().getSenderCards().isEmpty());

        send("#addtrade -c 15222345123455");
        assertTrue(getMultiTrade().getSenderCards().isEmpty());
    }

    @Test
    void testCardsAddedToTrade() {
        send("#addtrade -c " + getTesterCard(0).getId() + " " + getTesterCard(1));
        assertEquals(2, getMultiTrade().getSenderCards().size());
        assertTrue(getMultiTrade().getSenderCards().contains(getTesterCard(0)));
        assertTrue(getMultiTrade().getSenderCards().contains(getTesterCard(1)));
    }

    @Test
    void testCannotAddUnknownStocks() {
        send("#addtrade -s testUnknown");
        assertTrue(getMultiTrade().getSenderStocks().isEmpty());

        CardPersonal card1 = getTesterCard(0);
        game().exchangeCardForStock(card1);

        CardPersonal card2 = tester2().getCards().get(0);
        game().exchangeCardForStock(card2);

        send("#addtrade -s \"" + card1.getCharacterInfo().getSeries().getName() + '"');
        assertTrue(getMultiTrade().getSenderStocks().isEmpty());
    }

    @Test
    void testAddedStockValue() {
        CardPersonal card1 = getTesterCard(0);
        game().exchangeCardForStock(card1);

        send("#addtrade -s \"" + card1.getCharacterInfo().getSeries().getName() + "\"=100.5");

        assertEquals(1, getMultiTrade().getSenderStocks().size());
        assertTrue(getMultiTrade().getSenderStocks().containsKey(card1.getCharacterInfo().getSeries()));
        assertEquals(100.5f, getMultiTrade().getSenderStocks().getOrDefault(card1.getCharacterInfo().getSeries(), 0.f));

    }

    @Test
    void testAddedMaximumStockValue_ifPlayerHasLessThanSpecified() {
        CardPersonal card1 = getTesterCard(0);
        float maxValue = game().exchangeCardForStock(card1);

        send("#addtrade -s \"" + card1.getCharacterInfo().getSeries().getName() + "\"=" + (maxValue + 100));

        assertEquals(1, getMultiTrade().getSenderStocks().size());
        assertTrue(getMultiTrade().getSenderStocks().containsKey(card1.getCharacterInfo().getSeries()));
        assertEquals(maxValue, getMultiTrade().getSenderStocks().getOrDefault(card1.getCharacterInfo().getSeries(), 0.f));
    }
}