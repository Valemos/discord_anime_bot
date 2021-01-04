package bot;

import bot.commands.CreateCardHandler;
import bot.commands.DropHandler;
import game.AnimeCardsGame;
import game.User;
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
                        new DropHandler()
                )));

    }

    @Test
    void testCommandNameExtracted() {
        assertEquals("", commandHandler.getCommandName(""));
        assertEquals("", commandHandler.getCommandName("drop"));
        assertEquals("", commandHandler.getCommandName("drop test"));
        assertEquals("", commandHandler.getCommandName("# drop test"));
        assertEquals("drop", commandHandler.getCommandName("#drop"));
        assertEquals("drop", commandHandler.getCommandName("#drop test test"));
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
        game.addUser(new User("1", AccessLevel.USER));
        game.addUser(new User("2", AccessLevel.ADMIN));

        assertTrue(commandHandler.userHasAccessToCommand("1", new DropHandler()));
        assertTrue(commandHandler.userHasAccessToCommand("2", new DropHandler()));
        assertFalse(commandHandler.userHasAccessToCommand("2", new CreateCardHandler()));
    }

}