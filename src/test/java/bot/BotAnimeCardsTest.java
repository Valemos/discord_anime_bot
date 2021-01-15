package bot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BotAnimeCardsTest {

    static MessageSenderMock sender;

    @BeforeAll
    static void setUp() throws Exception {
        sender = new MessageSenderMock();
    }

    @Test
    void testSendRegularMessage() {
        sender.send("#drop", "1");
    }
}