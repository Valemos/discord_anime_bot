package bot;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.JDAImpl;
import org.mockito.*;

import javax.security.auth.login.LoginException;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class MessageSenderMock {

    private final String testCreatorId;

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
    private JDAImpl mJDA;
    @Mock
    private CommandClient mCommandClient;
    @Mock
    private EventWaiter mEventWaiter;
    @Mock
    private TextChannel mTextChannel;
    @Spy
    private final BotAnimeCards mBot = new BotAnimeCards();

    private final AbstractCommand<?>[] spyCommands;



    public MessageSenderMock() throws LoginException {
        MockitoAnnotations.initMocks(this);

        doReturn(mJDA).when(mBot).buildJDA(any());
        doCallRealMethod().when(mJDA).handleEvent(any());

        AbstractCommand<?>[] commands = mBot.getCommands(mBot.getGame());

        spyCommands = Arrays.stream(commands)
                .map(Mockito::spy)
                .toArray(AbstractCommand<?>[]::new);

        when(mBot.getCommands(any())).thenReturn(spyCommands);

        mBot.authenticate("");
        mBot.loadDefaultSettings();

        testCreatorId = mBot.getGame().getPlayerById("1").getId();
    }

    public void send(String message, String userId) {
        when(mMessageEvent.getMessage()).thenReturn(mMessage);
        when(mMessage.getContentRaw()).thenReturn(message);

        when(mMessageEvent.isFromType(ChannelType.TEXT)).thenReturn(true);
        when(mMessageEvent.isFromType(ChannelType.PRIVATE)).thenReturn(false);
        when(mMessageEvent.getTextChannel()).thenReturn(mTextChannel);
        when(mTextChannel.canTalk()).thenReturn(true);

        when(mMessageEvent.getJDA()).thenReturn(mJDA);
        when(mJDA.getSelfUser()).thenReturn(mSelfUser);
        when(mSelfUser.getId()).thenReturn(userId);

        mJDA.handleEvent(mMessageEvent);
    }

    public void assertCommandHandled(Class<? extends AbstractCommand<?>> commandClass){
        AbstractCommand<?> spyCommand = findSpyCommand(commandClass);
        verify(spyCommand).handle(mCommandEvent);
    }

    private AbstractCommand<?> findSpyCommand(Class<? extends AbstractCommand<?>> commandClass) {
        return Arrays.stream(spyCommands)
                .filter(cmd -> ! (cmd.getClass().equals(commandClass)))
                .findFirst().orElseThrow(RuntimeException::new);
    }
}
