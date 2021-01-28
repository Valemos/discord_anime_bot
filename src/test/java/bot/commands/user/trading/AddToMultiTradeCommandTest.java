package bot.commands.user.trading;

import bot.commands.AbstractCommandTest;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardPersonal;
import game.contract.MultiTradeContract;
import game.materials.Material;
import game.materials.MaterialsSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AddToMultiTradeCommandTest extends AbstractCommandTest<AddToMultiTradeCommand, AddToMultiTradeCommand.Arguments> implements InterfaceMultiTradeTest {

    private String contractMessageId;

    @BeforeEach
    private void setUp() {
        setCommand(new AddToMultiTradeCommand(spyGame));

        // add new multi trade contract before tests
        contractMessageId = "111";
        MultiTradeContract multiTrade = new MultiTradeContract(tester.getId(), tester2.getId());
        spyGame.getContractsManager().add(contractMessageId, multiTrade);
    }

    @Override
    public MultiTradeContract getMultiTrade() {
        return spyGame.getContractsManager().getForMessage(MultiTradeContract.class, contractMessageId);
    }

    @Override
    public Player tester() {
        return tester;
    }

    @Override
    public AnimeCardsGame game() {
        return spyGame;
    }

    @Nested
    public class MaterialsTest{
        @Test
        void testTradeUnknownMaterial() {
            arguments.materialsMap.put("unknown", "100");
            handleCommand(tester, arguments);

            MultiTradeContract contract = getMultiTrade();
            assertTrue(contract.getSenderMaterials().getMap().isEmpty());
        }

        @Test
        void testMultiTradeMaterialAmountNotInteger() {
            arguments.materialsMap.put("gold", "hello");
            handleCommand(arguments);
            assertTrue(getMultiTrade().getSenderMaterials().isEmpty());
            arguments.materialsMap.clear();

            arguments.materialsMap.put("gold", "100.568");
            handleCommand(arguments);
            assertTrue(getMultiTrade().getSenderMaterials().isEmpty());
            arguments.materialsMap.clear();

            arguments.materialsMap.put("gold", "");
            handleCommand(arguments);
            assertTrue(getMultiTrade().getSenderMaterials().isEmpty());
            arguments.materialsMap.clear();

            arguments.materialsMap.put("gold", "10f");
            handleCommand(arguments);
            assertTrue(getMultiTrade().getSenderMaterials().isEmpty());
            arguments.materialsMap.clear();
        }

        @Test
        void testGoldAddedToTrade() {
            tester.getMaterials().setAmount(Material.GOLD, 100);

            arguments.materialsMap.put("gold", "100");
            handleCommand(arguments);

            MultiTradeContract contract = getMultiTrade();
            assertEquals(100, contract.getSenderMaterials().getMap().get(Material.GOLD));
        }

        @Test
        void testPlayerHasInsufficientMaterials() {
            tester.getMaterials().setAmount(Material.GOLD, 100);

            arguments.materialsMap.put("gold", "1000");
            handleCommand(arguments);
            arguments.materialsMap.clear();

            arguments.materialsMap.put("gold", "100000");
            handleCommand(arguments);

            MultiTradeContract contract = getMultiTrade();
            assertEquals(100, contract.getSenderMaterials().getMap().get(Material.GOLD));
        }
    }

    @Nested
    public class CardsTest{
        @Test
        void testNotUserCardsNotAdded() {
            spyGame.pickPersonalCard(tester2.getId(), spyGame.getCardGlobalUnique("riko", null), 1);

            arguments.cardIds.add("unknown");
            arguments.cardIds.add("multi word unknown");
            arguments.cardIds.add("15222345123455");
            arguments.cardIds.add(tester2.getCards().get(0).getId());

            handleCommand(arguments);
            assertTrue(getMultiTrade().getSenderCards().isEmpty());
        }


        @Test
        void testCardsAddedToTrade() {
            CardPersonal card0 = tester.getCards().get(0);
            CardPersonal card1 = tester.getCards().get(1);

            arguments.cardIds.add(card0.getId());
            arguments.cardIds.add(card1.getId());
            handleCommand();

            assertEquals(2, getMultiTrade().getSenderCards().size());
            assertTrue(getMultiTrade().getSenderCards().contains(card0));
            assertTrue(getMultiTrade().getSenderCards().contains(card1));
        }
    }

    @Nested
    public class StocksTest{

        @Test
        void testCannotAddUnknownStocks() {
            arguments.stockValuesMap.put("testUnknown", "1000.2");
            handleCommand(arguments);
            assertTrue(getMultiTrade().getSenderStocks().isEmpty());
        }

        @Test
        void testCannotAddStockYouDoNotOwn() {
            CardPersonal card1 = tester.getCards().get(0);

            spyGame.pickPersonalCard(tester2.getId(),
                    spyGame.getCardsGlobal().getByCharacter(tester.getCards().get(tester.getCards().size() - 1).getCharacterInfo()),
                    1);
            CardPersonal card2 = tester2.getCards().get(0);

            game().exchangeCardForStock(card1);
            game().exchangeCardForStock(card2);

            assertNotEquals(card1.getCharacterInfo(), card2.getCharacterInfo());

            arguments.stockValuesMap.put(card2.getCharacterInfo().getSeries().getName(), "100.5");
            handleCommand(arguments);

            assertTrue(getMultiTrade().getSenderStocks().isEmpty());
        }

        @Test
        void testAddedStockValue() {
            CardPersonal card1 = tester.getCards().get(0);
            game().exchangeCardForStock(card1);

            arguments.stockValuesMap.put(card1.getCharacterInfo().getSeries().getName(), "100.5");
            handleCommand(arguments);

            assertEquals(1, getMultiTrade().getSenderStocks().size());
            assertTrue(getMultiTrade().getSenderStocks().containsKey(card1.getCharacterInfo().getSeries()));
            assertEquals(100.5f, getMultiTrade().getSenderStocks().getOrDefault(card1.getCharacterInfo().getSeries(), 0.f));
        }

        @Test
        void testAddedMaximumStockValue_ifPlayerHasLessThanSpecified() {
            CardPersonal card1 = tester.getCards().get(0);
            float maxValue = game().exchangeCardForStock(card1);

            arguments.stockValuesMap.put(card1.getCharacterInfo().getSeries().getName(), String.valueOf(maxValue + 100));
            handleCommand(arguments);

            arguments.stockValuesMap.put(card1.getCharacterInfo().getSeries().getName(), String.valueOf(maxValue * 2));

            assertEquals(1, getMultiTrade().getSenderStocks().size());
            assertTrue(getMultiTrade().getSenderStocks().containsKey(card1.getCharacterInfo().getSeries()));
            assertEquals(maxValue, getMultiTrade().getSenderStocks().getOrDefault(card1.getCharacterInfo().getSeries(), 0.f));
        }

        @Test
        void testAddStockValueWithMinus() {
            CardPersonal card1 = tester.getCards().get(0);
            spyGame.exchangeCardForStock(card1);

            arguments.stockValuesMap.put(card1.getCharacterInfo().getSeries().getName(), "-100");
            handleCommand(arguments);

            assertEquals(1, getMultiTrade().getSenderStocks().size());
            assertTrue(getMultiTrade().getSenderStocks().containsKey(card1.getCharacterInfo().getSeries()));
            assertEquals(-100, getMultiTrade().getSenderStocks().getOrDefault(card1.getCharacterInfo().getSeries(), 0.f));
        }

    }

    @Nested
    public class ArmorTest{

        @Test
        void testUnknownArmorNotAdded() {
            spyGame.getArmorShop().getItems().get(0).useFor(spyGame, tester);
            spyGame.getArmorShop().getItems().get(1).useFor(spyGame, tester2);

            arguments.armorIds.add("unknown");
            arguments.armorIds.add(tester2.getArmorItems().get(0).getId());
            handleCommand(arguments);

            assertTrue(getMultiTrade().getSenderArmor().isEmpty());
        }

        @Test
        void testArmorAdded() {
            spyGame.getArmorShop().getItems().get(0).useFor(spyGame, tester);

            arguments.armorIds.add(tester.getArmorItems().get(0).getId());
            handleCommand(arguments);

            assertTrue(getMultiTrade().getSenderArmor().contains(tester.getArmorItems().get(0)));
        }
    }
}