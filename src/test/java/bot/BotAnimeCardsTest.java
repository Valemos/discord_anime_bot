package bot;

import bot.commands.user.DropCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BotAnimeCardsTest {

    static MessageSenderMock sender;

    @BeforeAll
    static void setSender() throws Exception {
        sender = new MessageSenderMock();
    }

    @BeforeEach
    void setUp() {
        sender.resetMocks();
    }

    @Test
    void testSendNotACommand() {
        sender.send("not a command");
        sender.assertCommandNotHandled();
    }

    @Test
    void testSendRegularCommand() {
        sender.send("#drop");
        sender.assertCommandHandled(DropCommand.class);
    }


}