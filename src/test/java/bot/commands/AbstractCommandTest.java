package bot.commands;

import bot.BotAnimeCards;
import bot.MessageEventMock;
import bot.commands.user.trading.AddToMultiTradeCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.AnimeCardsGame;
import game.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

public class AbstractCommandTest<C extends AbstractCommand<A>, A> {

    protected static MessageEventMock messageEventMock;

    protected C command;
    protected A arguments;

    protected static BotAnimeCards bot;
    protected static AnimeCardsGame spyGame;
    protected Player tester;
    protected Player tester2;

    @BeforeAll
    static void beforeAll() {
        bot = new BotAnimeCards(mock(EventWaiter.class));
        bot.loadSettings("hibernate_test.cfg.xml");
        spyGame = spy(bot.getGame());
        messageEventMock = new MessageEventMock();
    }

    @AfterAll
    static void afterAll() {
        bot.getGame().getDatabaseSession().close();
    }

    @BeforeEach
    void loadSettings() {
        messageEventMock.reset();
        spyGame.reset();
        reset(spyGame);

        bot.loadTestGameSettings(spyGame);
        tester = spyGame.getOrCreatePlayer("409754559775375371");
        tester2 = spyGame.getOrCreatePlayer("347162620996091904");
    }

    protected void setCommand(C command) {
        this.command = command;
        arguments = createArguments(command);
    }

    protected void handleCommand() {
        handleCommand(command, tester, arguments);
    }

    protected void handleCommand(A args) {
        handleCommand(command, tester, args);
    }

    protected void handleCommand(Player player, A arguments) {
        handleCommand(command, player, arguments);
    }

    protected <T extends AbstractCommand<K>, K> K createArguments(T command) {
        return command.createArgumentsInstance(command.argumentsClass);
    }

    protected <T extends AbstractCommand<K>, K> void handleCommand(T command, Player player, K arguments) {
        command.game = spyGame;
        command.player = player;
        command.commandArgs = arguments;
        messageEventMock.setUser(player.getId());
        CommandEvent event = new CommandEvent(messageEventMock.getMock(), null, null);
        command.handle(event);
    }

    protected <T extends AbstractCommandNoArguments> void handleCommand(T command) {
        handleCommand(command, tester, command.createArgumentsInstance(AbstractCommandNoArguments.EmptyArguments.class));
    }
}
