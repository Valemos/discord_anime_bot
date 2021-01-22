package bot.commands.user.shop;

import bot.BotMessageSenderMock;
import game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.security.auth.login.LoginException;

public class MessageSenderTester {
    protected static BotMessageSenderMock sender;

    @BeforeAll
    static void initializeSender() throws LoginException {
        sender = new BotMessageSenderMock();
    }

    @BeforeEach
    void resetSender() {
        sender.reset();
    }

    protected Player tester() {
        return sender.tester();
    }
}
