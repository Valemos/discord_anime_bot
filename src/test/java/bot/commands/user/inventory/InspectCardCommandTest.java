package bot.commands.user.inventory;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InspectCardCommandTest extends MessageSenderTester {


    @Test
    void testInspectNotCorrectCommand() {
        sender.send("#i");
        sender.assertCommandNotHandled();
    }

    @Test
    void testInspectCardSingleWord() {
        sender.send("#i riko");
        sender.assertCommandHandled(InspectCardCommand.class);

        InspectCardCommand cmd = (InspectCardCommand) sender.findSpyCommand(InspectCardCommand.class);
        assertEquals("riko", cmd.getArgumentsObject().getCardName());
        assertNull(cmd.getArgumentsObject().getSeriesName());
    }

    @Test
    void testInspectCardMultipleWords() {
        sender.send("#i haruhi suzum");
        sender.assertCommandHandled(InspectCardCommand.class);

        InspectCardCommand cmd = (InspectCardCommand) sender.findSpyCommand(InspectCardCommand.class);
        assertEquals("haruhi suzum", cmd.getArgumentsObject().getCardName());
        assertNull(cmd.getArgumentsObject().getSeriesName());
    }

    @Test
    void testInspectCardSeriesMultipleWords() {
        sender.send("#i haruhi suzum -s \"haruhi no\"");
        sender.assertCommandHandled(InspectCardCommand.class);

        InspectCardCommand cmd = (InspectCardCommand) sender.findSpyCommand(InspectCardCommand.class);
        assertEquals("haruhi suzum", cmd.getArgumentsObject().getCardName());
        assertEquals("haruhi no", cmd.getArgumentsObject().getSeriesName());
    }

    @Test
    void testFindGlobalCards() {
        CardGlobal card1 = sender.getGame().getCardGlobalUnique("riko", null);
        assertNotNull(card1);

        assertEquals(sender.cardGlobal1, card1);

        card1 = sender.getGame().getCardGlobalUnique("haruhi suzum", null);
        CardGlobal card2 = sender.getGame().getCardGlobalUnique("haruhi suzum", "haruhi no");

        assertEquals(card1, card2);

        assertNull(sender.getGame().getCardGlobalUnique("unknown card", null));
        assertNull(sender.getGame().getCardGlobalUnique("unknown card", ""));
        assertNull(sender.getGame().getCardGlobalUnique("", "unknown series"));
    }
}