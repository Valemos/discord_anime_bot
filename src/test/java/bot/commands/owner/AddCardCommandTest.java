package bot.commands.owner;

import bot.BotMessageSenderMock;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddCardCommandTest {


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
    void incorrectCommandArguments() {
        sender.send("#addcard");
        sender.assertCommandNotHandled();
    }

    @Test
    void testCreateCardCommand() {
        assertNull(sender.getGame().getCardGlobal("card name", "card series"));

        sender.send("#addcard \"card name\" \"card series\" image_url");
        sender.assertCommandHandled(AddCardCommand.class);

        CardGlobal c = sender.getGame().getCardGlobal("card name", "card series");
        assertNotNull(c);
        assertEquals("card name", c.getCharacterInfo().getCharacterName());
        assertEquals("card series", c.getCharacterInfo().getSeriesTitle());
        assertEquals("image_url", c.getCharacterInfo().getImageUrl());
    }

    @Test
    void testCreateWithInsufficientParameters() {
        assertNull(sender.getGame().getCardGlobal("card name", "card series"));

        sender.send("#addcard \"card name\"");
        sender.assertCommandNotHandled();

        assertNull(sender.getGame().getCardGlobal("card name", null));
        assertNull(sender.getGame().getCardGlobal("card name", "card series"));
    }
}