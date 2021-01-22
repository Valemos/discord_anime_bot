package bot.commands.user.shop;

import bot.BotMessageSenderMock;
import game.Player;
import org.junit.jupiter.api.BeforeAll;

import javax.security.auth.login.LoginException;

public class MessageSenderTester {
    protected static BotMessageSenderMock sender;

    @BeforeAll
    static void beforeAll() throws LoginException {
        sender = new BotMessageSenderMock();
    }

    protected Player tester() {
        return sender.tester();
    }
}
