package bot.commands;

import bot.BotAnimeCards;
import bot.MessageEventMock;
import bot.commands.owner.AddCardCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.AnimeCardsGame;
import game.Player;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class AbstractCommandTest<T extends AbstractCommand<A>, A> {

    private static MessageEventMock messageEventMock;

    protected T command;

    @Mock
    private static EventWaiter mEventWaiter;
    @Mock
    private MessageAction mMessageAction;
    @Mock
    private RestAction<Void> mVoidRestAction;
    @Mock
    private MessageChannel mMessageChannel;
    @Mock
    private User mUser;

    protected static BotAnimeCards spyBot;
    protected static AnimeCardsGame spyGame;
    protected Player tester;

    @BeforeAll
    static void beforeAll() {
        spyBot = new BotAnimeCards(mEventWaiter);
        spyBot.loadSettings("hibernate_test.cfg.xml");
        spyGame = spy(spyBot.getGame());
        messageEventMock = new MessageEventMock();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        spyBot.loadTestGameSettings(spyGame);
        tester = spyGame.getOrCreatePlayer("409754559775375371");
    }

    protected void setCommand(T commandObject) {
        command = commandObject;
    }

    protected A createArguments() {
        return command.createArgumentsInstance(command.argumentsClass);
    }

    protected void handleCommand(Player player, A arguments) {
        command.commandArgs = arguments;
        CommandEvent cevent = new CommandEvent(messageEventMock.getMock(), null, null);
        command.handle(cevent);
    }
}
