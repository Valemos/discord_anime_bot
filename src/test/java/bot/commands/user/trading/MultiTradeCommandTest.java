package bot.commands.user.trading;

import bot.commands.user.shop.MessageSenderTester;
import game.contract.CardForCardContract;
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
    void testCardsSentToPlayer() {
        sendAndCapture("#sendcards " +
                sender.tester2.getId() + " " +
                getTesterCard(0).getId() + " " +
                getTesterCard(1).getId(),
                sender.tester1.getId(),
                "111");
        ContractInterface contract = spyContracts.get(SendCardsContract.class, "111");
        assertNotNull(contract);
//        sender.chooseMenuReaction(contract.);
    }
}