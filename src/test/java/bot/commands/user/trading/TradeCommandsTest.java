package bot.commands.user.trading;

import bot.commands.AbstractCommandTest;
import bot.commands.arguments.RequiredPlayerArguments;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.contract.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeCommandsTest extends AbstractCommandTest<MultiTradeCommand, RequiredPlayerArguments> implements InterfaceMultiTradeTest {

    private ContractsManager spyContracts;

    @BeforeEach
    void setUp() {
        spyContracts = spy(spyGame.getContractsManager());
        doReturn(spyContracts).when(spyGame).getContractsManager();
    }

    @Override
    public AnimeCardsGame game() {
        return spyGame;
    }

    @Nested
    protected class MultiTrade{

        @BeforeEach
        void setUp() {
            setCommand(new MultiTradeCommand(spyGame));
        }

        @Test
        void testCannotTradeWithNotExistingPlayer() {
            arguments.id = "unknownId";
            handleCommand();

            verify(spyContracts, never()).add(any());
        }


        @Test
        void cannotMultiTradeWithYourself() {
            arguments.id = tester.getId();
            handleCommand();

            verify(spyContracts, never()).add(any());
        }


        @Test
        void testMultiTradeContractFound() {
            arguments.id = tester2.getId();
            handleCommand();

            MultiTradeContract contract = getMultiTrade(tester);
            assertNotNull(contract);

            contract = getMultiTrade(tester2);
            assertNotNull(contract);
        }
    }

    @Nested
    protected class SendCards{

        private SendCardsCommand cmdSendCards;
        private SendCardsCommand.Arguments args;

        @BeforeEach
        void setUp() {
            cmdSendCards = new SendCardsCommand(spyGame);
            args = createArguments(cmdSendCards);
        }

        @Test
        void cannotSendCardsToNotExistingPlayer() {
            args.recieverId = "unknownId";
            args.cardIds.add(tester.getCards().get(0).getId());
            args.cardIds.add(tester.getCards().get(1).getId());

            handleCommand(cmdSendCards, tester, args);

            assertNull(getContract(tester, SendCardsContract.class));
        }

        @Test
        void cannotSendCardsToYourself() {
            args.recieverId = tester.getId();
            args.cardIds.add(tester.getCards().get(0).getId());
            args.cardIds.add(tester.getCards().get(1).getId());

            handleCommand(cmdSendCards, tester, args);

            assertNull(getContract(tester, SendCardsContract.class));
        }


        @Test
        void testCannotSendUnknownCards() {
            CardPersonal card1 = tester.getCards().get(0);
            CardPersonal card2 = pickTesterCard(tester2, spyGame.getCardGlobalUnique("riko", null));

            args.recieverId = tester2.getId();
            args.cardIds.add("unknownCardId");
            args.cardIds.add(card1.getId());
            args.cardIds.add(card2.getId());
            handleCommand(cmdSendCards, tester, args);

            SendCardsContract contract = getContract(tester, SendCardsContract.class);

            assertEquals(1, contract.getCards().size());
            assertEquals(card1.getId(), contract.getCards().get(0).getId());
        }

        @Test
        void testCardsSentToPlayer() {
            CardPersonal card1 = tester.getCards().get(0);
            CardPersonal card2 = tester.getCards().get(1);

            args.recieverId = tester2.getId();
            args.cardIds.add(card1.getId());
            args.cardIds.add(card2.getId());

            handleCommand(cmdSendCards, tester, args);

            SendCardsContract contract = getContract(tester, SendCardsContract.class);
            assertNotNull(contract);
            contract.confirm(spyGame, tester.getId());

            assertFalse(tester.getCards().contains(card1));
            assertFalse(tester.getCards().contains(card2));

            assertTrue(tester2.getCards().contains(card1));
            assertTrue(tester2.getCards().contains(card2));
        }

        @Test
        void testCannotSendCardsYouDoNotOwn() {
            CardPersonal card = pickTesterCard(tester2, spyGame.getCardGlobalUnique("riko", null));

            int collectionSize = tester2.getCards().size();


            args.recieverId = tester2.getId();
            args.cardIds.add(card.getId());

            handleCommand(cmdSendCards, tester, args);

            SendCardsContract contract = getContract(tester, SendCardsContract.class);
            assertNull(contract);

            assertEquals(collectionSize, tester2.getCards().size());
        }
    }

    @Nested
    protected class ExchangeCards{

        private TradeCardForCardCommand cmdTradeCards;
        private TradeCardForCardCommand.Arguments args;

        @BeforeEach
        void setUp() {
            cmdTradeCards = new TradeCardForCardCommand(spyGame);
            args = createArguments(cmdTradeCards);
        }

        @Test
        void testCannotTradeCardsWithYourself() {
            args.sendCardId = tester.getCards().get(0).getId();
            args.receiveCardId = tester.getCards().get(1).getId();
            handleCommand(cmdTradeCards, tester, args);

            CardForCardContract contract = getContract(tester, CardForCardContract.class);
            assertNull(contract);
        }

        @Test
        void testExchangeCards() {
            CardPersonal card1 = tester.getCards().get(0);
            CardPersonal card2 = pickTesterCard(tester2, spyGame.getCardGlobalUnique("riko", null));

            assertFalse(tester.getCards().contains(card2));
            assertFalse(tester2.getCards().contains(card1));

            args.sendCardId = card1.getId();
            args.receiveCardId = card2.getId();
            handleCommand(cmdTradeCards, tester, args);

            CardForCardContract contract = getContract(tester, CardForCardContract.class);
            assertNotNull(contract);
            contract.confirm(spyGame, tester.getId());
            contract.confirm(spyGame, tester2.getId());

            assertTrue(tester.getCards().contains(card2));
            assertTrue(tester2.getCards().contains(card1));
        }


        @Test
        void testExchangeAnyCardUnknown() {
            CardPersonal card1 = pickTesterCard(tester2, spyGame.getCardGlobalUnique("riko", null));

            args.sendCardId = tester.getCards().get(0).getId();
            args.receiveCardId = "unknown";
            handleCommand(cmdTradeCards, tester, args);

            args.sendCardId = "unknown";
            args.receiveCardId = card1.getId();
            handleCommand(cmdTradeCards, tester, args);

            CardForCardContract contract = getContract(tester, CardForCardContract.class);
            assertNull(contract);
        }
    }
}