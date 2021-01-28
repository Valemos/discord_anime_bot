package bot.commands.user.trading;

import bot.commands.AbstractCommandTest;
import bot.commands.arguments.RequiredPlayerArguments;
import bot.commands.user.shop.MessageSenderTester;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.contract.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MultiTradeCommandTest extends AbstractCommandTest<MultiTradeCommand, RequiredPlayerArguments> implements InterfaceMultiTradeTest {

    private ContractsManager spyContracts;

    @BeforeEach
    void setUp() {
        spyContracts = spy(game().getContractsManager());
        doReturn(spyContracts).when(game()).getContractsManager();

    }

    private CardPersonal pickCardForTester2(CardGlobal card) {
        return spyGame.pickPersonalCard(tester2.getId(), card, 5);
    }

    @Test
    void testCannotTradeWithNotExistingPlayer() {
        send("#multitrade unknownId");
        sender.assertMessageQueueWithoutConsumer();
    }

    @Test
    void cannotSendCardsToNotExistingPlayer() {
        send("#sendcards unknownId " + getTesterCard(0).getId() + " " + getTesterCard(1).getId());
        sender.assertMessageQueueWithoutConsumer();
    }

    @Test
    void testCannotExchangeCardsWithUnknownPlayer() {
        send("#tradecards unknownId " + getTesterCard(0).getId() + " " + getTesterCard(1).getId());
        sender.assertMessageQueueWithoutConsumer();
    }

    @Test
    void testCannotTradeCardsWithYourself() {
        send("#tradecards " + tester().getId());
        sender.assertMessageQueueWithoutConsumer();
    }

    @Test
    void cannotSendCardsToYourself() {
        send("#sendcards " + tester().getId());
        sender.assertMessageQueueWithoutConsumer();
    }

    @Test
    void cannotMultiTradeWithYourself() {
        send("#multitrade " + tester().getId());
        sender.assertMessageQueueWithoutConsumer();
    }

    @Test
    void testCannotSendUnknownCards() {
        CardPersonal card1 = getTesterCard(0);

        sendAndCapture("#sendcards " + tester2.getId() + " unknownCardId " + card1.getId() + " cardId",
                sender.tester1.getId(),
                "111");

        SendCardsContract contract = getContract(tester(), SendCardsContract.class);

        assertEquals(1, contract.getCards().size());
        assertEquals(card1.getId(), contract.getCards().get(0).getId());

        assertTrue(tester().getCards().contains(card1));
        assertFalse(tester2.getCards().contains(card1));
    }

    @Test
    void testCardsSentToPlayer() {
        CardPersonal card1 = getTesterCard(0);
        CardPersonal card2 = getTesterCard(1);

        sendAndCapture("#sendcards " + tester2.getId() + " " + card1.getId() + " " +card2.getId(),
                                sender.tester1.getId(),
                                "111");

        SendCardsContract contract = spyContracts.getForMessage(SendCardsContract.class, "111");
        assertNotNull(contract);
        contract.confirm(game(), sender.tester1.getId());

        assertFalse(tester().getCards().contains(card1));
        assertFalse(tester().getCards().contains(card2));

        assertTrue(tester2.getCards().contains(card1));
        assertTrue(tester2.getCards().contains(card2));
    }

    @Test
    void testCannotSendCardsYouDoNotOwn() {
        CardPersonal card = pickCardForTester2(sender.cardGlobal1);

        int collectionSize = tester2.getCards().size();

        send("#sendcards " + tester2.getId() + " " + card.getId(), sender.tester1.getId(), "111");
        sender.assertMessageQueueWithoutConsumer();

        int newSize = tester2.getCards().size();
        assertEquals(collectionSize, newSize);
    }

    @Test
    void testExchangeCards() {
        CardPersonal card1 = getTesterCard(0);
        CardPersonal card2 = pickCardForTester2(sender.cardGlobal1);

        assertFalse(tester().getCards().contains(card2));
        assertFalse(tester2.getCards().contains(card1));

        sendAndCapture("#tradecards " + " " + card1.getId() + " " +card2.getId(),
                sender.tester1.getId(),
                "111");

        CardForCardContract contract = spyContracts.getForMessage(CardForCardContract.class, "111");
        assertNotNull(contract);
        contract.confirm(game(), tester().getId());
        contract.confirm(game(), tester2.getId());

        assertTrue(tester().getCards().contains(card2));
        assertTrue(tester2.getCards().contains(card1));
    }

    @Test
    void testExchangeAnyCardUnknown() {
        CardPersonal card1 = getTesterCard(0);

        send("#tradecards " + card1.getId() + " unknownId",
                sender.tester1.getId(),
                "111");

        send("#tradecards " + " unknownId" + card1.getId(),
                sender.tester1.getId(),
                "111");

        verify(spyContracts, never()).add(any(), any());
    }

    @Test
    void testMultiTradeContractFound() {
        sendAndCapture("#multitrade " + tester2.getId());

        MultiTradeContract contract = getMultiTrade();
        assertNotNull(contract);

        contract = getContract(tester2, MultiTradeContract.class);
        assertNotNull(contract);
    }

    @Override
    public Player tester() {
        return null;
    }

    @Override
    public AnimeCardsGame game() {
        return null;
    }
}