package bot;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.impl.CommandClientImpl;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardGlobal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.mockito.*;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class BotMessageSenderMock {

    @Mock
    private MessageReceivedEvent mMessageEvent;
    @Mock
    private Message mMessage;
    @Mock
    private User mUser;
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
    @Captor
    private ArgumentCaptor<Consumer<? super Message>> actionCaptor;

    CommandClientImpl spyCommandClient;

    private BotAnimeCards spyBot;

    private AbstractCommand<?>[] spyCommands;

    public Player tester1;
    public Player tester2;
    public List<CardGlobal> cardsGlobal;
    public CardGlobal cardGlobal1;


    public BotMessageSenderMock() throws LoginException {
        MockitoAnnotations.initMocks(this);

        spyBot = initSpyBot();
        initBotSettings();
    }

    @NotNull
    private BotAnimeCards initSpyBot() throws LoginException {

        spyBot = Mockito.spy(new BotAnimeCards(mEventWaiter));
        spyOnCommands();

        doReturn(mJDA).when(spyBot).buildJDA(any());

        spyBot.authenticate("");
        return spyBot;
    }

    private void initBotSettings() {
        spyBot.loadDefaultGameSettings(spyBot.getGame());
        tester1 = spyBot.getGame().getPlayerById("409754559775375371");
        tester2 = spyBot.getGame().getPlayerById("347162620996091904");

        cardsGlobal = spyBot.getGame().getCardsGlobalManager().getAllCards();
        cardGlobal1 = cardsGlobal.get(0);

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
        when(mMessageChannel.sendMessage(any(MessageEmbed.class))).thenReturn(mMessageAction);
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
        Mockito.reset(spyCommands);
        Mockito.reset(mMessageEvent, mUser, mMessageChannel, mTextChannel, mMessageAction);
        initMessageEventMock();
    }

    public void send(String message) {
        send(message, tester1.getId());
    }

    public void send(String message, String userId) {
        when(mMessage.getContentRaw()).thenReturn(message);
        when(mUser.isBot()).thenReturn(false);
        when(mUser.getId()).thenReturn(userId);

        spyCommandClient.onEvent(mMessageEvent);
    }

    public void sendAndCaptureQueue(String message) {
        sendAndCaptureQueue(message, tester1.getId());
    }

    public void sendAndCaptureQueue(String message, String userId){
        doNothing().when(mMessageAction).queue(actionCaptor.capture());

        send(message, userId);

        when(mMessage.getId()).thenReturn("id");
        actionCaptor.getValue().accept(mMessage);
    }

    public AbstractCommand<?> findSpyCommand(Class<? extends AbstractCommand<?>> commandClass) {
        return Arrays.stream(spyCommands)
                .filter(cmd -> cmd.isSameCommandClass(commandClass))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public void assertCommandHandled(Class<? extends AbstractCommand<?>> commandClass){
        AbstractCommand<?> spyCommand = findSpyCommand(commandClass);
        verify(spyCommand).handle(any());
    }

    public void assertCommandNotHandled() {
        for (AbstractCommand<?> cmd : spyCommands){
            verify(cmd, never()).handle(any());
        }
    }


    public AnimeCardsGame getGame() {
        return spyBot.getGame();
    }

    public void resetGame() {
        spyBot.getGame().reset();
        initBotSettings();
    }

    public BotAnimeCards getBot() {
        return spyBot;
    }

    public void reset() {
        resetMocks();
        resetGame();
    }
}
