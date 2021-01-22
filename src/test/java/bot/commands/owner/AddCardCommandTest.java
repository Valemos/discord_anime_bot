package bot.commands.owner;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddCardCommandTest  extends MessageSenderTester {

    @BeforeEach
    void setUp() {
        sender.reset();
    }

    @Test
    void incorrectCommandArguments() {
        sender.send("#addcard");
        sender.assertCommandNotHandled();
    }

    @Test
    void testCreateCardCommand() {
        assertNull(sender.getGame().getCardGlobalUnique("card name", "card series"));

        sender.send("#addcard \"card name\" \"card series\" image_url");
        sender.assertCommandHandled(AddCardCommand.class);

        CardGlobal c = sender.getGame().getCardGlobalUnique("card name", "card series");
        assertNotNull(c);
        assertEquals("card name", c.getCharacterInfo().getName());
        assertEquals("card series", c.getCharacterInfo().getSeriesName());
        assertEquals("image_url", c.getCharacterInfo().getImageUrl());
    }

    @Test
    void testCreateWithInsufficientParameters() {
        assertNull(sender.getGame().getCardGlobalUnique("card name", "card series"));

        sender.send("#addcard \"card name\"");
        sender.assertCommandNotHandled();

        assertNull(sender.getGame().getCardGlobalUnique("card name", null));
        assertNull(sender.getGame().getCardGlobalUnique("card name", "card series"));
    }
}