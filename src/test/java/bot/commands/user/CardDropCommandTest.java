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
        Player tester = sender.tester1;
        List<CardPersonal> prevCollection = tester.getCards();

        sender.sendAndCaptureMessage("#drop", tester.getId(), "111");
        sender.assertCommandHandled(CardDropCommand.class);
        List<CardGlobal> dropped = sender.getGame().getDropManager().getCards("111");


        sender.chooseDropMenuReaction("111", tester.getId(), MenuEmoji.ONE);
        List<CardPersonal> newCollection = tester.getCards();

        assertNotEquals(newCollection, prevCollection);

        assertEquals(
                dropped.get(0).getCharacterInfo(),
                newCollection.get(newCollection.size() - 1).getCharacterInfo()
        );
    }

    @Test
    void testCooldown() {
        Player tester = sender.tester1;
        sender.sendAndCaptureMessage("#drop", tester.getId(), "11");
        assertNotNull(sender.getGame().getDropManager().getCards("11"));

        sender.sendAndCaptureMessage("#drop", tester.getId(), "15");
        assertNull(sender.getGame().getDropManager().getCards("15"));

        sender.getGame().getCooldowns(tester.getId()).getDrop().setUsed(null);

        sender.sendAndCaptureMessage("#drop", tester.getId(), "12");
        assertNotNull(sender.getGame().getDropManager().getCards("12"));
    }

    @Test
    void grabTheSameCharacter() {

        Player tester = sender.tester1;
        int prevSize = tester.getCards().size();

        sender.sendAndCaptureMessage("#drop", tester.getId(), "111");
        List<CardGlobal> dropped = sender.getGame().getDropManager().getCards("111");

        sender.chooseDropMenuReaction("111", tester.getId(), MenuEmoji.ONE);
        List<CardPersonal> collection1 = tester.getCards();
        CardPersonal card1 = collection1.get(collection1.size() - 1);
        assertEquals(1, collection1.size() - prevSize);

        assertEquals(
                dropped.get(0).getCharacterInfo(),
                card1.getCharacterInfo()
        );

        sender.getGame().getCooldowns(tester.getId()).reset();

        sender.chooseDropMenuReaction("111", tester.getId(), MenuEmoji.ONE);
        List<CardPersonal> collection2 = tester.getCards();
        CardPersonal card2 = collection2.get(collection2.size() - 1);
        assertEquals(1, collection2.size() - collection1.size());

        assertEquals(
                dropped.get(0).getCharacterInfo(),
                collection2.get(collection2.size() - 2).getCharacterInfo()
        );

        assertEquals(
                dropped.get(0).getCharacterInfo(),
                card2.getCharacterInfo()
        );
    }
}