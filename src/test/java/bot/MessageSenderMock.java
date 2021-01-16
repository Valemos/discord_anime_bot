package bot;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.impl.CommandClientImpl;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import okhttp3.OkHttpClient;
import org.mockito.*;

import javax.security.auth.login.LoginException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class MessageSenderMock {

    private final String testPlayerId;

    @Mock
    private MessageReceivedEvent mMessageEvent;
    @Mock
    private CommandEvent mCommandEvent;
    @Mock
    private Message mMessage;
    @Mock
    private User mUser;
    @Mock
    private SelfUser mSelfUser;
    @Mock
    private OkHttpClient httpClient;
    @Mock
    private EventWaiter mEventWaiter;
    @Mock
    private TextChannel mTextChannel;
    @Mock
    private MessageChannel mMessageChannel;
    @Mock
    private MessageAction mMessageAction;
    @Mock
    private JDA mJDA;
    @Spy
    CommandClientBuilder spyCommandClientBuilder;
    CommandClientImpl spyCommandClient;
    @Spy
    private final BotAnimeCards spyBot = new BotAnimeCards();

    private AbstractCommand<?>[] spyCommands;


    public MessageSenderMock() throws LoginException {
        MockitoAnnotations.initMocks(this);

        spyOnCommands();

        doReturn(mJDA).when(spyBot).buildJDA(any());

        spyBot.authenticate("");
        spyBot.loadDefaultSettings();

        testPlayerId = spyBot.getGame().getPlayerById("1").getId();

        initMessageEventMock();
    }

    private void initMessageEventMock() {
        when(mMessageEvent.getJDA()).thenReturn(mJDA);

        when(mMessageEvent.getMessage()).thenReturn(mMessage);
        when(mMessageEvent.getAuthor()).thenReturn(mUser);
        when(mTextChannel.canTalk()).thenReturn(true);

        when(mMessageEvent.getChannelType()).thenReturn(ChannelType.TEXT);
        when(mMessageEvent.getTextChannel()).thenReturn(mTextChannel);
        when(mMessageEvent.getChannel()).thenReturn(mMessageChannel);
        when(mMessageChannel.sendMessage(anyString())).thenReturn(mMessageAction);
    }

    private void spyOnCommands() {
        AbstractCommand<?>[] commands = spyBot.getCommands(spyBot.getGame());
        spyCommands = Arrays.stream(commands)
                .map(Mockito::spy)
                .toArray(AbstractCommand<?>[]::new);
        when(spyBot.getCommands(any())).thenReturn(spyCommands);

        spyCommandClient = Mockito.spy((CommandClientImpl) spyBot.buildCommandClient());
        doReturn(spyCommandClient).when(spyBot).buildCommandClient();
    }

    public void resetMocks() {
        reset(spyCommands);
        reset(spyCommandClient, mMessageEvent, mUser, mMessageChannel, mTextChannel, mMessageAction);
        initMessageEventMock();
    }

    public void send(String message) {
        send(message, testPlayerId);
    }

    public void send(String message, String userId) {
        when(mMessage.getContentRaw()).thenReturn(message);
        when(mUser.isBot()).thenReturn(false);
        when(mUser.getId()).thenReturn(userId);

        spyCommandClient.onEvent(mMessageEvent);
    }

    private AbstractCommand<?> findSpyCommand(Class<? extends AbstractCommand<?>> commandClass) {
        return Arrays.stream(spyCommands)
                .filter(cmd -> ! (cmd.getClass().equals(commandClass)))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public void assertCommandHandled(Class<? extends AbstractCommand<?>> commandClass){
        AbstractCommand<?> spyCommand = findSpyCommand(commandClass);
        verify(spyCommand).handle(any());
    }

    public void assertCommandNotHandled() {
        for (AbstractCommand<?> cmd : spyCommands){
            verify(cmd, never()).execute(any());
        }
    }
}
