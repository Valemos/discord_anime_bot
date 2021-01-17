package bot;

import bot.commands.AbstractCommand;
import bot.menu.EmojiMenuHandler;
import bot.menu.MenuEmoji;
import com.jagrosh.jdautilities.command.impl.CommandClientImpl;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardGlobal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
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
    private MessageReactionAddEvent mReactionAddEvent;
    @Mock
    private MessageReaction.ReactionEmote mReactionEmote;
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
        when(mMessageEvent.getMessage()).thenReturn(mMessage);
        when(mMessageEvent.getAuthor()).thenReturn(mUser);
        setEventChannel(mMessageEvent, ChannelType.TEXT);
        setMessageChannelMock();
    }

    private void setMessageChannelMock() {
        when(mMessageChannel.sendMessage(anyString())).thenReturn(mMessageAction);
        when(mMessageChannel.sendMessage(any(MessageEmbed.class))).thenReturn(mMessageAction);
        when(mMessageChannel.sendMessage(any(Message.class))).thenReturn(mMessageAction);
        when(mMessage.editMessage(any(Message.class))).thenReturn(mMessageAction);
    }

    private void setEventChannel(GenericMessageEvent event, ChannelType channelType) {
        when(event.getJDA()).thenReturn(mJDA);
        when(event.getChannel()).thenReturn(mMessageChannel);
        when(event.getChannelType()).thenReturn(channelType);
        when(event.getTextChannel()).thenReturn(mTextChannel);
        when(mTextChannel.canTalk()).thenReturn(true);
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
        send(message, tester1.getId(), "1");
    }

    public void send(String message, String userId, String messageId) {
        setMessageMock(messageId, message);
        setUserMock(userId);

        spyCommandClient.onEvent(mMessageEvent);
    }

    private void setUserMock(String userId) {
        when(mUser.getId()).thenReturn(userId);
        when(mUser.isBot()).thenReturn(false);
    }

    private void setMessageMock(String messageId) {
        setMessageMock(messageId, "");
    }

    private void setMessageMock(String messageId, String message) {
        if (messageId != null) when(mMessage.getId()).thenReturn(messageId);
        when(mMessage.getContentRaw()).thenReturn(message);
    }

    private void setEventMessage(MessageReactionAddEvent event, String messageId, String playerId) {
        when(event.getMessageId()).thenReturn(messageId);
        when(event.getUserId()).thenReturn(playerId);
    }

    public void sendAndCaptureMessage(String message) {
        sendAndCaptureMessage(message, tester1.getId(), null);
    }

    public void sendAndCaptureMessage(String message, String userId, String messageId){
        send(message, userId, messageId);

        verify(mMessageAction).queue(actionCaptor.capture());
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

    public MessageReactionAddEvent getReactionEventMock(MenuEmoji emoji) {
        when(mReactionAddEvent.getReactionEmote()).thenReturn(mReactionEmote);
        when(mReactionEmote.getEmoji()).thenReturn(emoji.getEmoji());
        when(mReactionAddEvent.getChannel()).thenReturn(mMessageChannel);
        return mReactionAddEvent;
    }

    public void chooseReactionMenu(Class<? extends AbstractCommand<?>> commandClass, String messageId, String playerId, MenuEmoji emoji) {
        EmojiMenuHandler command = (EmojiMenuHandler) findSpyCommand(commandClass);
        setMessageMock(messageId);
        setUserMock(playerId);

        setEventChannel(mReactionAddEvent, ChannelType.TEXT);
        setEventMessage(mReactionAddEvent, messageId, playerId);

        command.hReactionAddEvent(getReactionEventMock(emoji), getGame());
    }
}
