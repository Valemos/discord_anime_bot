package bot;

import bot.commands.AbstractCommand;
import bot.commands.user.carddrop.CardDropTimer;
import bot.menu.EmojiMenuHandler;
import bot.menu.MenuEmoji;
import com.jagrosh.jdautilities.command.impl.CommandClientImpl;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardGlobal;
import game.player_objects.squadron.Squadron;
import game.shop.items.ArmorItem;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.hibernate.Session;
import org.mockito.*;

import javax.annotation.Nonnull;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
    private RestAction<Void> mVoidRestAction;
    @Mock
    private User mUser;
    @Mock
    private RestAction<User> mRestActionUser;
    @Captor
    private ArgumentCaptor<Consumer<? super User>> userActionCaptor;
    @Mock
    private EventWaiter mEventWaiter;
    @Mock
    private TextChannel mTextChannel;
    @Mock
    private MessageChannel mMessageChannel;
    @Mock
    public MessageAction mMessageAction;
    @Mock
    private JDA mJDA;
    @Captor
    private ArgumentCaptor<Consumer<? super Message>> messageActionCaptor;
    @Mock
    private Timer mDropTimer;
    @Captor
    private ArgumentCaptor<TimerTask> cardDropCaptor;


    CommandClientImpl spyCommandClient;

    private BotAnimeCards spyBot;
    private AnimeCardsGame spyGame;
    private Session databaseSession;

    private AbstractCommand<?>[] spyCommands;


    public Player testerDefault;
    public Player tester1;
    public Player tester2;
    public List<CardGlobal> cardsGlobal;
    public CardGlobal cardGlobal1;


    public BotMessageSenderMock() throws LoginException {
        MockitoAnnotations.initMocks(this);
        spyBot = initSpyBot();
        initBotSettings();
    }

    @Nonnull
    private BotAnimeCards initSpyBot() throws LoginException {

        spyBot = Mockito.spy(new BotAnimeCards(mEventWaiter));
        spyBot.loadSettings("hibernate_test.cfg.xml");
        spyGame = spy(spyBot.getGame());
        spyBot.setGame(spyGame);

        databaseSession = spyGame.getDatabaseSession();


        spyOnCommands();
        setDropTimerMock();

        doReturn(mJDA).when(spyBot).buildJDA(any());


        spyBot.authenticate("");
        return spyBot;
    }

    public void setTesterDefault(Player player){
        testerDefault = player;
    }

    private void setDropTimerMock() {
        spyBot.getGame().getDropManager().setFightTimer(mDropTimer);

    }

    public void finishDropTimer(){
        verify(mDropTimer, atLeastOnce()).schedule(cardDropCaptor.capture(), anyLong());
        CardDropTimer dropTimer = (CardDropTimer) cardDropCaptor.getValue();
        captureNotifyPlayers(dropTimer);
    }

    private void captureNotifyPlayers(CardDropTimer dropTimer) {
        when(mJDA.retrieveUserById(anyString())).thenReturn(mRestActionUser);
        dropTimer.run();
        verify(mRestActionUser, atLeastOnce()).queue(userActionCaptor.capture());
        userActionCaptor.getValue().accept(mUser);
    }

    private void initBotSettings() {
        clearArmorItemsDatabase();
        spyBot.getGame().setSession(databaseSession);
        spyBot.loadTestGameSettings(spyGame);
        tester1 = spyBot.getGame().getOrCreatePlayer("409754559775375371");
        tester2 = spyBot.getGame().getOrCreatePlayer("347162620996091904");
        testerDefault = tester1;

        cardsGlobal = spyBot.getGame().getCardsGlobal().getAllCards();
        cardGlobal1 = cardsGlobal.get(0);

        initMessageEventMock();
    }

    private void clearArmorItemsDatabase() {
        clearTable(ArmorItem.class);
    }

    public void clearSquadronsTable() {
        List<Squadron> squadrons = getAllObjects(Squadron.class);
        for (Squadron sq : squadrons){
            getGame().clearSquadron(sq);

            databaseSession.beginTransaction();
            sq.getOwner().setSquadron(null);
            databaseSession.merge(sq.getOwner());
            databaseSession.delete(sq);
            databaseSession.getTransaction().commit();
        }
    }

    private <T> void clearTable(Class<T> entityClass) {
        databaseSession.beginTransaction();

        List<T> objects = getAllObjects(entityClass);
        for (T obj : objects){
            databaseSession.delete(obj);
        }

        databaseSession.getTransaction().commit();
    }

    private <T> List<T> getAllObjects(Class<T> entityClass) {
        CriteriaBuilder cb = databaseSession.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityClass);
        return databaseSession.createQuery(q.select(q.from(entityClass))).list();
    }


    private void initMessageEventMock() {
        when(mMessageEvent.getMessage()).thenReturn(mMessage);
        when(mMessageEvent.getAuthor()).thenReturn(mUser);
        setEventChannel(mMessageEvent, ChannelType.TEXT);
        setMessageChannelMock();
    }

    private void setMessageChannelMock() {
        when(mMessageChannel.getJDA()).thenReturn(mJDA);
        when(mMessageChannel.sendMessage(anyString())).thenReturn(mMessageAction);
        when(mMessageChannel.sendMessage(any(MessageEmbed.class))).thenReturn(mMessageAction);
        when(mMessageChannel.sendMessage(any(Message.class))).thenReturn(mMessageAction);
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
        Mockito.reset(spyGame, spyBot);
        Mockito.reset(mMessageEvent, mUser, mMessageChannel, mTextChannel, mMessageAction);
        initMessageEventMock();
    }

    public void send(String message) {
        send(message, testerDefault.getId(), "1");
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
        when(mMessage.editMessage(any(Message.class))).thenReturn(mMessageAction);
        when(mMessage.addReaction(anyString())).thenReturn(mVoidRestAction);
        when(mMessage.getChannel()).thenReturn(mMessageChannel);
    }

    private void setEventMessage(MessageReactionAddEvent event, String messageId, String playerId) {
        when(event.getMessageId()).thenReturn(messageId);
        when(event.getUserId()).thenReturn(playerId);
    }

    public void sendAndCaptureMessage(String message) {
        sendAndCaptureMessage(message, testerDefault.getId(), "default");
    }

    public void sendAndCaptureMessage(String message, String userId, String messageId){
        send(message, userId, messageId);

        verify(mMessageAction, atLeast(1)).queue(messageActionCaptor.capture());
        messageActionCaptor.getValue().accept(mMessage);
    }

    public AbstractCommand<?> findSpyCommand(Class<? extends AbstractCommand<?>> commandClass) {
        return Arrays.stream(spyCommands)
                .filter(cmd -> cmd.isSameCommandClass(commandClass))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public void assertCommandHandled(Class<? extends AbstractCommand<?>> commandClass) {
        assertCommandHandled(commandClass, 1);
    }

    public void assertCommandHandled(Class<? extends AbstractCommand<?>> commandClass, int timesCalled){
        AbstractCommand<?> spyCommand = findSpyCommand(commandClass);
        verify(spyCommand, times(timesCalled)).handle(any());
    }

    public void assertCommandNotHandled() {
        for (AbstractCommand<?> cmd : spyCommands){
            verify(cmd, never()).handle(any());
        }
    }


    public AnimeCardsGame getGame() {
        return spyGame;
    }

    public void resetGame() {
        initBotSettings();
        setDropTimerMock();
        getGame().getOrCreatePlayer(tester1.getId()).getCooldowns().reset();
        getGame().getOrCreatePlayer(tester2.getId()).getCooldowns().reset();
    }

    public BotAnimeCards getBot() {
        return spyBot;
    }

    public void reset() {
        testerDefault = tester1;
        resetMocks();
        resetGame();
    }

    public MessageReactionAddEvent getReactionEventMock(MenuEmoji emoji) {
        when(mReactionAddEvent.getReactionEmote()).thenReturn(mReactionEmote);
        when(mReactionEmote.getEmoji()).thenReturn(emoji.getEmoji());
        when(mReactionAddEvent.getChannel()).thenReturn(mMessageChannel);

        return mReactionAddEvent;
    }

    public void chooseDropMenuReaction(String messageId, String playerId, MenuEmoji emoji) {
        chooseMenuReaction(getGame().getDropManager().getActivity(messageId).getMenu(), messageId, playerId, emoji);
    }

    public void chooseMenuReaction(EmojiMenuHandler menu, String messageId, String playerId, MenuEmoji emoji) {
        setMessageMock(messageId);
        setUserMock(playerId);

        setEventChannel(mReactionAddEvent, ChannelType.TEXT);
        setEventMessage(mReactionAddEvent, messageId, playerId);

        menu.hReactionAddEvent(getReactionEventMock(emoji), getGame());
    }

    public Player tester() {
        return loadTester(testerDefault);
    }

    public Player loadTester(Player tester) {
        return spyBot.getGame().getOrCreatePlayer(tester.getId());
    }

    public void assertMessageQueueWithoutConsumer() {
        verify(mMessageAction, atLeastOnce()).queue();
        verify(mMessageAction, never()).queue(any());
    }

}
