package bot.commands;

import bot.BotAnimeCards;
import bot.MessageEventMock;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.AnimeCardsGame;
import game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.spy;

public class AbstractCommandTest<C extends AbstractCommand<A>, A> {

    private static MessageEventMock messageEventMock;

    protected C command;

    @Mock
    private static EventWaiter mEventWaiter;

    protected static BotAnimeCards spyBot;
    protected static AnimeCardsGame spyGame;
    protected Player tester;
    protected Player tester2;

    @BeforeAll
    static void beforeAll() {
        spyBot = new BotAnimeCards(mEventWaiter);
        spyBot.loadSettings("hibernate_test.cfg.xml");
        spyGame = spy(spyBot.getGame());
        messageEventMock = new MessageEventMock();
    }

    @BeforeEach
    void loadSettings() {
        MockitoAnnotations.initMocks(this);
        messageEventMock.reset();

        spyGame.setEventWaiter(mEventWaiter);
        spyGame.setSession(spyGame.getDatabaseSession());

        spyBot.loadTestGameSettings(spyGame);
        tester = spyGame.getOrCreatePlayer("409754559775375371");
        tester2 = spyGame.getOrCreatePlayer("347162620996091904");
    }

    protected void setCommand(C commandObject) {
        command = commandObject;
    }

    protected void handleCommand(A arguments) {
        handleCommand(command, tester, arguments);
    }

    protected void handleCommand(Player player, A arguments) {
        handleCommand(command, player, arguments);
    }

    protected A createArguments() {
        return command.createArgumentsInstance(command.argumentsClass);
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
}
