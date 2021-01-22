package bot.commands.user;

import bot.commands.user.carddrop.CardDropCommand;
import bot.commands.user.shop.MessageSenderTester;
import bot.menu.MenuEmoji;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardDropCommandTest extends MessageSenderTester {


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
        List<CardPersonal> prevCollection = List.copyOf(tester().getCards());

        String messageId = "111";
        sender.sendAndCaptureMessage("#drop", tester().getId(), messageId);
        sender.assertCommandHandled(CardDropCommand.class);
        List<CardGlobal> dropped = sender.getGame().getDropManager().getCards(messageId);


        sender.chooseDropMenuReaction(messageId, tester().getId(), MenuEmoji.ONE);
        sender.finishDropTimer();

        List<CardPersonal> newCollection = tester().getCards();

        assertNotEquals(newCollection, prevCollection);

        assertEquals(
                dropped.get(0).getCharacterInfo(),
                newCollection.get(newCollection.size() - 1).getCharacterInfo()
        );
    }

    @Test
    void testCooldown() {

        sender.sendAndCaptureMessage("#drop", tester().getId(), "11");
        assertNotNull(sender.getGame().getDropManager().getCards("11"));

        sender.sendAndCaptureMessage("#drop", tester().getId(), "15");
        assertNull(sender.getGame().getDropManager().getCards("15"));

        tester().getCooldowns().getDrop().setUsed(null);

        sender.sendAndCaptureMessage("#drop", tester().getId(), "12");
        assertNotNull(sender.getGame().getDropManager().getCards("12"));
    }

    @Test
    void tryGrabMultipleCharacters() {
        int prevSize = tester().getCards().size();

        String messageId = "111";
        sender.sendAndCaptureMessage("#drop", tester().getId(), messageId);
        List<CardGlobal> dropped = sender.getGame().getDropManager().getCards(messageId);

        sender.chooseDropMenuReaction(messageId, tester().getId(), MenuEmoji.ONE);
        tester().getCooldowns().reset();
        sender.chooseDropMenuReaction(messageId, tester().getId(), MenuEmoji.ONE);

        tester().getCooldowns().reset();
        sender.chooseDropMenuReaction(messageId, tester().getId(), MenuEmoji.TWO);

        sender.finishDropTimer();


        List<CardPersonal> collection = tester().getCards();

        // must receive first character and second character one copy each
        assertEquals(2, collection.size() - prevSize);

        CardPersonal card1 = collection.get(collection.size() - 1);
        CardPersonal card2 = collection.get(collection.size() - 2);

        assertTrue(dropped.get(0).getCharacterInfo().equals(card1.getCharacterInfo()) ||
                            dropped.get(1).getCharacterInfo().equals(card1.getCharacterInfo()));

        assertTrue(dropped.get(0).getCharacterInfo().equals(card2.getCharacterInfo()) ||
                            dropped.get(1).getCharacterInfo().equals(card2.getCharacterInfo()));
    }
}