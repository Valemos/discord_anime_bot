import bot.BotAnimeCards;
import org.mockito.Mockito;

public class MessageSenderMock {

    private final BotAnimeCards spyBot;

    public MessageSenderMock() {
        spyBot = Mockito.spy(new BotAnimeCards());

    }
}
