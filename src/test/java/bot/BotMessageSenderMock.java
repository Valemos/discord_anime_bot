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
import game.player_objects.ArmorItemPersonal;
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

    private MessageEventMock messageEventMock;

    @Mock
    private MessageReactionAddEvent mReactionAddEvent;
    @Mock
    private MessageReaction.ReactionEmote mReactionEmote;
    @Mock
    private RestAction<User> mRestActionUser;
    @Captor
    private ArgumentCaptor<Consumer<? super User>> userActionCaptor;
    @Mock
    private EventWaiter mEventWaiter;
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

        messageEventMock = new MessageEventMock();
        doReturn(messageEventMock.getJDA()).when(spyBot).buildJDA(any());


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
        when(messageEventMock.getJDA().retrieveUserById(anyString())).thenReturn(mRestActionUser);
        dropTimer.run();
        verify(mRestActionUser, atLeastOnce()).queue(userActionCaptor.capture());
        userActionCaptor.getValue().accept(messageEventMock.getUser());
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

        messageEventMock = new MessageEventMock();
    }

    private void clearArmorItemsDatabase() {
        clearTable(ArmorItemPersonal.class);
        clearTable(ArmorItem.class);
    }

    public <T> void clearTable(Class<T> entityClass) {
        databaseSession.beginTransaction();

        List<T> objects = AnimeCardsGame.getAllObjects(databaseSession, entityClass);
        for (T obj : objects){
            databaseSession.delete(obj);
        }

        databaseSession.getTransaction().commit();
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
        Mockito.reset(messageEventMock.getMock());
        messageEventMock.initMessageEventMock();
    }

    public void send(String message) {
        send(message, testerDefault.getId(), "1");
    }

    public void send(String message, String userId, String messageId) {
        messageEventMock.setMessage(messageId, message);
        messageEventMock.setUser(userId);

        spyCommandClient.onEvent(messageEventMock.getMock());
    }

    public void sendAndCaptureMessage(String message) {
        sendAndCaptureMessage(message, testerDefault.getId(), "default");
    }

    public void sendAndCaptureMessage(String message, String userId, String messageId){
        send(message, userId, messageId);

        verify(messageEventMock.getAction(), atLeast(1)).queue(messageActionCaptor.capture());
        messageActionCaptor.getValue().accept(messageEventMock.getMessage());
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

        MessageChannel channel = messageEventMock.getMessage().getChannel();
        when(mReactionAddEvent.getChannel()).thenReturn(channel);

        return mReactionAddEvent;
    }

    public void chooseDropMenuReaction(String messageId, String playerId, MenuEmoji emoji) {
        chooseMenuReaction(getGame().getDropManager().getActivity(messageId).getMenu(), messageId, playerId, emoji);
    }

    public void chooseMenuReaction(EmojiMenuHandler menu, String messageId, String playerId, MenuEmoji emoji) {
        messageEventMock.setMessage(messageId);
        messageEventMock.setUser(playerId);

        messageEventMock.setEventChannel(mReactionAddEvent, ChannelType.TEXT);
        messageEventMock.setEventMessage(mReactionAddEvent, messageId, playerId);

        menu.hReactionAddEvent(getReactionEventMock(emoji), getGame());
    }

    public Player tester() {
        return loadTester(testerDefault);
    }

    public Player loadTester(Player tester) {
        return spyBot.getGame().getOrCreatePlayer(tester.getId());
    }

    public void assertMessageQueueWithoutConsumer() {
        verify(messageEventMock.getAction(), atLeastOnce()).queue();
        verify(messageEventMock.getAction(), never()).queue(any());
    }

}
