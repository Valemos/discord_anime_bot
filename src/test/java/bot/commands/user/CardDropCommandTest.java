package bot.commands.user;

import bot.BotMessageSenderMock;
import bot.commands.user.carddrop.CardDropCommand;
import bot.menu.MenuEmoji;
import game.Player;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardDropCommandTest {

    static BotMessageSenderMock sender;

    @BeforeAll
    static void setSender() throws Exception {
        sender = new BotMessageSenderMock();
    }

    @BeforeEach
    void setUp() {
        sender.reset();
    }

    private Player tester(BotMessageSenderMock sender) {
        return sender.getGame().getPlayer(sender.tester1.getId());
    }

    @Test
    void testDropCommandHandled() {
        sender.sendAndCaptureMessage("#drop");
        sender.assertCommandHandled(CardDropCommand.class);
    }

    @Test
    void testCardsDropped() {
        assertNull(sender.getGame().getDropManager().getCards("1000"));

        sender.sendAndCaptureMessage("#drop", sender.tester1.getId(), "1000");
        sender.assertCommandHandled(CardDropCommand.class);

        assertNotNull(sender.getGame().getDropManager().getCards("1000"));
    }

    @Test
    void testCardGrabbedByPlayer() {
        List<CardPersonal> prevCollection = List.copyOf(tester(sender).getCards());

        String messageId = "111";
        sender.sendAndCaptureMessage("#drop", tester(sender).getId(), messageId);
        sender.assertCommandHandled(CardDropCommand.class);
        List<CardGlobal> dropped = sender.getGame().getDropManager().getCards(messageId);


        sender.chooseDropMenuReaction(messageId, tester(sender).getId(), MenuEmoji.ONE);
        sender.finishDropTimer();

        List<CardPersonal> newCollection = tester(sender).getCards();

        assertNotEquals(newCollection, prevCollection);

        assertEquals(
                dropped.get(0).getCharacterInfo(),
                newCollection.get(newCollection.size() - 1).getCharacterInfo()
        );
    }

    @Test
    void testCooldown() {

        sender.sendAndCaptureMessage("#drop", tester(sender).getId(), "11");
        assertNotNull(sender.getGame().getDropManager().getCards("11"));

        sender.sendAndCaptureMessage("#drop", tester(sender).getId(), "15");
        assertNull(sender.getGame().getDropManager().getCards("15"));

        tester(sender).getCooldowns().getDrop().setUsed(null);

        sender.sendAndCaptureMessage("#drop", tester(sender).getId(), "12");
        assertNotNull(sender.getGame().getDropManager().getCards("12"));
    }

    @Test
    void tryGrabMultipleCharacters() {
        int prevSize = tester(sender).getCards().size();

        String messageId = "111";
        sender.sendAndCaptureMessage("#drop", tester(sender).getId(), messageId);
        List<CardGlobal> dropped = sender.getGame().getDropManager().getCards(messageId);

        sender.chooseDropMenuReaction(messageId, tester(sender).getId(), MenuEmoji.ONE);
        tester(sender).getCooldowns().reset();
        sender.chooseDropMenuReaction(messageId, tester(sender).getId(), MenuEmoji.ONE);
        tester(sender).getCooldowns().reset();
        sender.chooseDropMenuReaction(messageId, tester(sender).getId(), MenuEmoji.TWO);

        sender.finishDropTimer();


        List<CardPersonal> collection = tester(sender).getCards();
        CardPersonal card1 = collection.get(collection.size() - 1);
        assertEquals(1, collection.size() - prevSize);

        assertEquals(
                dropped.get(0).getCharacterInfo(),
                card1.getCharacterInfo()
        );

    }
}