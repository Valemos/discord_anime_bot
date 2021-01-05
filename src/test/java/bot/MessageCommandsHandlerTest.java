package bot;

import bot.commands.BotCommandHandler;
import bot.commands.CreateCardHandler;
import bot.commands.DropHandler;
import game.AnimeCardsGame;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageCommandsHandlerTest {
    private MessageCommandsHandler commandHandler;
    private AnimeCardsGame game;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        commandHandler = new MessageCommandsHandler(game);
        commandHandler.setCommands(
                new ArrayList<>(List.of(
                        new DropHandler(),
                        new CreateCardHandler()
                )));

    }

    @Test
    void testCommandNameExtracted() {
        assertEquals("", BotCommandHandler.getCommandName(""));
        assertEquals("", BotCommandHandler.getCommandName("drop"));
        assertEquals("", BotCommandHandler.getCommandName("drop test"));
        assertEquals("", BotCommandHandler.getCommandName("# drop test"));
        assertEquals("drop", BotCommandHandler.getCommandName("#drop"));
        assertEquals("drop", BotCommandHandler.getCommandName("#drop test test"));
    }

    @Test
    void testIncorrectCommand() {
        assertNull(commandHandler.findCommandForString("#asdfds"));
    }

    @Test
    void testCommandHandled(){
        assertEquals(commandHandler.findCommandForString("#drop").getClass(), DropHandler.class);
    }

    @Test
    void testUserHasAccessToCommand() {
        game.addPlayer(new Player("1", AccessLevel.USER));
        game.addPlayer(new Player("2", AccessLevel.ADMIN));

        assertTrue(commandHandler.userHasAccessToCommand("1", new DropHandler()));
        assertTrue(commandHandler.userHasAccessToCommand("2", new DropHandler()));
        assertFalse(commandHandler.userHasAccessToCommand("2", new CreateCardHandler()));
    }

}