package bot.commands.user.trading;

import bot.commands.user.shop.MessageSenderTester;
import game.Player;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.contract.ContractInterface;
import game.contract.ContractsManager;
import game.contract.SendCardsContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MultiTradeCommandTest extends MessageSenderTester {

    private ContractsManager spyContracts;

    @BeforeEach
    void setUp() {
        spyContracts = spy(game().getContractsManager());
        doReturn(spyContracts).when(game()).getContractsManager();
    }

    private CardPersonal pickCardForTester2(CardGlobal card) {
        return game().pickPersonalCard(tester2().getId(), card, 5);
    }

    @Test
    void testCannotTradeWithNotExistingPlayer() {
        send("#multitrade unknownId");
        verify(spyContracts, never()).add(any(), any(), any());
    }

    @Test
    void cannotSendCardsToNotExistingPlayer() {
        send("#sendcards unknownId " + getTesterCard(0).getId() + " " + getTesterCard(1).getId());
        verify(spyContracts, never()).add(any(), any(), any());
    }

    @Test
    void testCannotExchangeCardsWithUnknownPlayer() {
        send("#tradecards unknownId " + getTesterCard(0).getId() + " " + getTesterCard(1).getId());
        verify(spyContracts, never()).add(any(), any(), any());
    }

    @Test
    void testCannotTradeWithYourself() {
        send("#tradecards " + tester().getId());
        send("#sendcards " + tester().getId());
        send("#multitrade " + tester().getId());

        verify(spyContracts, never()).add(any(), any(), any());
    }

    @Test
    void testCannotSendUnknownCards() {
        CardPersonal card1 = getTesterCard(0);

        send("#sendcards " + tester2().getId() + " unknownCardId " + card1.getId() + " cardId",
                sender.tester1.getId(),
                "111");
        verify(spyContracts, never()).add(any(), any(), any());

        assertFalse(tester().getCards().contains(card1));
        assertTrue(tester2().getCards().contains(card1));
    }

    @Test
    void testCardsSentToPlayer() {
        CardPersonal card1 = getTesterCard(0);
        CardPersonal card2 = getTesterCard(1);

        sendAndCapture("#sendcards " + tester2().getId() + " " + card1.getId() + " " +card2.getId(),
                                sender.tester1.getId(),
                                "111");

        ContractInterface contract = spyContracts.get(SendCardsContract.class, "111");
        assertNotNull(contract);
        contract.finish(game());

        assertFalse(tester().getCards().contains(card1));
        assertFalse(tester().getCards().contains(card2));

        assertTrue(tester2().getCards().contains(card1));
        assertTrue(tester2().getCards().contains(card2));
    }

    @Test
    void testCannotSendCardsYouDoNotOwn() {
        CardPersonal card = pickCardForTester2(sender.cardGlobal1);

        int collectionSize = tester2().getCards().size();

        send("#sendcards " + tester2().getId() + " " + card.getId(), sender.tester1.getId(), "111");
        verify(spyContracts, never()).add(any(), any(), any());

        int newSize = tester2().getCards().size();
        assertEquals(collectionSize, newSize);
    }

    @Test
    void testExchangeCards() {
        CardPersonal card1 = getTesterCard(0);
        CardPersonal card2 = pickCardForTester2(sender.cardGlobal1);

        assertFalse(tester().getCards().contains(card2));
        assertFalse(tester2().getCards().contains(card1));

        sendAndCapture("#tradecards " + tester2().getId() + " " + card1.getId() + " " +card2.getId(),
                sender.tester1.getId(),
                "111");

        ContractInterface contract = spyContracts.get(SendCardsContract.class, "111");
        assertNotNull(contract);
        contract.finish(game());

        assertTrue(tester().getCards().contains(card2));
        assertTrue(tester2().getCards().contains(card1));
    }

    @Test
    void testExchangeAnyCardUnknown() {
        CardPersonal card1 = getTesterCard(0);

        sendAndCapture("#tradecards " + tester2().getId() + " " + card1.getId() + " unknownId",
                sender.tester1.getId(),
                "111");
        verify(spyContracts, never()).add(any(), any(), any());
    }
}