package bot.commands.user.inventory;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InspectCardCommandTest extends MessageSenderTester {


    @Test
    void testInspectNotCorrectCommand() {
        send("#i");
        sender.assertCommandNotHandled();
    }

    @Test
    void testInspectCardSingleWord() {
        send("#i riko");
        sender.assertCommandHandled(InspectCardCommand.class);

        InspectCardCommand cmd = (InspectCardCommand) sender.findSpyCommand(InspectCardCommand.class);
        assertEquals("riko", cmd.getArgumentsObject().getCardName());
        assertNull(cmd.getArgumentsObject().seriesName);
    }

    @Test
    void testInspectCardMultipleWords() {
        send("#i haruhi suzum");
        sender.assertCommandHandled(InspectCardCommand.class);

        InspectCardCommand cmd = (InspectCardCommand) sender.findSpyCommand(InspectCardCommand.class);
        assertEquals("haruhi suzum", cmd.getArgumentsObject().getCardName());
        assertNull(cmd.getArgumentsObject().seriesName);
    }

    @Test
    void testInspectCardSeriesMultipleWords() {
        send("#i haruhi suzum -s \"haruhi no\"");
        sender.assertCommandHandled(InspectCardCommand.class);

        InspectCardCommand cmd = (InspectCardCommand) sender.findSpyCommand(InspectCardCommand.class);
        assertEquals("haruhi suzum", cmd.getArgumentsObject().getCardName());
        assertEquals("haruhi no", cmd.getArgumentsObject().seriesName);
    }

    @Test
    void testFindGlobalCards() {
        CardGlobal card1 = game().getCardGlobalUnique("riko", null);
        assertNotNull(card1);

        assertEquals(sender.cardGlobal1, card1);

        card1 = game().getCardGlobalUnique("haruhi suzum", null);
        CardGlobal card2 = game().getCardGlobalUnique("haruhi suzum", "haruhi no");

        assertEquals(card1, card2);

        assertNull(game().getCardGlobalUnique("unknown card", null));
        assertNull(game().getCardGlobalUnique("unknown card", ""));
        assertNull(game().getCardGlobalUnique("", "unknown series"));
    }
}